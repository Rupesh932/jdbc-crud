package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnectionManager {
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
	
	public static Connection getDbConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(URL + "/" + dbName, USER_NAME, PASSWORD);
	}
	
	public static boolean isDatabaseExist(String dbName) {
		String qry = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
		// INFORMATION_SCHEMA.SCHEMATA → metadata table-> it collects all database

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


	/**
	 * Safely closes any AutoCloseable resource. Useful when try-with-resources
	 * isn't applicable.
	 */
	public static void closeResource(AutoCloseable resource) {
		try {
			if (resource != null) {
				resource.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
