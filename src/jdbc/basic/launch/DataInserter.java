package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jdbc.basic.launch.Constants.OperationStatus;

public class DataInserter {
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

	public static String executeUpdateUsingStatement(String dbName, String sqlQuery) {
		try (Connection con = DbConnectionManager.getDbConnection(dbName); Statement st = con.createStatement()) {
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
		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(sqlQuery)) {
			for (int i = 0; i < colValues.size(); i++) {
				ps.setObject(i + 1, colValues.get(i));
			}
			return (ps.executeUpdate() > 0) ? Constants.OperationStatus.SUCCESS : Constants.OperationStatus.FAILED;

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.OperationStatus.ERROR;
		}
	}

}
