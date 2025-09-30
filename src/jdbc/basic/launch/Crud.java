package jdbc.basic.launch;

public interface Crud {
	String createTable(String string);
	String createTable();

	String insertData(String dbName,String tableName,Model model);

	void readData();

	void upadateData();

	void deleteData();
}
