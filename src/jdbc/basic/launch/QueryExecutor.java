package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutor {

	public static List<Map<String, Object>> readAllRows(String dbName, String tableName) {
		String qry = String.format("SELECT * FROM `%s`.`%s` ", dbName, tableName);
		List<Map<String, Object>> rows = new ArrayList<>();

		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry)) {
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = new LinkedHashMap<>();
				for (int i = 1; i <= colCount; i++) {
					row.put(meta.getColumnName(i), rs.getObject(i));
				}
				rows.add(row);
			}
		} catch (SQLException e) {
			Map<String, Object> errorRow = new LinkedHashMap<>();
			errorRow.put("Error", e.getMessage());
			errorRow.put("Table", tableName);

			rows.add(errorRow);
		}
		return rows;

	}

	public static Map<String, Object> readOneRowByCrenditial(String dbName, String tableName, String userName,
			String password) {
		String qry = String.format("SELECT *  FROM `%s`.`%s` WHERE `USERNAME` = ? AND `PASSWORD` = ? LIMIT 1", dbName,
				tableName);
		Map<String, Object> row = new LinkedHashMap<>();
		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry)) {
			ps.setString(1, userName);
			ps.setString(2, password);
			row = fireExecutQuery(dbName, tableName, qry, ps);

		} catch (SQLException e) {
			row.put("Error", e.getMessage());
		}
		return row;
	}

	public static Map<String, Object> readOneRowById(String dbName, String tableName, int intValue) {
		String qry = String.format("SELECT *  FROM `%s`.`%s` WHERE `SN` = ? LIMIT 1", dbName, tableName);
		Map<String, Object> row = new LinkedHashMap<>();
		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry)) {
			ps.setInt(1, intValue);
			row = fireExecutQuery(dbName, tableName, qry, ps);
		} catch (SQLException e) {
			row.put("Error", e.getMessage());
		}
		return row;
	}

	public static Map<String, Object> fireExecutQuery(String dbName, String tableName, String sql, PreparedStatement ps)
			throws SQLException {
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		Map<String, Object> row = new LinkedHashMap<>();
		int colCount = meta.getColumnCount();
		if (rs.next()) {
			for (int i = 1; i <= colCount; i++) {
				String colName = meta.getColumnName(i);
				Object colValue = rs.getObject(i);
				row.put(colName, colValue);
			}
		} else {
			row.put("fail", "Not found");
		}
		return row;
	}

}
