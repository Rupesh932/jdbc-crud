package jdbc.basic.launch;

import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;

public class CrudImpl implements Crud {

	@Override
	public OperationStatus createTable(String dbName, String tableName,Map<String,String> colMeta) {

		return DatabaseAdmin.createTable(dbName, tableName, colMeta);
	}

	@Override
	public OperationStatus insertData(String dbName, String tableName, Map<String, Object> userInput) {

		return DataInserter.insertDataUsingPS(dbName, tableName, userInput);
	}

	@Override
	public List<Map<String, Object>> readAllRows(String dbName, String tableName) {
		return QueryExecutor.readAllRows(dbName, tableName);
	}

	@Override
	public Map<String, Object> readOneRowByCredential(String dbName, String tableName, String userName,
			String password) {
		return QueryExecutor.readOneRowByCrenditial(dbName, tableName, userName, password);
	}

	@Override
	public Map<String, Object> readOneRowById(String dbName, String tableName,int id) {
		return QueryExecutor.readOneRowById(dbName, tableName, id);
	}

	@Override
	public void upadateData() {

	}

	@Override
	public void deleteData() {

	}

	

}
