package jdbc.basic.launch;

import java.util.Map;

import jdbc.basic.launch.Constants.OperationStatus;

public interface Crud {
	OperationStatus createTable(String string);

	OperationStatus insertData(String dbName, String tableName, Map<String,Object>userInput);

	void readData();

	void upadateData();

	void deleteData();
}
