package jdbc.basic.launch;

public class MenuManager {

	private CrudManager cm = new CrudManager();;

	public void menu() {
		System.out.println();
		String menuText = StyledMessage.Banner.menu("  MY SERVICES     ", """
				1. CREATE TABLE
				2. INSERT DATA
				3. READ DATA
				4. UPDATE DATA
				5. DELETE
				9. EXIT.
				""");
		System.out.println(menuText);
	}

	public void menuHandler() {
		int attempt = Constants.Input.MIN_ATTEMPTS;
		String result = "";
		while (true) {
			if (attempt < Constants.Input.MAX_ATTEMPTS) {
				menu();
				int choice = InputManager.intInput("enter your choice  ");
				if (choice == Constants.Menu.EXIT) {
					System.out.println(StyledMessage.System.exit(" now existing, thankx for using"));
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
						System.out.println(StyledMessage.Action.sparkle("Process Reading: ", Emoji.UI.GLASSES));
						result = cm.handleReadData();
						System.out.println(result);
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
					System.out.println(StyledMessage.Status.warning("invalid choice. enter (1-5) or 9 to exit."));
					attempt++;
				}
			} else {

				System.out.println(StyledMessage.Status.error("maximum invalid try limit exceeded."));
				return;
			}
		}

	}

}
