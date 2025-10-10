package jdbc.basic.launch;

import java.util.Scanner;

public class InputManager {
	static Scanner  scan = new Scanner(System.in);

	public static void closeResource() {
		scan.close();
	}
	public static int intInput(String prompt) {
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

	public static String stringInput(String prompt) {
		System.out.print(MessageStyler.makeBlue());
		System.out.println(Emoji.INPUT + prompt);
		String value = scan.nextLine();
		System.out.println(Color.RESET);
		return value;

	}

	public static char charInput(String prompt) {
		System.out.print(MessageStyler.makeBlue());
		System.out.println(Emoji.INPUT + prompt);
		String character = scan.nextLine();
		System.out.println(Color.RESET);
		return character.isEmpty() ? ' ' : character.charAt(0);
	}

	public static Double decimalInput(String prompt) {
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

}
