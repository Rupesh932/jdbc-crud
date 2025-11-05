package jdbc.basic.launch;

import java.util.List;
import java.util.Map;
import jdbc.basic.launch.Constants.QueryStatus;

public class CrudImpl implements Crud {

	@Override
	public QueryStatus createTable(String dbName, String tableName,Map<String,String> colMeta) {

		return DatabaseAdmin.createTable(dbName, tableName, colMeta);
	}

	@Override
	public QueryStatus insertData(String dbName, String tableName, Map<String, Object> userInput) {

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
	public QueryStatus upadateData(String dbName,String tableName) {
		return DatabaseAdmin.updateTable(dbName, tableName);
				
	}

	@Override
	public QueryStatus deleteRow(String dbName, String tableName) {
		return DatabaseAdmin.deleteRow(dbName,tableName);
	}

	@Override
	public QueryStatus dropTable(String dbName) {
		
		return DatabaseAdmin.dropTable(dbName);
	}

	@Override
	public QueryStatus dropDatabase() {
		
		return null;
	}

	@Override
	public QueryStatus alterTable(String dbName, String tableName,QueryStatus status) {
		
		return DatabaseAdmin.alterTable(dbName, tableName,status);
		
	}

	

}
