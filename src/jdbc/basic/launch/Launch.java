package jdbc.basic.launch;

public class Launch {

	public static void main(String[] args) {
//		System.out.println("Java version: " + System.getProperty("java.version"));
//		System.out.println("Java vendor: " + System.getProperty("java.vendor"));
//		System.out.println("Java home: " + System.getProperty("java.home"));
		
		
		MenuManager manager = new MenuManager();
		manager.menuHandler();
		
		

		
		


		InputManager.closeResource();
		


	}

}
