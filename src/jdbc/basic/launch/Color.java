package jdbc.basic.launch;

public class Color {

	public static class Reset {
		public static final String RESET = "\u001B[0m"; // Reset all styles
	}
	 public static final String MAGENTA = "\u001B[45m"; 

	public static class Foreground {
		public static final String BLACK = "\u001B[30m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";
		public static final String YELLOW = "\u001B[33m";
		public static final String BLUE = "\u001B[34m";
		public static final String PURPLE = "\u001B[35m";
		public static final String CYAN = "\u001B[36m";
		public static final String WHITE = "\u001B[37m";
	}

	public static class BrightForeground {
		public static final String BLACK = "\u001B[90m";
		public static final String RED = "\u001B[91m";
		public static final String GREEN = "\u001B[92m";
		public static final String YELLOW = "\u001B[93m";
		public static final String BLUE = "\u001B[94m";
		public static final String PURPLE = "\u001B[95m";
		public static final String CYAN = "\u001B[96m";
		public static final String WHITE = "\u001B[97m";
		  
	}

	public static class Background {
		public static final String BLACK = "\u001B[40m";
		public static final String RED = "\u001B[41m";
		public static final String GREEN = "\u001B[42m";
		public static final String YELLOW = "\u001B[43m";
		public static final String BLUE = "\u001B[44m";
		public static final String PURPLE = "\u001B[45m";
		public static final String CYAN = "\u001B[46m";
		public static final String WHITE = "\u001B[47m";
		 public static final String MAGENTA = "\u001B[45m"; 
	}

	public static class BrightBackground {
		public static final String BLACK = "\u001B[100m";
		public static final String RED = "\u001B[101m";
		public static final String GREEN = "\u001B[102m";
		public static final String YELLOW = "\u001B[103m";
		public static final String BLUE = "\u001B[104m";
		public static final String PURPLE = "\u001B[105m";
		public static final String CYAN = "\u001B[106m";
		public static final String WHITE = "\u001B[107m";
		public static final String MAGENTA = "\u001B[105m";
	}

	public static class Style {
		public static final String BOLD = "\u001B[1m";
		public static final String DIM = "\u001B[2m";
		public static final String ITALIC = "\u001B[3m"; // May not work in all terminals
		public static final String UNDERLINE = "\u001B[4m";
		public static final String BLINK = "\u001B[5m"; // Rarely supported
		public static final String REVERSE = "\u001B[7m"; // Swaps foreground/background
		public static final String HIDDEN = "\u001B[8m"; // Hidden text
	}
}
