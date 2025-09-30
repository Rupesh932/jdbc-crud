package jdbc.basic.launch;

import java.util.Scanner;

public class MenuManager {
	Scanner scan = new Scanner(System.in);

	public void closeResource() {
		scan.close();
	}

	private Crud ci;

	public MenuManager() {
		ci = new CrudImpl(this);
	}

	public void menu() {
		System.out.println("""
				======= MY SERVICES ========

				1. CREATE TABLE
				2. INSERT DATA
				3. READ DATA
				4. UPDATE DATA
				5. DELETE
				9. EXIT.

				""");
	}

	public void menuHandler() {
		int attempt = Constants.MIN_ATTEMPTS;
		String result = "";
		while (true) {

			if (attempt < Constants.MAX_ATTEMPTS) {
				menu();
				int choice = intInput("enter your choice : ");
				if (choice == Constants.EXIT) {
					System.out.println("Thanks for using, now exiting.");
					closeResource();
					return;
				}

				if (choice >= Constants.MIN_OPTION && choice <= Constants.MAX_OPTION) {
					attempt = Constants.MIN_ATTEMPTS;
					switch (choice) {
					case Constants.CREATE_TABLE:
						result = ci.createTable("employee");
						System.out.println(result);
						break;
					case Constants.INSERT_DATA:
						//result = ci.insertData(tableData());
						System.out.println(result);

						break;
					case Constants.READ_DATA:
						ci.readData();

						break;
					case Constants.UPDATE_DATA:
						ci.upadateData();

						break;
					case Constants.DELETE_DATA:
						ci.deleteData();

						break;
					default:

						break;

					}
				} else {
					System.out.println("invalid choice. enter (1-5) or 9 to exit.");
					attempt++;
				}
			} else {
				System.out.println("maximum invalid try limit exceeded.");
				closeResource();
				return;
			}
		}

	}

	public int intInput(String prompt) {
		System.out.println(prompt);
		while (true) {
			try {
				return Integer.parseInt(scan.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("invalid input, enter integer value only.");
			}
		}
	}

	public String stringInput(String prompt) {
		System.out.println(prompt);
		return scan.nextLine();

	}

	public char charInput(String prompt) {
		System.out.println(prompt);
		String character = scan.nextLine();
		return character.isEmpty() ? ' ' : character.charAt(0);
	}

	public Model tableData() {
		String userName = stringInput("enter user name: ");
		String fullName = stringInput("enter full name: ");
		String password = stringInput("enter password: ");
		String address = stringInput("enter address: ");
		int salary = intInput("enter salary: ");
		return new Model(userName, fullName, password, address, salary);
	}
}
