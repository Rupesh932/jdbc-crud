package jdbc.basic.launch;

import java.util.Map;
import jdbc.basic.launch.Constants.OperationStatus;

public class CrudImpl implements Crud {

	@Override
	public OperationStatus createTable(String dbName, String tableName,Map<String,String> colMeta) {

		return ConnectionFactory.createTable(dbName, tableName, colMeta);
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
