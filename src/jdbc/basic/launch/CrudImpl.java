package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;

public class CrudImpl implements Crud {
	private MenuManager manager;

	public CrudImpl(MenuManager manager) {
		this.manager = manager;
	}

	@Override
	public OperationStatus createTable(String dbName) {
		String tableName = manager.validName("enter table name:");
		int colCount = manager.intInput("enter max colume number:");
		Map<String, String> colNameAndConstrain = new LinkedHashMap<>();
		for (int i = 1; i <= colCount; i++) {
			String colName = manager.validName("enter " + i + " column name of table.");
			String colConstrain = manager.stringInput("enter " + i + " column constrains.");
			colNameAndConstrain.put(colName, colConstrain);
		}
		return ConnectionFactory.createTable(dbName, tableName, colNameAndConstrain);
	}

	
	@Override
	public OperationStatus insertData(String dbName, String tableName, Map<String, Object> userInput) {
		
		return ConnectionFactory.insertDataUsingPS(dbName, tableName, userInput);
	}
	@Override
	public void readData() {

	}

	@Override
	public void upadateData() {

	}

	@Override
	public void deleteData() {

	}

}
