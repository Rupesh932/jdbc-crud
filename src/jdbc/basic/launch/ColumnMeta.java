package jdbc.basic.launch;

//Represents metadata for a column: name, SQL type code, and mandatory(not null fields) flag
public record ColumnMeta(String colName,int intCode,boolean isMandatory,boolean isUnique) {

}
