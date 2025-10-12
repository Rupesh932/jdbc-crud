package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;

import jdbc.basic.launch.Constants.OperationStatus;

public class DatabaseAdmin {

	public static OperationStatus createDataBase(String dbName) {
		String qry = "CREATE DATABASE IF NOT EXISTS " + dbName;
		try (Connection con = DbConnectionManager.getCon(); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			return Constants.OperationStatus.SUCCESS;

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.FAILED;
		}
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
		try (Connection con = DbConnectionManager.getDbConnection(dbName); Statement st = con.createStatement()) {
			st.execute(sqlQuery.toString());
			return Constants.OperationStatus.SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.FAILED;
		}
	}
	public static boolean isTableExists(String dbName, String tableName) {
		try (Connection con =  DbConnectionManager.getDbConnection(dbName); Statement st = con.createStatement()) {
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
}
