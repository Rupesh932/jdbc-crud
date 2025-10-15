package jdbc.basic.launch;

import java.util.List;
import java.util.Scanner;

import jdbc.basic.launch.Constants.QueryStatus;

public class InputManager {
	static Scanner scan = new Scanner(System.in);

	public static void closeResource() {
		scan.close();
	}

	public static String dbNameInput(String prompt) {
		List<String> existedDb = SchemaInspector.getCustomDatabases();

		String styledDb = StyledMessage.Status.info("Available databases: " + existedDb);
		System.out.println(styledDb);
		while (true) {
			String dbName = CrudHelper.validName(prompt);
			if (existedDb.contains(dbName)) {
				System.out.println(StyledMessage.Action.sparkle(Constants.QueryStatus.DB_EXISTED.getMessage(),
						Emoji.Status.SUCCESS));
				return dbName;
			} else {
				System.out.println(StyledMessage.Status
						.warning("Database '" + dbName + "' is not found. Do you want to create this database?"));
				char yesNo = charInput("Enter y/Y to create database or press any key to ignore");
				if (Character.toLowerCase(yesNo) == 'y') {
					QueryStatus status = DatabaseAdmin.createDatabase(dbName);
					if (status.equals(Constants.QueryStatus.DB_CREATED)) {
						System.out.println(StyledMessage.Action.sparkle(QueryStatus.DB_CREATED.getMessage(),
								Emoji.FileOps.DATABASE));
						return dbName;
					} else {
						System.out.println(
								StyledMessage.Status.failed(Constants.QueryStatus.DB_CREATION_FAILED.getMessage()));
						continue;
					}
				} else {
					System.out.println(StyledMessage.Status.info(QueryStatus.DB_SKIPPED.getMessage()));
					return QueryStatus.DB_SKIPPED.getCode();
				}
			}
		}

	}

	public static String tableNameInput(String dbName, String prompt) {
		List<String> tableList = SchemaInspector.showTables(dbName);
		if(!tableList.isEmpty()) {
			
			String styledTable = StyledMessage.Status.info("Available tables: " + tableList);
			System.out.println(styledTable);
		}
		return CrudHelper.validName(prompt);
	}

	public static int intInput(String prompt) {
		prompt = StyledMessage.Input.prompt(prompt);
		System.out.println(prompt);
		int intValue;
		while (true) {
			try {
				intValue = Integer.parseInt(scan.nextLine());
				break;
			} catch (NumberFormatException e) {
				prompt = StyledMessage.Status.warning("invalid input, enter integer value only.");
				System.out.println(prompt);
			}
		}
		System.out.println(Color.Reset.RESET);
		return intValue;
	}

	public static String stringInput(String prompt) {
		prompt = StyledMessage.Input.prompt(prompt);
		System.out.println(prompt);
		String value = scan.nextLine();
		System.out.println(Color.Reset.RESET);
		return value;

	}

	public static char charInput(String prompt) {
		prompt = StyledMessage.Input.prompt(prompt);
		System.out.println(prompt);
		String character = scan.nextLine();
		System.out.println(Color.Reset.RESET);
		return character.isEmpty() ? ' ' : character.charAt(0);
	}

	public static Double decimalInput(String prompt) {
		prompt = StyledMessage.Input.prompt(prompt);
		System.out.println(prompt);
		Double input;
		while (true) {
			try {
				input = Double.parseDouble(scan.nextLine().trim());
				break;
			} catch (NumberFormatException e) {
				System.out.println(StyledMessage.Status.warning("invalid input, enter integer value only."));
			}
		}
		System.out.println(Color.Reset.RESET);
		return input;
	}

}
