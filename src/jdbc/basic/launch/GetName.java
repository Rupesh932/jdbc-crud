package jdbc.basic.launch;

public class GetName {
	public static String getDbName() {
		return CrudHelper.validName("Enter database name to create table.");
	}
	public static String getDbName(String prompt) {
		return CrudHelper.validName(prompt);
	}
	public static String getTableName(String dbName) {
		return CrudHelper.validName("Enter table name to create table into database ."+dbName);
	}
	public static String getTableName(String dbName,String prompt) {
		return CrudHelper.validName(prompt+": "+dbName);
	}


}
