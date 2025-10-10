package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;

public class CrudManager {
	private Crud ci = new CrudImpl();
	CrudManagerHelper helper = new CrudManagerHelper();

	public String handleTableCreation() {
		String dbName = GetName.getDbName();
		if (ConnectionFactory.isDatabaseExist(dbName)) {
			String tableName = GetName.getTableName(dbName);
			if (!ConnectionFactory.isTableExists(dbName, tableName)) {
				return helper.createTableIfNotExist(dbName, tableName);
			} else {
				return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_EXISTED.getMessage());
			}
		}

		// prompts for user if database not exist.
		System.out.println(Color.RED + Emoji.CROSSMARK + "database '" + dbName
				+ "' is not found.do you want to create this database? " + Color.RESET);
		char yesNo = InputManager.charInput(" \tenter y/Y to create database or press any key to ignore.");
		if (yesNo == 'y' || yesNo == 'Y') {
			return helper.createTableIfDatabaseNotExist(dbName, dbName);
		} else {
			return MessageStyler.makeRed(Emoji.WARNING + Constants.OperationMessage.DB_SKIPPED.getMessage());
		}

	}

	public String handleInsertData() {
		String dbName = CrudHelper.validName("enter database name to insert data.");
		if (ConnectionFactory.isDatabaseExist(dbName)) {
			String tableName = CrudHelper.validName("enter table name to insert data.");
			if (ConnectionFactory.isTableExists(dbName, tableName)) {
				List<ColumnMeta> cols = ConnectionFactory.getInsertableColumns(dbName, tableName);
				Map<String, Object> userInput = new LinkedHashMap<>();
				Object colValue;
				String colType;
				for (ColumnMeta meta : cols) {
					System.out.println(Color.BG_GREEN + Color.WHITE + Emoji.NOTE + "column name: " + meta.colName()
							+ ", can hold: " + meta.intCode() + "(typed), is mandatory:" + meta.isMandatory()
							+ ", should unique :" + meta.isUnique() + Color.RESET);
					colType = CrudHelper.getColumnType(meta.intCode());
					if (meta.isMandatory()) {
						while (true) {
							colValue = CrudHelper.getColumnValue(colType, meta.colName());
							if (meta.isUnique() && colValue instanceof String s) {
								boolean isUnique = ConnectionFactory.isColumnValueUnique(dbName, tableName,
										meta.colName(), s);
								if (!isUnique) {
									System.out.println(MessageStyler.makeRed() + Emoji.ERROR + s
											+ " is already taken, please enter unique value for '" + meta.colName()
											+ "'");
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

				OperationStatus insertData = ci.insertData(dbName, tableName, userInput);
				if (insertData.equals(Constants.OperationStatus.SUCCESS)) {
					return MessageStyler
							.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DATA_INSERTED.getMessage());
				} else {
					return MessageStyler
							.makeRed(Emoji.ERROR + Constants.OperationMessage.DATA_INSERT_FAILED.getMessage());
				}
			} else {
				return Color.BG_RED + MessageStyler
						.makeYellow(Emoji.CROSSMARK + Constants.OperationMessage.TABLE_NOT_FOUND.getMessage());
			}
		} else {
			return Color.BG_RED
					+ MessageStyler.makeYellow(Emoji.CROSSMARK + Constants.OperationMessage.DB_NOT_FOUND.getMessage());

		}
	}

	public void handleReadData() {
		// TODO Auto-generated method stub

	}

	public void handleUpadateData() {
		// TODO Auto-generated method stub

	}

	public void handleDeleteData() {
		// TODO Auto-generated method stub

	}

}
