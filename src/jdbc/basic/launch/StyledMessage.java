package jdbc.basic.launch;

public class StyledMessage {

	// This is a utility class — instantiation is not allowed
	private StyledMessage() {
		throw new UnsupportedOperationException("Utility class");
	}

	// 🔹 System Messages
	public static class System {
		public static String exit(String msg) {
			return exit(msg, Emoji.Status.EXIT);
		}

		public static String exit(String msg, String emoji) {
			return render(Color.BrightBackground.BLACK, Color.BrightForeground.RED, Color.Style.BOLD, emoji, msg);
		}
	}

	// 🔹 Status Messages
	public static class Status {
		public static String success(String msg) {
			return success(msg, Emoji.Status.SUCCESS);
		}

		public static String success(String msg, String emoji) {
			return render(Color.BrightBackground.GREEN, Color.Foreground.BLACK, Color.Style.BOLD, emoji, msg);
		}

		public static String error(String msg) {
			return error(msg, Emoji.Status.ERROR);
		}

		public static String error(String msg, String emoji) {
			return render(Color.BrightBackground.RED, Color.Foreground.WHITE, Color.Style.BOLD, emoji, msg);
		}

		public static String warning(String msg) {
			return warning(msg, Emoji.Status.WARNING);
		}

		public static String warning(String msg, String emoji) {
			return render(Color.BrightBackground.YELLOW, Color.Foreground.BLACK, Color.Style.UNDERLINE, emoji, msg);
		}

		public static String info(String msg) {
			return info(msg, Emoji.Status.INFO_ALT);
		}

		public static String info(String msg, String emoji) {
			return render(Color.BrightBackground.CYAN, Color.Foreground.BLACK, Color.Style.DIM, emoji, msg);
		}

		public static String failed(String msg) {
			return failed(msg, Emoji.Status.FAIL_ALT);
		}

		public static String failed(String msg, String emoji) {
			return render(Color.Background.RED, Color.BrightForeground.YELLOW, Color.Style.REVERSE, emoji, msg);
		}
	}

	// 🔹 Action Messages
	public static class Action {
		public static String fixed(String msg) {
			return fixed(msg, Emoji.Action.FIXED);
		}

		public static String fixed(String msg, String emoji) {
			return render(Color.Background.GREEN, Color.Foreground.WHITE, Color.Style.BOLD, emoji, msg);
		}

		public static String deploy(String msg) {
			return deploy(msg, Emoji.Build.DEPLOY);
		}

		public static String deploy(String msg, String emoji) {
			return render(Color.BrightBackground.BLUE, Color.Foreground.WHITE, Color.Style.BOLD, emoji, msg);
		}

		public static String sparkle(String msg) {
			return sparkle(msg, Emoji.Action.SPARKLES);
		}

		public static String sparkle(String msg, String emoji) {
			return render(Color.Background.BLACK, Color.BrightForeground.GREEN, Color.Style.BOLD, emoji, msg);
		}

		public static String debug(String msg) {
			return debug(msg, Emoji.Action.BUG);
		}

		public static String debug(String msg, String emoji) {
			return render(Color.Background.BLACK, Color.BrightForeground.GREEN, Color.Style.DIM, emoji, msg);
		}
	}

	// 🔹 Input Prompt
	public static class Input {
		public static String prompt(String msg) {
			return Color.BrightBackground.BLUE + Color.Foreground.WHITE + Color.Style.BOLD + " " + Emoji.UI.INPUT + " "
					+ msg + ": ";
		}

		public static String preview(String msg) {
			return Color.BrightBackground.CYAN + Color.Foreground.BLACK + Color.Style.BOLD + " " + Emoji.UI.PREVIEW
					+ " " + msg + Color.Reset.RESET;

		}

	}

	public static class Banner {
		public static String menu(String title, String body) {
			return Color.BrightBackground.MAGENTA + Color.Foreground.BLACK + Color.Style.BOLD + " " + Emoji.UI.MENU
					+ " " + title + Color.Reset.RESET + "\n\n" + Color.BrightForeground.PURPLE + Color.Style.BOLD+body
					+ Color.Reset.RESET;
		}
	}

	// 🔹 Custom Styling
	public static class Custom {
		public static String styled(String bgColor, String fgColor, String style, String emoji, String msg) {
			return render(bgColor, fgColor, style, emoji, msg);
		}
	}

	// 🔹 Core Renderer
	private static String render(String bgColor, String fgColor, String style, String emoji, String msg) {
		return bgColor + fgColor + style + " " + emoji + " " + msg + Color.Reset.RESET;
	}
}
