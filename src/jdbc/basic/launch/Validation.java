package jdbc.basic.launch;

import java.util.Set;

import jdbc.basic.launch.Constants.IntConstant;

public class Validation {

	private static int MAX_LENGTH = 64;
	private static int MIN_LENGTH = 2;
	private final static Set<String> RESERVED = Set.of("select", "from", "table", "insert", "delete", "update", "drop",
			"create", "join", "where");
	private final static Set<String> allowedTypes = Set.of("INT", "TEXT", "DATE", "VARCHAR", "DECIMAL", "CHAR", "FLOAT",
			"BOOLEAN");

	public static String validateIdentifier(String msg, String label) {
		String regex = "[a-zA-Z_][a-zA-Z0-9_]*";
		while (true) {
			String name = InputManager.stringInput(msg).trim();

			if (name.matches(regex) && !(RESERVED.contains(name.toLowerCase())) && name.length() >= MIN_LENGTH
					&& name.length() <= MAX_LENGTH) {
				return name;
			}
			System.out.println(StyledMessage.Status.warning("Invalid " + label
					+ " name. Use letters, digits, and underscores only. Reserved SQL keywords are not allowed. Length must be between "
					+ MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

		}

	}

	public static String validateDbName(String msg) {
		return validateIdentifier(msg, "database");
	}

	public static String validateTableName(String msg) {
		return validateIdentifier(msg, "table");
	}

	public static String validateColumnName(String msg) {
		return validateIdentifier(msg, "column");
	}

	public static boolean isValidColumnType(String colType) {
		if (colType == null || colType.isEmpty()) {
			return false;
		}
		String upper = colType.toUpperCase();// let VARCHAR(100)
		String[] parts = upper.split("\\(");// split string based on '(' and stores each part to parts :["VARCHAR",
											// "100)"]
		String baseType = parts[0];// "VARCHAR"
		if (!allowedTypes.contains(baseType)) {
			return false;
		}
		if (upper.matches("VARCHAR\\(\\d+\\)") || upper.matches("DECIMAL\\(\\d+,\\d+\\)")
				|| upper.matches("CHAR\\(\\d+\\)")) {
			return true;
		}
		
		   return parts.length == IntConstant.ONE;
	}

}
