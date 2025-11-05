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
		return StyledMessage.Action.sparkle("Found  " + (count - 1) + " Row/s from database");

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


		if (QueryStatus.ID_NOT_FOUND.name().equals(map.get("Status"))) {
			System.out.print(StyledMessage.Status.error("Id " +map.get("id")+ " is not found"));
			System.out.println(Color.Reset.RESET);

			return StyledMessage.Status.failed("Reading failed.");
		}
		map.forEach((key, value) -> {

			System.out.print(StyledMessage.Status.info(key + " : " + Color.Foreground.PURPLE + value));
			System.out.println(Color.Reset.RESET);

		});
		return StyledMessage.Status.success( " Read completed.");

	}

}
