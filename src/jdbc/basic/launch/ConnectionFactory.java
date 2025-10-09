package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jdbc.basic.launch.Constants.OperationStatus;

public class ConnectionFactory {
	private static final String URL = "jdbc:mysql://localhost:3306";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "";

	public static Connection getCon() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void closeResource(AutoCloseable resource) {
		try {
			if (resource != null) {
				resource.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static OperationStatus createDataBase(String dbName) {
		String qry = "CREATE DATABASE IF NOT EXISTS " + dbName;
		try (Connection con = getCon(); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			return Constants.OperationStatus.SUCCESS;

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.FAILED;
		}
	}

	public static boolean isDatabaseExist(String dbName) {
		String qry = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
		// INFORMATION_SCHEMA.SCHEMATA → metadata table ho, jasma sabai database ko naam
		// hunxa.
		try (Connection con = getCon(); PreparedStatement ps = con.prepareStatement(qry)) {
			ps.setString(1, dbName);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next(); // true if DB exists
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Connection getDbConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(URL + "/" + dbName, USER_NAME, PASSWORD);
	}

	public static OperationStatus createTable(String dbName, String tableName, Map<String, String> cols) {
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
		try (Connection con = getDbConnection(dbName); Statement st = con.createStatement()) {
			st.execute(sqlQuery.toString());
			return Constants.OperationStatus.SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.FAILED;
		}
	}

	public static String insertDataUsingStatement(String dbName, String tableName,
			LinkedHashMap<String, Object> rowData) {
		StringBuilder colName = new StringBuilder();
		StringBuilder colData = new StringBuilder();

		for (Entry<String, Object> entry : rowData.entrySet()) {
			String cols = entry.getKey();
			Object values = entry.getValue();

			colName.append(cols).append(", ");
			// handles null values:
			if (values == null) {
				colData.append("null").append(", ");
			} else {
				String safeValue = values.toString().replace("'", "''");
				colData.append("'").append(safeValue).append("'").append(", ");
			}
		}
		// remove last comma and space
		colName.setLength(colName.length() - 2);
		colData.setLength(colData.length() - 2);

		String insertQuery = "INSERT INTO " + tableName + "(" + colName + ") values (" + colData + ")";
		// System.out.println(insertQuery);
		return executeUpdateUsingStatement(dbName, insertQuery);

	}

	/**
	 * Checks whether a given column value already exists in the specified table.
	 * 
	 * @param dbName    the name of the database
	 * @param tableName the name of the table
	 * @param colName   the column to check for uniqueness
	 * @param colValue  the value to check
	 * @return true if the value already exists (i.e., not unique), false if it's
	 *         unique If any SQL error occurs, assumes value is not unique
	 *         (defensive fallback)
	 */

	public static boolean isColumnValueUnique(String dbName, String tableName, String colName, String colValue) {
		String qry = String.format("SELECT COUNT(*) FROM `%s`.`%s` WHERE `%s` = ?", dbName, tableName, colName);
		try (Connection con = getDbConnection(dbName); PreparedStatement ps = con.prepareStatement(qry);) {
			ps.setString(1, colValue);
			ResultSet rs = ps.executeQuery(); // query fire भयो
			rs.next();                        // cursor अगाडि सारियो (first row मा)
			int count = rs.getInt(1);         //getInt(1) ले ResultSet को पहिलो column को value निकाल्छ,यहाँ 1 भनेको column index हो
			return count == 0;                // यदि count 0 हो भने unique हो

		} catch (SQLException e) {
			System.out.println(
					MessageStyler.makeRed() + Emoji.ERROR + "Error while checking uniqueness " + e.getMessage());
			return false;// assume not unique.
		}
	}

	public static OperationStatus insertDataUsingPS(String dbName, String tableName, Map<String, Object> rowData) {
		StringBuilder columns = new StringBuilder(" (");
		StringBuilder placeholders = new StringBuilder();
		for (String col : rowData.keySet()) {
			columns.append(col).append(", ");
			placeholders.append("?, ");
		}
		columns.setLength(columns.length() - 2);
		placeholders.setLength(placeholders.length() - 2);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into ").append(dbName).append(".").append(tableName).append(columns)
				.append(") values (").append(placeholders).append(")");
		String sql = sqlBuilder.toString();

		List<Object> colValues = new ArrayList<>(rowData.values());
		return executeUpdateUsingPS(dbName, sql, colValues);
	}

	public static OperationStatus executeUpdateUsingPS(String dbName, String sqlQuery, List<Object> colValues) {
		try (Connection con = getDbConnection(dbName); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
			for (int i = 0; i < colValues.size(); i++) {
				ps.setObject(i + 1, colValues.get(i));
			}
			return (ps.executeUpdate() > 0) ? Constants.OperationStatus.SUCCESS : Constants.OperationStatus.FAILED;

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.ERROR;
		}
	}

	/**
	 * Extracts metadata for insertable columns from the specified table.
	 * 
	 * This method filters out auto-increment fields (e.g., primary keys) and
	 * returns a list of ColumnMeta records, each containing: - Column name - SQL
	 * type code - Mandatory flag (true if NOT NULL) - Uniqueness flag (true if
	 * column has a UNIQUE constraint)
	 * 
	 * Internally, it uses two metadata queries: 1. getColumns(...) → to retrieve
	 * column definitions, including nullability and auto-increment status 2.
	 * getIndexInfo(...) → to identify columns that are part of UNIQUE indexes
	 * 
	 * Each row in the index ResultSet (`indexRs`) represents a column involved in a
	 * unique constraint. The column name (`col`) is extracted and added to a set
	 * for fast lookup during metadata assembly.
	 * 
	 * This method is useful for dynamic insert flows, schema-aware validation, and
	 * CLI-driven UX.
	 *
	 * @param dbName    the name of the database (catalog)
	 * @param tableName the name of the table to inspect
	 * @return a list of ColumnMeta objects representing insertable fields
	 */

	public static List<ColumnMeta> getInsertableColumns(String dbName, String tableName) {
		List<ColumnMeta> columns = new ArrayList<>();
		if (isTableExists(dbName, tableName)) {
			try (Connection con = getDbConnection(dbName)) {
				DatabaseMetaData meta = con.getMetaData();// now meta has all meta info of dbName.
				ResultSet rs = meta.getColumns(dbName, null, tableName, null);// now rs has meta info of columns .
				// collect unique columns
				Set<String> uniqueCols = new HashSet<>();
				ResultSet indexRs = meta.getIndexInfo(dbName, null, tableName, true, false);
				while (indexRs.next()) {
					String col = indexRs.getString("COLUMN_NAME");
					if (col != null) {
						uniqueCols.add(col);
					}
				}
				while (rs.next()) {
					String colName = rs.getString("COLUMN_NAME");
					int dataType = rs.getInt("DATA_TYPE");
					int nullable = rs.getInt("NULLABLE"); // 0 = NOT NULL, 1 = NULLABLE, 2 = UNKNOWN
					String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");// YES or NO
					if (!"YES".equalsIgnoreCase(isAutoIncrement)) {
						boolean isMandatory = (nullable == DatabaseMetaData.columnNoNulls);
						// public static final int columnNoNulls = 0 -> defined in DatabaseMetaData.
						// nullable = 0 -> 0 == 0 -> isMnadatory = true -> not null fields.
						// nullable = 1/2 -> 1/2 == 0 -> isMnadatory = false -> optional fields.
						boolean isUnique = uniqueCols.contains(colName);
						columns.add(new ColumnMeta(colName, dataType, isMandatory, isUnique));
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return columns;
	}

	public static boolean isTableExists(String dbName, String tableName) {
		try (Connection con = ConnectionFactory.getDbConnection(dbName); Statement st = con.createStatement()) {
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

	// for insert ,update and delete query
	public static String executeUpdateUsingStatement(String dbName, String sqlQuery) {
		try (Connection con = getDbConnection(dbName); Statement st = con.createStatement()) {
//			int row = st.executeUpdate(sqlQuery);
//			if (row > 0) {
//				return Constants.SUCCESS;
//			} else {
//				return Constants.FAILED;
//			}
			return (st.executeUpdate(sqlQuery) > 0) ? Constants.OperationStatus.SUCCESS.getMessage()
					: Constants.OperationStatus.FAILED.getMessage();

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.ERROR.getMessage();
		}
	}

}
