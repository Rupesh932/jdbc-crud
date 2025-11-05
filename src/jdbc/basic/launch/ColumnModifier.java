package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import jdbc.basic.launch.Constants.ColumnAction;
import jdbc.basic.launch.Constants.IntConstant;
import jdbc.basic.launch.Constants.QueryStatus;

public class ColumnModifier {
	private String dbName;
	private String tableName;
	private String colName;
	private List<String[]> info;

	public ColumnModifier(String dbName, String tableName, String colName, List<String[]> info) {
		this.dbName = dbName;
		this.tableName = tableName;
		this.colName = colName;
		this.info = info;
	}

	public QueryStatus rename() {
		int count = IntConstant.ONE;
		while (true) {
			String newColName = Validation.validateColumnName("Enter new column name ");
			boolean existedName = info.stream().anyMatch(arr -> arr[0].equalsIgnoreCase(newColName));
			if (existedName) {
				String msg = StyledMessage.Status.warning("'" + newColName + "' is already taken");
				System.out.println(msg);
				if (count == IntConstant.TWO) {

					return QueryStatus.RENAME_FAILED;

				}
				count++;
				continue;
			}
			// ALTER TABLE table_name RENAME COLUMN old_column_name TO new_column_name;
			String sql = String.format("ALTER TABLE `%s`.`%s` RENAME COLUMN `%s` TO  `%s` ", dbName, tableName, colName,
					newColName);
			QueryStatus status = fireExecuteUpdate(sql);
			if (status == QueryStatus.SUCCESS) {
				this.colName = newColName;
				return QueryStatus.RENAME_SUCCESS;
			} else if (status == QueryStatus.SQL_EXCEPTION)
				return status;
			else {

				return QueryStatus.RENAME_FAILED;
			}
		}

	}

	public QueryStatus changeType() {
		int count = IntConstant.ONE;
		while (true) {
			String newColType = InputManager
					.stringInput("Enter data type for column '" + colName + "' e.g. VARCHAR(n)");
			boolean validType = Validation.isValidColumnType(newColType);
			if (validType) {
				// ALTER TABLE table_name MODIFY COLUMN column_name NEW_TYPE;
				String qry = String.format("ALTER TABLE `%s`.`%s` MODIFY COLUMN `%s` %s ", dbName, tableName, colName,
						newColType);
				QueryStatus status = fireExecuteUpdate(qry);
				if (status == QueryStatus.SUCCESS) {
					return QueryStatus.TYPE_CHANGE_SUCCESS;
				} else if (status == QueryStatus.SQL_EXCEPTION) {
					String msg = StyledMessage.Status
							.warning(" Type change failed due to incompatible existing values in column '" + colName
									+ "'.\n" + " For example, string values like 'hari' cannot be stored in INT type.\n"
									+ " Use 'Drop Column' and 'Add Column' options if you want to recreate the column with a new type.");
					System.out.println(msg);
					return status;

				} else {
					return QueryStatus.TYPE_CHANGE_FAILED;
				}
			} else {
				String msg = StyledMessage.Status.warning("'" + newColType + "' is invalid type, try valid one");
				System.out.println(msg);
				if (count == 2) {
					return QueryStatus.TYPE_CHANGE_SKIPPED;
				}
				count++;
				continue;
			}
		}

	}

	public QueryStatus modifyKeyConstraint() {

//		String[] colMeta = info.stream().filter(arr -> arr[0].equalsIgnoreCase(colName)).findFirst().orElse(null);
//		System.out.println(colMeta);
		Set<String> keys = Set.of("PRIMARY KEY", "UNIQUE KEY", "FOREIGN KEY");
		String msg = StyledMessage.Status.info("Your possible keys : " + keys);
		System.out.println(msg);
		String newKeyConstrain = InputManager.stringInput("Enter key for column '" + colName + "'").toUpperCase();
		if (!newKeyConstrain.endsWith(ColumnAction.KEY.name())) {
			newKeyConstrain += " " + ColumnAction.KEY.name();
		}

		if (!keys.contains(newKeyConstrain)) {
			msg = StyledMessage.Status.warning("Invalid key constraint entered");
			System.out.println(msg);
			return QueryStatus.KEY_MODIFY_FAILED;
		}

		if (newKeyConstrain.equalsIgnoreCase("PRIMARY KEY")) {
			return QueryStatus.PRIMARY_KEY_MODIFY_NOT_ALLOWED;
		} else if (newKeyConstrain.equalsIgnoreCase("FOREIGN KEY")) {
			msg = StyledMessage.Status.warning("Foreign key modifacition is not implementation yet");
			System.out.println(msg);
			return QueryStatus.KEY_MODIFY_FAILED;
		} else {
			String qry = String.format("SHOW INDEX FROM `%s` ", tableName);
			try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
					Statement st = con.createStatement()) {
				ResultSet rs = st.executeQuery(qry);
				boolean skipAdd = false;
				while (rs.next()) {
					if (rs.getString("Column_name").equalsIgnoreCase(colName) && rs.getInt("Non_unique") == 0) {
						msg = StyledMessage.Status.info("column already has unique constraint, want to remove it?");
						System.out.println(msg);
						char yesNo = InputManager.charInput("Press y/Y to remove or press any key  to ignore");
						if (Character.toLowerCase(yesNo) == 'y') {
							String indexName = rs.getString("Key_name");
							qry = String.format("ALTER TABLE `%s`.`%s` DROP INDEX `%s`", dbName, tableName, indexName);
							QueryStatus status = fireExecuteUpdate(qry);
							if (status == QueryStatus.SUCCESS) {
								return QueryStatus.KEY_DROP_SUCCESS;
							} else {
								return status;
							}

						} else {
							skipAdd = true;
							break;
						}
					}
				}
				if(skipAdd) {
					return QueryStatus.MODIFICATION_SKIPPED;
				}

			} catch (SQLException e) {
				msg = StyledMessage.Status.error(e.getMessage());
				System.out.println(msg + " lado");
				return QueryStatus.SQL_EXCEPTION;
			}
			qry = String.format("ALTER TABLE `%s`.`%s` ADD UNIQUE(`%s`)", dbName, tableName, colName);

			QueryStatus status = fireExecuteUpdate(qry);
			if (status == QueryStatus.SUCCESS) {
				return QueryStatus.KEY_MODIFY_SUCCESS;
			} else if (status == QueryStatus.SQL_EXCEPTION) {
				return status;
			} else {
				return QueryStatus.KEY_MODIFY_FAILED;
			}
		}

	}

	public QueryStatus toggleNullability() {
		return QueryStatus.TODO;
	}

	public QueryStatus updateExtraFlags() {
		return QueryStatus.TODO;
	}

	private QueryStatus fireExecuteUpdate(String sql) {
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			st.executeUpdate(sql);
			return QueryStatus.SUCCESS;

		} catch (SQLException e) {
			String msg = StyledMessage.Status.error(e.getMessage());
			System.out.println(msg);
			return QueryStatus.SQL_EXCEPTION;

		}
	}

	public String getColName() {

		return this.colName;
	}

}
