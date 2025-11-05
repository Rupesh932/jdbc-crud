package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.basic.launch.Constants.QueryStatus;

public class AdminDeleteHelper {
	public static boolean isRowExist(String dbName, String tableName, String pk, Object rowIdentity) {
		// SELECT 1 FROM table_name WHERE pk1 = ? [AND pk2 = ? ...] LIMIT 1;
		String qry = String.format("SELECT 1 FROM `%s`.`%s` WHERE %s = ?", dbName, tableName, pk);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry);) {
			ps.setObject(1, rowIdentity);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error(e.getMessage()));
			return false;
		}
	}

	public static QueryStatus deleteRow(String dbName, String tableName, String pk, Object rowIdentity) {
		// DELETE FROM `table_name` WHERE pk_column = ?
		String delQry = String.format("DELETE FROM `%s`.`%s` WHERE %s = ?", dbName, tableName, pk);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
				PreparedStatement ps = con.prepareStatement(delQry)) {
			ps.setObject(1, rowIdentity);
			int row = ps.executeUpdate();
			if (row > 0) {
				return QueryStatus.ROW_DELETED;
			} else {
				return QueryStatus.ROW_DELETION_FAILED;
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error(e.getMessage()));
			return QueryStatus.SQL_EXCEPTION;
		}

	}
}
