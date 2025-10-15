package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.QueryStatus;
import jdbc.basic.launch.Constants.ReadMode;

public class CrudManager {
	private Crud ci = new CrudImpl();
	CrudFlowHelper helper = new CrudFlowHelper();

	public String handleTableCreation() {
		String dbName = InputManager.dbNameInput("Enter database name to create table ");
		if (dbName.equals(QueryStatus.DB_SKIPPED.getCode())) {
			return StyledMessage.Action.fixed(QueryStatus.TABLE_SKIPPED.getMessage());
		}

		String tableName = InputManager.tableNameInput(dbName, "Enter table name to create table");
		if (!DbConnectionManager.isDatabaseExist(dbName)) {
			return helper.createTableIfDatabaseNotExist(dbName, tableName);
		}

		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return helper.createTableIfNotExist(dbName, tableName);
		} else {
			return StyledMessage.Status.success(QueryStatus.TABLE_EXISTED.getMessage(), Emoji.Action.OK_HAND);
		}
	}

	public String handleInsertData() {

		String dbName = InputManager.dbNameInput("Enter database name to insert data.");
		if (!DbConnectionManager.isDatabaseExist(dbName)) {
			return StyledMessage.Status.warning(QueryStatus.DB_NOT_FOUND.getMessage(), Emoji.Status.WARNING_ALT);
		}

		String tableName = InputManager.tableNameInput(dbName, "Enter table name to insert data.");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return StyledMessage.Status.warning(QueryStatus.TABLE_NOT_FOUND.getMessage());
		}

		List<ColumnMeta> cols = SchemaInspector.getInsertableColumns(dbName, tableName);
		Map<String, Object> userInput = new LinkedHashMap<>();
		userInput = helper.dataCollector(dbName, tableName, cols);
		QueryStatus insertData = ci.insertData(dbName, tableName, userInput);
		if (insertData.equals(QueryStatus.SUCCESS)) {
			return StyledMessage.Action.sparkle(QueryStatus.DATA_INSERTED.getMessage(), Emoji.Action.THUMBS_UP);
		} else {
			return StyledMessage.Status.failed(QueryStatus.DATA_INSERT_FAILED.getMessage(), Emoji.Action.THUMBS_DOWN);
		}

	}

	public String handleReadData() {
		String dbName = InputManager.dbNameInput("Enter database name to read table.");
		if (!DbConnectionManager.isDatabaseExist(dbName)) {
			return StyledMessage.Status.failed(QueryStatus.DB_NOT_FOUND.getMessage());
		}
		String tableName = InputManager.tableNameInput(dbName, "Enter table name to read data from database ");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return StyledMessage.Status.failed(QueryStatus.TABLE_NOT_FOUND.getMessage());
		}

		ReadMode mode = helper.getReadMode();
		switch (mode) {
		case ALL -> {
			System.out.println(StyledMessage.Status.info(" Reading all rows: ", Emoji.UI.GLASSES));
			List<Map<String, Object>> rows = ci.readAllRows(dbName, tableName);
			return DisplayHelper.showData(rows);
		}
		case BY_ID -> {
			System.out.println(StyledMessage.Status.info(" Reading based on id: ", Emoji.UI.GLASSES));
			int id = InputManager.intInput(" Enter id/sn to read data from '" + dbName + "'.'" + tableName + "'");
			Map<String, Object> row = ci.readOneRowById(dbName, tableName, id);
			return DisplayHelper.showData(row);
		}

		case BY_CREDENTIALS -> {
			System.out.println(
					StyledMessage.Status.info(" Reading based on 'userName' and 'password' :", Emoji.UI.GLASSES));
			String userName = InputManager
					.stringInput(" Enter userName to read data from '" + dbName + "'.'" + tableName + "'");
			String password = InputManager.stringInput("Enter password :");
			Map<String, Object> row = ci.readOneRowByCredential(dbName, tableName, userName, password);
			return DisplayHelper.showData(row);
		}
		default -> {
			return StyledMessage.Action.sparkle("UNKNOWN CASE", Emoji.Action.BUG);
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
