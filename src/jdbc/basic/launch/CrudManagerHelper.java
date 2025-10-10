package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.Map;

import jdbc.basic.launch.Constants.OperationStatus;

public class CrudManagerHelper {
	private  Crud ci = new CrudImpl();
	Map<String, String> colMeta = new LinkedHashMap<>();
	
	public  String createTableIfNotExist(String dbName,String tableName) {
		
		colMeta = CrudHelper.prepareColumnMeta(tableName);
		OperationStatus tableResult = ci.createTable(dbName, tableName, colMeta);
		if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
			return MessageStyler
					.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_CREATED.getMessage());
		} else {
			return MessageStyler
					.makeRed(Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
		}
	}
	public  String createTableIfDatabaseNotExist(String dbName,String tableName) {
		OperationStatus dbResult = ConnectionFactory.createDataBase(dbName);
		if (dbResult.equals(Constants.OperationStatus.SUCCESS)) {
			tableName = GetName.getTableName(dbName);
			if (!ConnectionFactory.isTableExists(dbName, tableName)) {
				colMeta = CrudHelper.prepareColumnMeta(tableName);
				OperationStatus tableResult = ci.createTable(dbName, tableName, colMeta);
				if (tableResult.equals(Constants.OperationStatus.SUCCESS)) {
					return MessageStyler
							.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage() + ".\n\t"
									+ Emoji.SUCCESS + Constants.OperationMessage.TABLE_CREATED.getMessage());
				} else {
					return MessageStyler
							.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.DB_CREATED.getMessage()) + ".\n\t"
							+ MessageStyler.makeRed(
									Emoji.ERROR + Constants.OperationMessage.TABLE_CREATION_FAILED.getMessage());
				}
			} else {
				return MessageStyler
						.makeGreen(Emoji.SUCCESS + Constants.OperationMessage.TABLE_EXISTED.getMessage());
			}
		} else {
			return MessageStyler.makeRed(Emoji.ERROR + Constants.OperationMessage.DB_CREATION_FAILED.getMessage());
		}
	}

}
