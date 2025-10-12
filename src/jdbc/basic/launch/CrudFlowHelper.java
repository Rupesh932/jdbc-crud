package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdbc.basic.launch.Constants.OperationStatus;
import jdbc.basic.launch.Constants.ReadMode;

public class CrudFlowHelper {
	private Crud ci = new CrudImpl();
	Map<String, String> colMeta = new LinkedHashMap<>();

	public String createTableIfNotExist(String dbName, String tableName) {

		colMeta = CrudHelper.prepareColumnMeta(tableName);
		OperationStatus tableResult = ci.createTable(dbName, tableName, colMeta);
		if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
			return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_CREATED.getMessage());
		} else {
			return MessageStyler.makeRed(Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
		}
	}

	public String createTableIfDatabaseNotExist(String dbName, String tableName) {
		OperationStatus dbResult = DatabaseAdmin.createDataBase(dbName);
		if (dbResult.equals(Constants.OperationStatus.SUCCESS)) {
			tableName = GetName.getTableName(dbName);
			if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
				colMeta = CrudHelper.prepareColumnMeta(tableName);
				OperationStatus tableResult = ci.createTable(dbName, tableName, colMeta);
				if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
					return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage()
							+ ".\n\t" + Emoji.SUCCESS + Constants.OperationMessage.TABLE_CREATED.getMessage());
				} else {
					return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage())
							+ ".\n\t" + MessageStyler.makeRed(
									Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
				}
			} else {
				return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_EXISTED.getMessage());
			}
		} else {
			return MessageStyler.makeRed(Emoji.ERROR + Constants.OperationMessage.DB_CREATION_FAILED.getMessage());
		}
	}

	public Map<String, Object> dataCollector(String dbName, String tableName, List<ColumnMeta> colMeta) {
		Object colValue;
		String colType;
		Map<String, Object> userInput = new LinkedHashMap<>();
		for (ColumnMeta meta : colMeta) {
			System.out.println(Color.BG_GREEN + Color.WHITE + Emoji.NOTE + "column name: " + meta.colName()
					+ ", can hold: " + meta.intCode() + "(typed), is mandatory:" + meta.isMandatory()
					+ ", should unique :" + meta.isUnique() + Color.RESET);
			colType = CrudHelper.getColumnType(meta.intCode());
			if (meta.isMandatory()) {
				while (true) {
					colValue = CrudHelper.getColumnValue(colType, meta.colName());
					if (meta.isUnique() && colValue instanceof String s) {
						boolean isUnique = SchemaInspector.isColumnValueUnique(dbName, tableName, meta.colName(), s);
						if (!isUnique) {
							System.out.println(MessageStyler.makeRed() + Emoji.ERROR + s
									+ " is already taken, please enter unique value for '" + meta.colName() + "'");
							continue;
						}
					}
					userInput.put(meta.colName(), colValue);
					break;
				}
			} else {
				System.out.println(Color.BRIGHT_RED + Color.YELLOW + Emoji.WARNING
						+ "Do you want to insert data to nullable field ?" + Color.RESET);
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
		char choice = InputManager.charInput("Read all (Press 'a') , or Read one row (Press 'r') ");
		if(Character.toLowerCase(choice) == 'a') 
			return Constants.ReadMode.ALL;
		
		
		choice = InputManager.charInput("Read by id (press 'i'), or Read by credentaials (Press 'c')");
		if(Character.toLowerCase(choice) == 'i')
			return Constants.ReadMode.BY_ID;
		else return Constants.ReadMode.BY_CREDENTIALS;
		
	}

}
