package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchemaInspector {

	/**
	 * Checks whether a given column value already exists in the specified table.
	 * 
	 * @param dbName    the name of the database
	 * @param tableName the name of the table
	 * @param colName   the column to check for uniqueness
	 * @param colValue  the value to check
	 * @return true if the value not exists (i.e., unique), true if it's unique If
	 *         any SQL error occurs, assumes value is not unique (defensive
	 *         fallback)
	 */

	public static boolean isColumnValueUnique(String dbName, String tableName, String colName, String colValue) {
		String qry = String.format("SELECT COUNT(*) FROM `%s`.`%s` WHERE `%s` = ?", dbName, tableName, colName);
		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry);) {
			ps.setString(1, colValue);
			ResultSet rs = ps.executeQuery(); // execute query to count matching rows
			rs.next(); // move cursor forward (first row)
			int count = rs.getInt(1); // get value from first column (i.e., count of matches),count == 0->unique,
										// count > 0 not unique.
			return count == 0; // true if no match found — value is unique

		} catch (SQLException e) {
			System.out.println(
					MessageStyler.makeRed() + Emoji.ERROR + "Error while checking uniqueness " + e.getMessage());
			return false;// assume not unique.
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
		if (DatabaseAdmin.isTableExists(dbName, tableName)) {
			try (Connection con = DbConnectionManager.getDbConnection(dbName)) {
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

}
