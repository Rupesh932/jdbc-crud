package jdbc.basic.launch;

import java.util.List;
import java.util.Map;

import jdbc.basic.launch.Constants.QueryStatus;

public interface Crud {
	QueryStatus createTable(String dbName, String tableNmae, Map<String, String> colMeta);

	QueryStatus insertData(String dbName, String tableName, Map<String, Object> userInput);

	List<Map<String, Object>> readAllRows(String dbName, String tableName);

	Map<String, Object> readOneRowByCredential(String dbName, String tableName, String userName, String password);

	Map<String, Object> readOneRowById(String dbName, String tableName, int id);
	
	QueryStatus dropTable(String dbName);
	
	QueryStatus dropDatabase();
	
	QueryStatus alterTable(String dbName,String tableName,QueryStatus status);

	QueryStatus  upadateData(String dbName,String tableName);

	QueryStatus deleteRow(String dbName,String tableName);
}
