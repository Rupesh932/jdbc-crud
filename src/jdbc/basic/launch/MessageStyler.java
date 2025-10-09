package jdbc.basic.launch;

public class MessageStyler {
	static String reset = Color.RESET;
	
	public static String makeGreen() {
		return Color.GREEN  ;
	}
	public static String makeGreen(String msg) {
		return Color.GREEN + msg  + reset;
	}
	
	public static String makeRed() {
		return Color.RED;
	}
	public static String makeRed(String msg) {
		return Color.RED + msg  + reset;
	}
	
	public static String makeBlue() {
		return Color.BLUE;
	}
	public static String makeBlue(String msg) {
		return Color.BLUE + msg  + reset;
	}
	
	public static  String makeYellow() {
		return Color.YELLOW ;
	}
	public static  String makeYellow(String msg) {
		return Color.YELLOW + msg  + reset;
	}
	
	public static  String makePurple() {
		return Color.PURPLE ;
	}
	public static  String makePurple(String msg) {
		return Color.PURPLE + msg + reset;
	}
	
	public static String makeCyna() {
		return Color.CYAN ;
				
	}
	public static String makeCyna(String msg) {
		return Color.CYAN  + msg + reset;
				
	}
	
}
