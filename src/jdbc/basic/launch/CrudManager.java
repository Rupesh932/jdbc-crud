package jdbc.basic.launch;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;

public class CrudManager {
	private Crud ci;
	MenuManager manager;

	public CrudManager(MenuManager manager) {
		this.manager = manager;
		ci = new CrudImpl(manager);
	}

	public String handleTableCreation() {
		String dbName = manager.validName("enter database name to create table.");
		if (ConnectionFactory.isDatabaseExist(dbName)) {
			OperationStatus tableResult = ci.createTable(dbName);
			if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
				return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_CREATED.getMessage());
			} else {
				return MessageStyler
						.makeRed(Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
			}
		}

		// prompts for user if database not exist.
		System.out.println(Color.RED + Emoji.CROSSMARK + "database " + dbName
				+ " is not found.do you want to create database? " + Color.RESET);
		char yesNo = manager.charInput(" \tenter y/Y to create database or press any key to ignore.");
		if (yesNo == 'y' || yesNo == 'Y') {
			OperationStatus dbResult = ConnectionFactory.createDataBase(dbName);
			if (dbResult.equals(Constants.OperationStatus.SUCCESS)) {
				OperationStatus tableResult = ci.createTable(dbName);
				if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
					return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage()
							+ ".\n\t" + Constants.OperationMessage.TABLE_CREATED.getMessage());
				} else {
					return MessageStyler.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage())
							+ ".\n\t" + MessageStyler.makeRed(
									Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
				}
			} else {
				return MessageStyler.makeRed(Emoji.ERROR + Constants.OperationMessage.DB_CREATION_FAILED.getMessage());
			}
		} else {
			return MessageStyler.makeRed(Emoji.WARNING + Constants.OperationMessage.DB_SKIPPED.getMessage());
		}

	}

	public String handleInsertData() {
		String dbName = manager.validName("enter database name to insert data.");
		if (ConnectionFactory.isDatabaseExist(dbName)) {
			String tableName = manager.validName("enter table name to insert data.");
			if (ConnectionFactory.isTableExists(dbName, tableName)) {
				List<ColumnMeta> cols = ConnectionFactory.getInsertableColumns(dbName, tableName);
				Map<String, Object> userInput = new LinkedHashMap<>();
				Object colValue;
				String colType;
				for (ColumnMeta meta : cols) {
					System.out.println(Color.BG_GREEN + Color.WHITE + Emoji.NOTE + "column name: " + meta.colName()
							+ ", can hold: " + meta.intCode() + "(typed), is mandatory:" + meta.isMandatory()
							+ ", should unique :" + meta.isUnique() + Color.RESET);
					colType = manager.getColumnType(meta.intCode());
					if (meta.isMandatory()) {

						while (true) {
							colValue = getColumnValue(colType, meta.colName());
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
						char yesNo = manager.charInput("Press y/Y to insert or press any key to ignore.");
						if (yesNo == 'y' || yesNo == 'Y') {
							colValue = getColumnValue(colType, meta.colName());
							userInput.put(meta.colName(), colValue);
						} else {
							userInput.put(meta.colName(), nullManager(colType));
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

	private Object getColumnValue(String colType, String colName) {

		return switch (colType) {
		case Constants.ColumnType.INTEGER -> manager.intInput("enter integer value for '" + colName + "' field.");
		case Constants.ColumnType.STRING -> manager.stringInput("enter string value for '" + colName + "' field");
		case Constants.ColumnType.BOOLEAN -> manager.charInput("enter y/n value for '" + colName + "' field");
		case Constants.ColumnType.NUMERIC -> manager.decimalInput("enter farctional value for '" + colName + "' field");
		case Constants.ColumnType.DATE -> LocalDate.now();
		case Constants.ColumnType.TIMESTAMP -> Timestamp.valueOf(LocalDateTime.now());
		default -> null;

		};

	}

	public Object nullManager(String colType) {
		return switch (colType) {
		case Constants.ColumnType.STRING -> Constants.DefaultValue.DEFAULT_STRING;
		case Constants.ColumnType.INTEGER -> Constants.DefaultValue.DEFAULT_INTEGER;
		case Constants.ColumnType.UNKNOWN -> Constants.DefaultValue.UNKNOWN;
		case Constants.ColumnType.NUMERIC -> Constants.DefaultValue.DEFAULT_DOUBLE;
		case Constants.ColumnType.DATE -> Constants.DefaultValue.DEFAULT_DATE;
		case Constants.ColumnType.TIMESTAMP -> Constants.DefaultValue.DEFAULT_TIMESTAMP;
		case Constants.ColumnType.BOOLEAN -> Constants.DefaultValue.DEFAULT_BOOLEAN;
		default -> null;

		};
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
