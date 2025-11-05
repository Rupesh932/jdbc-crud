package jdbc.basic.launch;

//Represents metadata for a column
public record ColumnMeta(String colName,int intCode,boolean isMandatory,boolean isUnique,boolean isPasswordField) {

}
