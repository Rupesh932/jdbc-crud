package jdbc.basic.launch;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CrudImpl implements Crud {
	private MenuManager manager;

	public CrudImpl(MenuManager manager) {
		this.manager = manager;
	}

	@Override
	public String createTable() {
		Map<String, String> colDef = new LinkedHashMap<>();
		colDef.put("sn", "INT PRIMARY KEY AUTO_INCREMENT");
		colDef.put("user_name", "VARCHAR(15) UNIQUE NOT NULL");
		colDef.put("full_name", "VARCHAR(50) NOT NULL");
		colDef.put("password", "VARCHAR(15) NOT NULL");
		colDef.put("address", "VARCHAR(50) NOT NULL");
		colDef.put("salary", "INT NOT NULL");
		colDef.put("file_name", "VARCHAR(200)");
		colDef.put("file_url", "TEXT");
		colDef.put("date", "DATE");
		colDef.put("message", "VARCHAR(200)");
		return ConnectionFactory.createTable("employee", "employee_record", colDef);

	}

	@Override
	public String createTable(String dbName) {
		String tableName = validName("enter table name:");
		int colCount = manager.intInput("enter max colume number:");
		Map<String, String> colNameAndConstrain = new LinkedHashMap<>();
		for (int i = 1; i <= colCount; i++) {
			String colName = validName("enter " + i + " column name of table.");
			String colConstrain = manager.stringInput("enter " + i + " column constrains.");
			colNameAndConstrain.put(colName, colConstrain);
		}
		return ConnectionFactory.createTable(dbName, tableName, colNameAndConstrain);
	}

	private String validName(String msg) {
		String regex = "[a-zA-Z_][a-zA-Z0-9_]*";
		Set<String> reserved = Set.of("select", "from", "table", "insert", "delete", "update", "drop", "create", "join", "where");
		while (true) {
			String name = manager.stringInput(msg).trim();
			if (name.matches(regex) && !(reserved.contains(name.toLowerCase()))) {
				return name;
			}
			System.out.println(
					"Invalid name. Use letters, digits, and underscores only. Reserved SQL keywords are not allowed.");
		}
	}

	@Override
	public String insertData(String dbName,String tableName,Model model) {

		if (ConnectionFactory.isTableExists(dbName,tableName)) {
			LinkedHashMap<String, Object> data = new LinkedHashMap<>();
			data.put("user_name", model.getUserName());
			data.put("full_name", model.getFullName());
			data.put("password", model.getPassword());
			data.put("address", model.getAddress());
			data.put("salary", model.getSalary());
			ConnectionFactory.insertDataUsingPS(dbName, tableName, data);
			return "data inserted successful";
		} else {
			return "table is not found, first create table  and try to insert data.";

		}

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
