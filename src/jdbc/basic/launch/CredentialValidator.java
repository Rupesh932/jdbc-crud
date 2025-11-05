package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.basic.launch.Constants.QueryStatus;

public class CredentialValidator {
	public static boolean userExist(String dbName, String tableName, String userName) {
		return !SchemaInspector.isColumnValueUnique(dbName, tableName, "USERNAME", userName);

	}

	public static boolean isPasswordMatched(String dbName, String tableName, String userName, String inputPassword) {
		if (dbName == null || tableName == null || userName == null || inputPassword == null) {
			System.out.println(StyledMessage.Status.warning("Invalid input for password check"));
			return false;
		}
		String qry = String.format("SELECT PASSWORD FROM `%s`.`%s` WHERE USERNAME = ?", dbName, tableName);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry);) {
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String storedHash = rs.getString("PASSWORD");
				return PasswordHasher.verify(inputPassword, storedHash);
			} else {
				System.out.println(StyledMessage.Status.warning(QueryStatus.USER_NOT_FOUND.getMessage()));
				return false;
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.warning("Error during password matched"));
			e.printStackTrace();
			return false;
		}

	}

	public static boolean isLoginValid(String dbName, String tableName, String userName, String password) {
		return userExist(dbName, tableName, userName) && isPasswordMatched(dbName, tableName, userName, password);
	}
}
