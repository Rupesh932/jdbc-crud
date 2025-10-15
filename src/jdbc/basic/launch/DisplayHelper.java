package jdbc.basic.launch;

import java.util.List;
import java.util.Map;

import jdbc.basic.launch.Constants.QueryStatus;

public class DisplayHelper {
	@SuppressWarnings({ "unchecked" })
	public static String showData(Object data) {
		if (data instanceof List) {
			return showMultipleRow((List<Map<String, Object>>) data);
		} else if (data instanceof Map) {
			return showSingleRow((Map<String, Object>) data);
		} else {
			return StyledMessage.Status.warning(QueryStatus.UNSUPPORTED.getMessage());

		}
	}

	public static String showMultipleRow(List<Map<String, Object>> rows) {
		String result = " ";
		int count = 1;
		for (Map<String, Object> row : rows) {
			result = printMap(row);
			System.out.println(StyledMessage.Status.success(" #Row-" + count + result));
			count++;

		}
		return StyledMessage.Action.sparkle((count - 1) + " Rows " + result);

	}

	public static String showSingleRow(Map<String, Object> row) {
		return printMap(row);
	}

	// Uses wildcard generics to ensure read-only safety.
	// Accepts any Map type (e.g., Map<String, Object>, Map<Integer, User>).
	// Prevents accidental mutation and supports flexible display logic.
	public static String printMap(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return StyledMessage.Status.failed(" Data not found");

		}
		if (map.containsKey("Error") || map.containsKey("Status")) {
			map.forEach((key, value) -> {
				System.out.print(StyledMessage.Status.error(key + " : " + Color.Foreground.PURPLE + value));
				System.out.println(Color.Reset.RESET);

			});
			return "Reading failed.";
		}
		map.forEach((key, value) -> {

			System.out.print(StyledMessage.Status.info(key + " : " + Color.Foreground.PURPLE + value));
			System.out.println(Color.Reset.RESET);

		});
		return " Reading successful.";

	}

}
