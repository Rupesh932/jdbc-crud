package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static void createDataBase(String dbName) {
		String qry = "CREATE DATABASE " + dbName;
		try (Connection con = getCon(); Statement st = con.createStatement()) {
			st.execute(qry);
			System.out.println("database creation successful.");
		} catch (SQLException e) {
			System.out.println("failed to create database.");
			e.printStackTrace();
		}
	}

	public static Connection getDbConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(URL + "/" + dbName, USER_NAME, PASSWORD);
	}

	public static String createTable(String dbName, String tableName, Map<String, String> cols) {
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

		// 5.execute the query.
		String successMsg = "table " + tableName + " is  created in " + dbName + " successfully done.";
		try (Connection con = getDbConnection(dbName); Statement st = con.createStatement()) {
			st.execute(sqlQuery.toString());
			return successMsg;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	public static int insertDataUsingStatement(String dbName, String tableName, LinkedHashMap<String, Object> rowData) {
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

	public static int insertDataUsingPS(String dbName, String tableName, LinkedHashMap<String, Object> rowData) {
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
		return executeUpdateUsingPS(dbName,sql,colValues);
	}

	public static int executeUpdateUsingPS(String dbName, String sqlQuery, List<Object> colValues) {
		try (Connection con = getDbConnection(dbName); PreparedStatement ps = con.prepareStatement(sqlQuery)) {
			for(int i = 0;i<colValues.size();i++) {
				ps.setObject(i+1, colValues.get(i));
			}
			return (ps.executeUpdate() > 0) ?Constants.SUCCESS:Constants.FAILED;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.ERROR;
		}
	}
   public static List<String> getColumnNames(String dbName,String tableName){
	   if(isTableExists(dbName,tableName)) {
		   try(Connection con = getDbConnection(dbName)){
			   DatabaseMetaData meta = con.getMetaData();
			   ResultSet rs = meta.getColumns(dbName, null, tableName, null);
			   List<String> colNames  = new ArrayList<>();
			   while(rs.next()) {
				   String cols = rs.getString("COLUMN_NAME");
				   colNames.add(cols);
			   }
			   return colNames;
		   }catch(SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   return Collections.emptyList();
   }
	public static boolean isTableExists(String dbName, String tableName) {
		try (Connection con = ConnectionFactory.getDbConnection(dbName); Statement st = con.createStatement()) {
			ResultSet rs = st.executeQuery("show tables");
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
	public static int executeUpdateUsingStatement(String dbName, String sqlQuery) {
		try (Connection con = getDbConnection(dbName); Statement st = con.createStatement()) {
//			int row = st.executeUpdate(sqlQuery);
//			if (row > 0) {
//				return Constants.SUCCESS;
//			} else {
//				return Constants.FAILED;
//			}
			return (st.executeUpdate(sqlQuery) > 0) ? Constants.SUCCESS : Constants.FAILED;

		} catch (SQLException e) {
			e.printStackTrace();
			return Constants.ERROR;
		}
	}

}
