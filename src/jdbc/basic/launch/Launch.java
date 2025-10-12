package jdbc.basic.launch;

public class Launch {

	public static void main(String[] args) {
		MenuManager manager = new MenuManager();
		manager.menuHandler();

		InputManager.closeResource();
		// TODO: Fix overlapping success message when db/table not found during read


	}

}
