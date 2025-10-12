package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;
import jdbc.basic.launch.Constants.ReadMode;

public class CrudManager {
	private Crud ci = new CrudImpl();
	CrudFlowHelper helper = new CrudFlowHelper();

	public String handleTableCreation() {
		String dbName = GetName.getDbName();
		if (DbConnectionManager.isDatabaseExist(dbName)) {
			String tableName = GetName.getTableName(dbName);
			if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
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
		String dbName = GetName.getDbName("enter database name to insert data.");
		if (DbConnectionManager.isDatabaseExist(dbName)) {
			String tableName = GetName.getTableName(dbName, "enter table name to insert data.");
			if (DatabaseAdmin.isTableExists(dbName, tableName)) {
				List<ColumnMeta> cols = SchemaInspector.getInsertableColumns(dbName, tableName);
				Map<String, Object> userInput = new LinkedHashMap<>();
				userInput = helper.dataCollector(dbName, tableName, cols);
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

	public String handleReadData() {
		String dbName = GetName.getDbName("Enter database name to read table.");

		if (!DbConnectionManager.isDatabaseExist(dbName)) {
			return MessageStyler.makeRed() + Emoji.CROSSMARK + Constants.OperationMessage.DB_NOT_FOUND.getMessage()
					+ Color.RESET;
		}
		String tableName = GetName.getTableName(dbName, "Enter table name to read data from database ");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return MessageStyler.makeRed() + Emoji.CROSSMARK + Constants.OperationMessage.TABLE_NOT_FOUND.getMessage()
					+ Color.RESET;
		}

		ReadMode mode = helper.getReadMode();
		switch (mode) {
		case ALL -> {
			System.out.println(MessageStyler.makePurple(Emoji.INFO + "Reading all rows:"));
			List<Map<String, Object>> rows = ci.readAllRows(dbName, tableName);
			return DisplayHelper.showData(rows);
		}
		case BY_ID -> {
			System.out.println(MessageStyler.makePurple(Emoji.CLOCK + "Reading based on id."));
			int id = InputManager.intInput(" Enter id/sn to read data from '" + dbName + "'.'" + tableName + "'");
			Map<String, Object> row = ci.readOneRowById(dbName, tableName, id);
			return DisplayHelper.printMap(row);
		}

		case BY_CREDENTIALS -> {
			System.out.println(MessageStyler.makePurple(Emoji.CLOCK + "Reading based on 'userName' and 'password'"));
			String userName = InputManager
					.stringInput(" Enter userName to read data from '" + dbName + "'.'" + tableName + "'");
			String password = InputManager.stringInput("Enter password :");
			Map<String, Object> row = ci.readOneRowByCredential(dbName, tableName, userName, password);
			return DisplayHelper.printMap(row);
		}
		default -> {
			return MessageStyler.makeRed() + Emoji.ERROR + "UNKNOWN CASE" + Color.RESET;
		}
		}

	}

	public void handleUpadateData() {
		// TODO Auto-generated method stub

	}

	public void handleDeleteData() {
		// TODO Auto-generated method stub

	}

}
