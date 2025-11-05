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
		if (QueryStatus.TABLE_SKIPPED.name().equals(tableName)) {
			return QueryStatus.TABLE_SKIPPED.getMessage();
		}
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

			int count = 3;
			while (count > 0) {
				if (!CredentialValidator.userExist(dbName, tableName, userName)) {
					count--;
					if (count == 0) {
						return StyledMessage.Status.error("Maximum invlid attempts reached, Operation terminated");
					}
					System.out.println(StyledMessage.Status
							.error(QueryStatus.USERNAME_NOT_MATCHED.getMessage() + ",  " + count + " try left"));
					userName = InputManager.stringInput("enter valid user name ");

				} else {

					String password = InputManager.stringInput("Enter password ");
					Map<String, Object> row = ci.readOneRowByCredential(dbName, tableName, userName, password);
					return DisplayHelper.showData(row);
				}
			}
		}

		default -> {
			return StyledMessage.Action.sparkle("UNKNOWN CASE", Emoji.Action.BUG);
		}

		}
		return StyledMessage.Status.error(QueryStatus.USER_NOT_FOUND.getMessage());
	}

	public String handleUpadateData() {
		String dbName = InputManager.dbNameInput("Enter database name to update table ");
		if (!DbConnectionManager.isDatabaseExist(dbName)) {
			return StyledMessage.Status.failed(QueryStatus.DB_NOT_FOUND.getMessage());
		}
		String tableName = InputManager.tableNameInput(dbName, "Enter table name to update row/s ");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return StyledMessage.Status.failed(QueryStatus.TABLE_NOT_FOUND.getMessage());
		}

		QueryStatus status = ci.upadateData(dbName, tableName);
		//System.out.println("status: "+ status);
		return switch (status) {
		case NONE_FIELD_CHOOSEN, PRIMARY_KEY_NOT_FOUND, SQL_EXCEPTION ->
			StyledMessage.Status.warning(QueryStatus.UPDATE_SUSPENDED.getMessage());

		case DATA_UPDATED -> StyledMessage.Status.success(QueryStatus.DATA_UPDATED.getMessage());
		case DATA_UPDATE_FAILED -> StyledMessage.Status.failed(QueryStatus.DATA_UPDATE_FAILED.getMessage());
		default -> StyledMessage.Status.warning(QueryStatus.FAILED.getMessage());

		};
	}

	public String handleDropTable() {
		String dbName = InputManager.dbNameInput("Enter database name to drop table");
		return ci.dropTable(dbName).getMessage();
	}

	public String handleAlterTable(QueryStatus status) {
		String dbName = InputManager.dbNameInput("Enter database name to alter table");
		if (dbName.equals(QueryStatus.DB_SKIPPED.name())) {
			return StyledMessage.Status.error(QueryStatus.DB_NOT_FOUND.getMessage());
		}

		String tableName = InputManager.tableNameInput(dbName, "Enter table name to alter it");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return StyledMessage.Status.error(QueryStatus.TABLE_NOT_FOUND.getMessage());
		}
		return ci.alterTable(dbName, tableName, status).getMessage();

	}

	public String handleDeleteData() {
		// DELETE FROM table_name WHERE condition;
		String dbName = InputManager.dbNameInput("Enter database name to delete data(row) from its table");
		if (dbName.equals(QueryStatus.DB_SKIPPED.name())) {
			return StyledMessage.Status.error(QueryStatus.DB_NOT_FOUND.getMessage());
		}

		String tableName = InputManager.tableNameInput(dbName, "Enter table name to remove/delete its row");
		if (!DatabaseAdmin.isTableExists(dbName, tableName)) {
			return StyledMessage.Status.error(QueryStatus.TABLE_NOT_FOUND.getMessage());
		}

		return ci.deleteRow(dbName, tableName).getMessage();
	}

}
