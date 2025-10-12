package jdbc.basic.launch;

import java.util.List;
import java.util.Map;

import jdbc.basic.launch.Constants.OperationStatus;

public interface Crud {
	OperationStatus createTable(String dbName, String tableNmae, Map<String, String> colMeta);

	OperationStatus insertData(String dbName, String tableName, Map<String, Object> userInput);

	List<Map<String,Object>> readAllRows(String dbName, String tableName);

	Map<String,Object> readOneRowByCredential(String dbName, String tableName, String userName, String password);

	Map<String,Object> readOneRowById(String dbName, String tableName,int id);

	void upadateData();

	void deleteData();
}
