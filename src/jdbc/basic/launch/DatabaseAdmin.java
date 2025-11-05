package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import jdbc.basic.launch.Constants.QueryStatus;

public class DatabaseAdmin {

	public static QueryStatus createDatabase(String dbName) {
		String qry = "CREATE DATABASE IF NOT EXISTS " + dbName;
		try (Connection con = DbConnectionManager.getServerConnection(); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			return QueryStatus.DB_CREATED;

		} catch (SQLException e) {

			e.printStackTrace();
			return QueryStatus.DB_CREATION_FAILED;
		}
	}

	public static QueryStatus dropDatabase() {
		String dbName = InputManager.dbNameInput("Enter database name to drop from server ");

		if (QueryStatus.DB_SKIPPED.name().equals(dbName)) {
			System.out.println(StyledMessage.Status.failed(QueryStatus.DB_NOT_FOUND.getMessage()));
			return QueryStatus.DB_NOT_FOUND;
		}
		String qry = String.format("DROP DATABASE `%s`", dbName);
		try (Connection con = DbConnectionManager.getServerConnection(); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			System.out.println(StyledMessage.Status.success("Database '" + dbName + "' dropped successfully."));
			return QueryStatus.DB_DROPPED;

		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Failed to drop database: " + e.getMessage()));
			return QueryStatus.EXCEPTION;
		}

	}

	public static QueryStatus createTable(String dbName, String tableName, Map<String, String> cols) {
		// 1. start query
		StringBuilder sqlQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");

		// 2. loop through map and build column definition
		for (Entry<String, String> entry : cols.entrySet()) {
			String colName = entry.getKey();
			String colConstrains = entry.getValue();
			sqlQuery.append(colName).append(" ").append(colConstrains).append(", ");
		}

		// 3. remove last(,) and space
		sqlQuery.setLength(sqlQuery.length() - 2);
		// 4.close the query.
		sqlQuery.append(")");
		// System.out.println(sqlQuery.toString());
		// 5.execute the query.
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			st.execute(sqlQuery.toString());
			return QueryStatus.TABLE_CREATED;
		} catch (SQLException e) {
			e.printStackTrace();
			return QueryStatus.TABLE_CREATION_FAILED;
		}
	}

	public static QueryStatus alterTable(String dbName, String tableName, QueryStatus status) {

		return AdminAlterHelper.handleAlterTable(dbName, tableName, status);
	}

	public static QueryStatus updateTable(String dbName, String tableName) {

		return AdminUpdateHelper.updateTable(dbName, tableName);

	}

	public static QueryStatus dropTable(String dbName) {
		String tableName = InputManager.tableNameInput(dbName, "Enter table name to drop it ");
		String qry = String.format("DROP TABLE `%s`.`%s`", dbName, tableName);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			System.out.println(StyledMessage.Status
					.success("Table '" + tableName + "' dropped successfully. from database '" + dbName + "'"));
			return QueryStatus.TABLE_DROPPED;
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Failed to drop table: " + e.getMessage()));
			return QueryStatus.EXCEPTION;
		}

	}

	public static boolean isTableExists(String dbName, String tableName) {
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			ResultSet rs = st.executeQuery("SHOW TABLES");
			while (rs.next()) {
				if (rs.getString(1).equals(tableName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static QueryStatus deleteRow(String dbName, String tableName) {
		List<String> pkCols = SchemaInspector.getPrimaryKeyCols(dbName, tableName);
		if (pkCols.isEmpty()) {
			String msg = StyledMessage.Status.warning(
					"Table '" + tableName + "' has not defined Primary key constraint, without PK CRUD operation "
							+ "might be hard, define PK while creating tables.");
			System.out.println(msg);
			return QueryStatus.PRIMARY_KEY_NOT_FOUND;
		}
		if (pkCols.size() > 1) {
			return QueryStatus.MULTI_PK_NOT_SUPPORTED;
		}

		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName)) {
			DatabaseMetaData meta = con.getMetaData();

			for (String pkCol : pkCols) {
				ResultSet rs = meta.getColumns(dbName, null, tableName, pkCol);
				if (rs.next()) {
					int sqlType = rs.getInt("DATA_TYPE");
					// String dataType = rs.getString("TYPE_NAME");
					String colType = CrudHelper.getColumnType(sqlType);
					String msg = StyledMessage.Status
							.info("Column '" + pkCol + "' is defined as 'primary key' of typed '" + colType
									+ "' for table '" + tableName + "' ");
					System.out.println(msg);
					Object rowIdentity = CrudHelper.getColumnValue(colType, pkCol);
					System.out.println(rowIdentity);
					boolean isRowExist = AdminDeleteHelper.isRowExist(dbName, tableName, pkCol, rowIdentity);
					if (!isRowExist) {
						return QueryStatus.ROW_NOT_EXITED;
					}
					//System.out.println("Row found");
					return AdminDeleteHelper.deleteRow(dbName, tableName, pkCol, rowIdentity);

				}
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error(e.getMessage()));
			return QueryStatus.SQL_EXCEPTION;
		}
		return null;
	}
}
