package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchemaInspector {

	/**
	 * Returns a list of user-defined tables from the specified database.
	 *
	 * Uses JDBC metadata to introspect schema structure. Only includes tables of
	 * type "TABLE" (excludes views/system tables).
	 *
	 * @param dbName Name of the database to inspect.
	 * @return List of table names in the given database.
	 * @see #getCustomDatabases()
	 */
	public static List<String> showTables(String dbName) {
		List<String> tables = new ArrayList<>();
		try (Connection con = DbConnectionManager.getCon()) {
			DatabaseMetaData meta = con.getMetaData();
			try (ResultSet table = meta.getTables(dbName, null, "%", new String[] { "TABLE" })) {
				while (table.next()) {
					tables.add(table.getString("TABLE_NAME"));
				}
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Error while extracting tables. "));
		}
		if (tables.isEmpty()) {
			System.out.println(StyledMessage.Status.warning("No tables found in database '" + dbName + "'."));
		}
		return tables;
	}

	/**
	 * Retrieves all custom database names along with their associated user-defined
	 * tables.
	 *
	 * This method introspects the server-level schema using JDBC metadata. It
	 * avoids manual SQL queries and works across all catalogs.
	 *
	 * Purpose: Useful for debugging, introspection, or admin-level tooling. Not
	 * recommended for interactive CLI UX where the user already selected a DB.
	 *
	 * Steps: 1. Prepare a LinkedHashMap to preserve insertion order. 2. Connect to
	 * the DB server (not a specific database). 3. Fetch custom catalogs using
	 * getCustomDatabases(). 4. For each catalog: - Use metaData.getTables(...)
	 * with: • catalog: database name • schemaPattern: null (MySQL doesn't use
	 * schemas like Oracle) • tableNamePattern: "%" (wildcard for all table names) •
	 * types: {"TABLE"} (includes only user-defined tables) - Collect table names
	 * into a list. - Put the list into the map with dbName as key. 5. Return the
	 * populated map.
	 *
	 * If no databases or tables are found, a warning is printed.
	 *
	 * @return Map of database name → list of table names.
	 * @see #getCustomDatabases()
	 */
	public static Map<String, List<String>> getAllDatabasesWithTables() {
		Map<String, List<String>> meta = new LinkedHashMap<>();
		try (Connection con = DbConnectionManager.getCon()) {
			DatabaseMetaData metaData = con.getMetaData();
			for (String catalog : getCustomDatabases()) {
				String[] type = { "TABLE" };
				try (ResultSet tables = metaData.getTables(catalog, null, "%", type)) {
					List<String> tableNames = new ArrayList<>();
					while (tables.next()) {
						String tableName = tables.getString("TABLE_NAME");
						tableNames.add(tableName);
					}
					meta.put(catalog, tableNames);
				}
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Error while extracting databases and tables. "));
		}
		if (meta.isEmpty()) {
			System.out.println(StyledMessage.Status.warning("No databases and table found or accessible."));
		}
		return meta;
	}

	/**
	 * Purpose: Fetch user-defined (custom) databases.
	 *
	 * This method uses JDBC metadata to list all catalogs and filters out
	 * system-level schemas such as: - mysql - information_schema -
	 * performance_schema - sys
	 *
	 * Steps: 1. Connect to the DB server (not a specific database). 2. Fetch
	 * catalogs using DatabaseMetaData.getCatalogs() from con.getMetaData(). 3.
	 * Filter out system databases using a Set. 4. Return the list of custom
	 * databases.
	 *
	 * If no custom databases are found or accessible, a warning is printed.
	 *
	 * @return List of custom database names.
	 */

	public static List<String> getCustomDatabases() {
		List<String> customDbs = new ArrayList<>();
		// listing system-level databases.
		Set<String> systemDbs = Set.of("mysql", "information_schema", "performance_schema", "sys");
		try (Connection con = DbConnectionManager.getCon()) {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet catalogs = meta.getCatalogs();
			while (catalogs.next()) {
				String dbName = catalogs.getString("TABLE_CAT");
				if (!systemDbs.contains(dbName)) {
					customDbs.add(dbName);
				}
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Error while extracting databases. " + e.getMessage()));
		}
		if (customDbs.isEmpty()) {
			System.out.println(StyledMessage.Status.warning("No databases found or accessible."));
		}
		return customDbs;
	}

	/**
	 * Checks whether a given column value is unique in the specified table.
	 *
	 * This method checks if a value already exists by counting how many rows match
	 * it in the table.
	 *
	 * - If the count is 0, the value is considered unique. - If the count is
	 * greater than 0, the value already exists. - If any SQL error occurs, the
	 * method assumes the value is not unique.
	 *
	 * This is useful for insert validation, CLI-driven UX, and schema-aware
	 * workflows.
	 *
	 * @param dbName    the name of the database (catalog)
	 * @param tableName the name of the table to inspect
	 * @param colName   the column to check for uniqueness
	 * @param colValue  the value to validate
	 * @return true if the value is unique (i.e., does not exist); false otherwise
	 */

	public static boolean isColumnValueUnique(String dbName, String tableName, String colName, String colValue) {
		String qry = String.format("SELECT COUNT(*) FROM `%s`.`%s` WHERE `%s` = ?", dbName, tableName, colName);
		try (Connection con = DbConnectionManager.getDbConnection(dbName);
				PreparedStatement ps = con.prepareStatement(qry);) {
			ps.setString(1, colValue);
			ResultSet rs = ps.executeQuery(); // execute query to count matching rows
			if (rs.next()) { // move cursor forward (first row)
				int count = rs.getInt(1); // get value from first row (i.e., count of matches),count == 0->unique,
											// count > 0 not unique.
				return count == 0; // true if no match found — value is unique
			}
			return false;

		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Error while checking uniqueness " + e.getMessage()));
			return false;// assume not unique.
		}
	}

	/**
	 * Extracts metadata for insertable columns from the specified table.
	 *
	 * This method filters out auto-increment fields (like primary keys) and returns
	 * a list of ColumnMeta records. Each record includes: - Column name - SQL type
	 * code - Mandatory flag (true if NOT NULL) - Uniqueness flag (true if column
	 * has a UNIQUE constraint)
	 *
	 * Internally, it uses two metadata queries: 1. getColumns(...) → to retrieve
	 * column definitions, including nullability and auto-increment status 2.
	 * getIndexInfo(...) → to identify columns that are part of UNIQUE indexes
	 *
	 * Each row in the index ResultSet represents a column involved in a unique
	 * constraint. The column name is added to a set for fast lookup during metadata
	 * assembly.
	 *
	 * Useful for dynamic insert flows, schema-aware validation, and CLI-driven UX.
	 *
	 * @param dbName    the name of the database
	 * @param tableName the name of the table to inspect
	 * @return a list of ColumnMeta objects representing insertable fields
	 */

	public static List<ColumnMeta> getInsertableColumns(String dbName, String tableName) {
		List<ColumnMeta> columns = new ArrayList<>();
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return columns;
		}
		try (Connection con = DbConnectionManager.getDbConnection(dbName)) {
			DatabaseMetaData meta = con.getMetaData();// now meta has all meta info of dbName.
			// step 1. collect unique columns
			Set<String> uniqueCols = new HashSet<>();
			try (ResultSet indexRs = meta.getIndexInfo(dbName, null, tableName, true, false)) {
				while (indexRs.next()) {
					String col = indexRs.getString("COLUMN_NAME");
					if (col != null) {
						uniqueCols.add(col);
					}
				}
			}
			// Step 2: Collect insertable columns
			try (ResultSet rs = meta.getColumns(dbName, null, tableName, null)) {
				while (rs.next()) {
					String colName = rs.getString("COLUMN_NAME");
					int dataType = rs.getInt("DATA_TYPE");
					int nullable = rs.getInt("NULLABLE"); // 0 = NOT NULL, 1 = NULLABLE, 2 = UNKNOWN
					String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");// YES or NO
					if (!"YES".equalsIgnoreCase(isAutoIncrement)) {
						boolean isMandatory = (nullable == DatabaseMetaData.columnNoNulls);
						// public static final int columnNoNulls = 0 -> defined in DatabaseMetaData.
						// nullable = 0 → NOT NULL → isMandatory = true
						// nullable = 1/2 → NULLABLE/UNKNOWN → isMandatory = false

						boolean isUnique = uniqueCols.contains(colName);
						columns.add(new ColumnMeta(colName, dataType, isMandatory, isUnique));
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(StyledMessage.Status.error("Error while extracting insertable columns. "));
			e.printStackTrace();
		}
		return columns;
	}

}
