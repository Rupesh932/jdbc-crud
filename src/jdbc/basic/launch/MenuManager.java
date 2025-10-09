package jdbc.basic.launch;

import java.sql.Types;
import java.util.Scanner;
import java.util.Set;

public class MenuManager {
	Scanner scan = new Scanner(System.in);

	public void closeResource() {
		scan.close();
	}

	private CrudManager cm;

	public MenuManager() {
		cm = new CrudManager(this);
	}

	public void menu(String color) {
		System.out.println(color);
		System.out.println("""
				======= MY SERVICES ========

				1. CREATE TABLE
				2. INSERT DATA
				3. READ DATA
				4. UPDATE DATA
				5. DELETE
				9. EXIT.

				""");
		System.out.println(Color.RESET);
	}

	public void menuHandler() {
		int attempt = Constants.Input.MIN_ATTEMPTS;
		String result = "";
		while (true) {

			if (attempt < Constants.Input.MAX_ATTEMPTS) {

				menu(MessageStyler.makePurple());

				int choice = intInput("enter your choice : ");
				if (choice == Constants.Menu.EXIT) {
					System.out.println(MessageStyler.makeRed(Emoji.EXIT + " now existing, thankx for using"));
					closeResource();
					return;
				}

				if (choice >= Constants.Input.MIN_OPTION && choice <= Constants.Input.MAX_OPTION) {
					attempt = Constants.Input.MIN_ATTEMPTS;
					switch (choice) {
					case Constants.Menu.CREATE_TABLE:
						result = cm.handleTableCreation();
						System.out.println(result);
						break;
					case Constants.Menu.INSERT_DATA:
						result = cm.handleInsertData();
						System.out.println(result);
						break;
					case Constants.Menu.READ_DATA:
						cm.handleReadData();

						break;
					case Constants.Menu.UPDATE_DATA:
						cm.handleUpadateData();

						break;
					case Constants.Menu.DELETE_DATA:
						cm.handleDeleteData();

						break;
					default:

						break;

					}
				} else {
					System.out.println(
							MessageStyler.makeRed(Emoji.WARNING + "invalid choice. enter (1-5) or 9 to exit."));
					attempt++;
				}
			} else {
				System.out.println(MessageStyler.makeRed(Emoji.ERROR + "maximum invalid try limit exceeded."));
				closeResource();
				return;
			}
		}

	}

//compatible with jdk 14 and +
	public String getColumnType(int typeCode) {
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

	public int intInput(String prompt) {
		System.out.print(MessageStyler.makeCyna());
		System.out.println(Emoji.INPUT + prompt + " ");
		int intValue;
		while (true) {
			try {
				intValue = Integer.parseInt(scan.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out
						.println(Color.RED + Emoji.WARNING + "invalid input, enter integer value only." + Color.RESET);
			}
		}
		System.out.println(Color.RESET);
		return intValue;
	}

	public String stringInput(String prompt) {
		System.out.print(MessageStyler.makeBlue());
		System.out.println(Emoji.INPUT + prompt);
		String value = scan.nextLine();
		System.out.println(Color.RESET);
		return value;

	}

	public char charInput(String prompt) {
		System.out.print(MessageStyler.makeBlue());
		System.out.println(Emoji.INPUT + prompt);
		String character = scan.nextLine();
		System.out.println(Color.RESET);
		return character.isEmpty() ? ' ' : character.charAt(0);
	}

	public Double decimalInput(String prompt) {
		System.out.print(MessageStyler.makeBlue() + Emoji.INPUT + prompt);
		Double input;
		while (true) {
			try {
				input = Double.parseDouble(scan.nextLine().trim());
				break;
			} catch (NumberFormatException e) {
				System.out
				.println(Color.RED + Emoji.WARNING + "invalid input, enter integer value only." + Color.RESET);
			}
		}
		System.out.println(Color.RESET);
		return input;
	}

//	public Model tableData() {
//		String userName = stringInput("enter user name: ");
//		String fullName = stringInput("enter full name: ");
//		String password = stringInput("enter password: ");
//		String address = stringInput("enter address: ");
//		int salary = intInput("enter salary: ");
//		return new Model(userName, fullName, password, address, salary);
//	}

	public String validName(String msg) {
		String regex = "[a-zA-Z_][a-zA-Z0-9_]*";
		Set<String> reserved = Set.of("select", "from", "table", "insert", "delete", "update", "drop", "create", "join",
				"where");
		while (true) {
			String name = stringInput(msg).trim();
			if (name.matches(regex) && !(reserved.contains(name.toLowerCase()))) {
				return name;
			}
			System.out.println(Color.RED + Emoji.WARNING
					+ "Invalid name. Use letters, digits, and underscores only. Reserved SQL keywords are not allowed."
					+ Color.RESET);
		}
	}

//	public static String getStatusMessage(int code, String msg) {
//		switch (code) {
//		case Constants.OperationStatus.SUCCESS.getMessage():
//			return "Operation Successful.";
//		case Constants.FAILED:
//			return "Operation Failed.";
//		case Constants.ERROR:
//			return "Exception Or Error ";
//		default:
//			return "Unknown status";
//		}
//	}
}
