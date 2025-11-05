package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdbc.basic.launch.Constants.CrudMode;
import jdbc.basic.launch.Constants.QueryStatus;
import jdbc.basic.launch.Constants.ReadMode;

public class CrudFlowHelper {
	private Crud ci = new CrudImpl();
	Map<String, String> colMeta = new LinkedHashMap<>();

	public String createTableIfNotExist(String dbName, String tableName) {

		colMeta = CrudHelper.prepareColumnMeta(tableName);
		QueryStatus tableResult = ci.createTable(dbName, tableName, colMeta);
		if (tableResult.equals(QueryStatus.TABLE_CREATED)) {
			return StyledMessage.Action.sparkle(QueryStatus.TABLE_CREATED.getMessage());
		} else {
			return StyledMessage.Status.failed(QueryStatus.TABLE_CREATION_FAILED.getMessage());
		}
	}

	public String createTableIfDatabaseNotExist(String dbName, String tableName) {
		QueryStatus dbResult = DatabaseAdmin.createDatabase(dbName);

		String styledDbMsg = StyledMessage.Action.sparkle(QueryStatus.DB_CREATED.getMessage(), Emoji.Action.THUMBS_UP);
		String styledTMsg = StyledMessage.Action.sparkle(QueryStatus.TABLE_CREATED.getMessage(),
				Emoji.Action.THUMBS_UP);

		if (dbResult.equals(QueryStatus.DB_CREATION_FAILED)) {
			styledDbMsg = StyledMessage.Action.sparkle(QueryStatus.DB_CREATION_FAILED.getMessage(),
					Emoji.Action.THUMBS_UP);
			return styledDbMsg;
		}

		colMeta = CrudHelper.prepareColumnMeta(tableName);
		QueryStatus tableResult = ci.createTable(dbName, tableName, colMeta);
		if (tableResult.equals(QueryStatus.TABLE_CREATION_FAILED)) {
			return StyledMessage.Status.failed(QueryStatus.TABLE_CREATION_FAILED.getMessage(),
					Emoji.Action.THUMBS_DOWN);
		}
		return styledDbMsg + ".\n\t" + styledTMsg;
	}

	public Map<String, Object> dataCollector(String dbName, String tableName, List<ColumnMeta> colMeta) {
		CrudMode mode = AdminUpdateHelper.mode;
		Object colValue;
		String colType;
		Map<String, Object> userInput = new LinkedHashMap<>();
		for (ColumnMeta meta : colMeta) {
			colType = CrudHelper.getColumnType(meta.intCode());
			System.out.println(StyledMessage.Input.preview("column name: " + meta.colName() + ", can hold: '" + colType
					+ "'(typed), is mandatory:" + meta.isMandatory() + ", is password field: " + meta.isPasswordField()
					+ ", should unique :" + meta.isUnique()));

			if (meta.isMandatory()) {
				while (true) {
					colValue = CrudHelper.getColumnValue(colType, meta.colName());
					if (meta.isUnique() && colValue instanceof String s) {
						boolean isUnique = SchemaInspector.isColumnValueUnique(dbName, tableName, meta.colName(), s);
						if (!isUnique) {
							System.out.println(StyledMessage.Status.error(
									s + " is already taken, please enter unique value for '" + meta.colName() + "'"));
							continue;
						}

					}
					if (meta.isPasswordField() && colValue instanceof String s) {
						colValue = PasswordHasher.hash(s);
					}
					userInput.put(meta.colName(), colValue);
					break;
				}
				
			} else {
				if(Constants.CrudMode.UPDATE.equals(mode)) {
					colValue = CrudHelper.getColumnValue(colType, meta.colName());
					 userInput.put(meta.colName(), colValue);
					 return userInput;
				}
				System.out.println(StyledMessage.Status.warning("Do you want to insert data to nullable field ?"));

				char yesNo = InputManager.charInput("Press y/Y to insert or press any key to ignore.");
				if (yesNo == 'y' || yesNo == 'Y') {
					colValue = CrudHelper.getColumnValue(colType, meta.colName());
					userInput.put(meta.colName(), colValue);
				} else {
					userInput.put(meta.colName(), CrudHelper.nullManager(colType));
				}
			}

		}
		return userInput;
	}

	public ReadMode getReadMode() {
		while (true) {
			char choice = InputManager.charInput("Read all (Press 'a') , or Read one row (Press 'r') ");
			if (Character.toLowerCase(choice) == 'a') {

				return Constants.ReadMode.ALL;
			} else if (Character.toLowerCase(choice) == 'r') {
				while (true) {
					choice = InputManager.charInput("Read by id (press 'i'), or Read by credentaials (Press 'c')");
					if (Character.toLowerCase(choice) == 'i') {

						return Constants.ReadMode.BY_ID;
					} else if (Character.toLowerCase(choice) == 'c') {
						return Constants.ReadMode.BY_CREDENTIALS;
					} else {
						System.out.println(StyledMessage.Status.warning("Invalid choice,try valid option as shown."));
					}
				}
			} else {
				System.out.println(StyledMessage.Status.warning("Invalid choice,try valid option as shown."));
			}

		}

	}

}
