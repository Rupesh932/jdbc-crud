package jdbc.basic.launch;

public class Launch {

	public static void main(String[] args) {
		MenuManager manager = new MenuManager();
		manager.menuHandler();

		InputManager.closeResource();

	}

}
