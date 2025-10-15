package jdbc.basic.launch;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CrudHelper {
	// compatible with jdk 14 and +
	public static String getColumnType(int typeCode) {
		return switch (typeCode) {
		case Types.INTEGER, Types.BIGINT -> Constants.ColumnType.INTEGER;

		case Types.FLOAT, Types.DOUBLE, Types.DECIMAL -> Constants.ColumnType.NUMERIC;// for fractional input.

		case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.NVARCHAR, Types.CLOB -> Constants.ColumnType.STRING;

		case Types.DATE -> Constants.ColumnType.DATE;

		case Types.TIMESTAMP -> Constants.ColumnType.TIMESTAMP;

		case Types.BOOLEAN -> Constants.ColumnType.BOOLEAN;

		default -> Constants.ColumnType.UNKNOWN;

		};
	}

	public static String validName(String msg) {
		String regex = "[a-zA-Z_][a-zA-Z0-9_]*";
		Set<String> reserved = Set.of("select", "from", "table", "insert", "delete", "update", "drop", "create", "join",
				"where");
		while (true) {
			String name = InputManager.stringInput(msg).trim();
			if (name.matches(regex) && !(reserved.contains(name.toLowerCase()))) {
				return name;
			}
			System.out.println(StyledMessage.Status.warning(
					"Invalid name. Use letters, digits, and underscores only. Reserved SQL keywords are not allowed."));

		}
	}

	public static Object getColumnValue(String colType, String colName) {

		return switch (colType) {
		case Constants.ColumnType.INTEGER -> InputManager.intInput("enter integer value for '" + colName + "' field.");
		case Constants.ColumnType.STRING -> InputManager.stringInput("enter string value for '" + colName + "' field");
		case Constants.ColumnType.BOOLEAN -> InputManager.charInput("enter y/n value for '" + colName + "' field");
		case Constants.ColumnType.NUMERIC ->
			InputManager.decimalInput("enter farctional value for '" + colName + "' field");
		case Constants.ColumnType.DATE -> LocalDate.now();
		case Constants.ColumnType.TIMESTAMP -> Timestamp.valueOf(LocalDateTime.now());
		default -> null;

		};

	}

	public static Object nullManager(String colType) {
		return switch (colType) {
		case Constants.ColumnType.STRING -> Constants.DefaultValue.DEFAULT_STRING;
		case Constants.ColumnType.INTEGER -> Constants.DefaultValue.DEFAULT_INTEGER;
		case Constants.ColumnType.UNKNOWN -> Constants.DefaultValue.UNKNOWN;
		case Constants.ColumnType.NUMERIC -> Constants.DefaultValue.DEFAULT_DOUBLE;
		case Constants.ColumnType.DATE -> Constants.DefaultValue.DEFAULT_DATE;
		case Constants.ColumnType.TIMESTAMP -> Constants.DefaultValue.DEFAULT_TIMESTAMP;
		case Constants.ColumnType.BOOLEAN -> Constants.DefaultValue.DEFAULT_BOOLEAN;
		default -> null;

		};
	}

	public static Map<String, String> prepareColumnMeta(String tableName) {

		int colCount = InputManager.intInput("Enter the number of columns for table '"+tableName+"'");
		Map<String, String> colNameAndConstrain = new LinkedHashMap<>();
		for (int i = 1; i <= colCount; i++) {
			String colName = validName("Enter the  name of column " + i );
			String colConstrain = InputManager
					.stringInput("Enter constraints for column '" + colName + "' (e.g. VARCHAR(20) NOT NULL UNIQUE)");
			colNameAndConstrain.put(colName, colConstrain);
		}
		return colNameAndConstrain;

	}
}
