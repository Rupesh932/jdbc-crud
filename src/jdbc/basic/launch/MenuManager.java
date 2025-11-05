package jdbc.basic.launch;

import jdbc.basic.launch.Constants.Input;
import jdbc.basic.launch.Constants.Menu;
import jdbc.basic.launch.Constants.QueryStatus;
import jdbc.basic.launch.Constants.SubMenu;

public class MenuManager {

	private static CrudManager cm = new CrudManager();

	public void menuHandler() {
		int attempt = Constants.Input.MIN_ATTEMPTS;
		String result = "";
		while (true) {
			if (attempt < Constants.Input.MAX_ATTEMPTS) {
				ServiceMenu.mainMenu();
				int choice = InputManager.intInput("enter your choice  ");
				if (choice == Menu.EXIT) {
					System.out.println(StyledMessage.System.exit("Exiting... Thanks for using!"));
					return;
				}
				if (choice >= Input.MIN_OPTION && choice <= Input.MAX_OPTION) {
					attempt = Input.MIN_ATTEMPTS;
					switch (choice) {
					case Menu.CREATE_TABLE:
						System.out.println(
								StyledMessage.Action.sparkle("Table creation in progress...", Emoji.Build.INSTALL));
						result = cm.handleTableCreation();
						System.out.println(result);
						break;
					case Menu.INSERT_DATA:
						System.out.println(
								StyledMessage.Action.sparkle("Inserting data into table...", Emoji.Status.THINKING));
						result = cm.handleInsertData();
						System.out.println(result);
						break;
					case Menu.READ_DATA:
						System.out.println(StyledMessage.Action.sparkle("Reading rows...", Emoji.UI.OPEN_BOOK));
						result = cm.handleReadData();
						System.out.println(result);
						break;
					case Menu.UPDATE_DATA:
						System.out.println(StyledMessage.Action.sparkle("Updating rows...", Emoji.Build.UPDATE));
						result = cm.handleUpadateData();
						System.out.println(result);
						break;
					case Menu.DELETE_DATA:
						System.out.println(StyledMessage.Action.sparkle("Deleting rows...", Emoji.FileOps.TRASH));
						result =cm.handleDeleteData();
						System.out.println(StyledMessage.Status.info(result));
						break;
					case Menu.DROP_TABLE:
						System.out.println(StyledMessage.Action.sparkle("Drop table is processing...", Emoji.FileOps.TRASH));
						result = cm.handleDropTable();
						System.out.println(StyledMessage.Status.info(result));
						break;
					case Menu.DROP_DATABASE:
						System.out.println(StyledMessage.Action.sparkle("Drop table is processing...", Emoji.FileOps.TRASH));
						result = DatabaseAdmin.dropDatabase().getMessage();
						System.out.println(StyledMessage.Status.info(result));
						break;
					case Menu.ALTER_TABLE:
						ServiceMenu.alterTableSubMenu();
						subMenu();
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

	public static void subMenu() {
		while (true) {
			String result = " ";
			int choice = InputManager.intInput("Enter your choice");
			if (choice == SubMenu.BACK) {
				System.out.println(StyledMessage.Action.fixed("Returning service menu"));
				return;
			}
			switch (choice) {
			case SubMenu.ADD_COLUMN:
				System.out.println(StyledMessage.Action.sparkle("Alter table : 'add column' processing...", Emoji.FileOps.NOTE));
				result = cm.handleAlterTable(QueryStatus.ADD_COLUMN);
				System.out.println(StyledMessage.Status.info(result));
				break;
			case SubMenu.DROP_COLUMN:
				System.out.println(StyledMessage.Action.sparkle("Alter table : 'drop column' processing...", Emoji.FileOps.NOTE));
				result = cm.handleAlterTable(QueryStatus.DROP_COLUMN);
				System.out.println(StyledMessage.Status.info(result));
				break;
			case SubMenu.MODIFY_COLUMN:
				System.out.println(StyledMessage.Action.sparkle("Alter table : 'modify column' processing...", Emoji.FileOps.NOTE));
				result = cm.handleAlterTable(QueryStatus.MODIFY_COLUMN);
				System.out.println(StyledMessage.Status.info(result));
				return;
			
			default:
				System.out.println(
						StyledMessage.Status.warning("Invalid choice, enter valid(1- 5) or (0 to back) input "));
			}
		}
	}

}
