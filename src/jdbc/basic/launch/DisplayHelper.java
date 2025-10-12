package jdbc.basic.launch;

import java.util.List;
import java.util.Map;

public class DisplayHelper {
	@SuppressWarnings({ "unchecked" })
	public static String showData(Object data) {
		if (data instanceof List) {
			return showMultipleRow((List<Map<String, Object>>) data);
		} else if (data instanceof Map) {
			return showSingleRow((Map<String, Object>) data);
		} else {
			return MessageStyler.makeYellow() + Emoji.FIRE + Constants.OperationMessage.UNSUPPORTED.getMessage()
					+ Color.RESET;
		}
	}

	public static String showMultipleRow(List<Map<String, Object>> rows) {
		String result = " ";
		int count = 1;
		for (Map<String, Object> row : rows) {
			result = printMap(row);
			System.out.println(MessageStyler.makeGreen(" #Row-"+count+" "+Constants.OperationStatus.FINISHED.getMessage()+Emoji.CHECKMARK));
			count++;
			if (result != null && !result.isEmpty()) {
				continue;
			}
			
		}
		return MessageStyler.makeRed()+Emoji.FIRE+" "+(count -1)+Color.RESET+MessageStyler.makeGreen(" Rows ")+result;

	}

	public static String showSingleRow(Map<String, Object> row) {
		return printMap(row);
	}

	// Uses wildcard generics to ensure read-only safety.
	// Accepts any Map type (e.g., Map<String, Object>, Map<Integer, User>).
	// Prevents accidental mutation and supports flexible display logic.
	public static String printMap(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return MessageStyler.makeRed(Emoji.CROSSMARK + " Data not found");

		}
		map.forEach((key, value) -> {
			System.out.print(MessageStyler.makeGreen() + key + " : " + Color.RESET);
			System.out.print(MessageStyler.makePurple() + Emoji.INFO + value + Color.RESET+" ");
			System.out.println();

		});
		return MessageStyler.makeGreen(Emoji.SUCCESS + " Reading successful.");

	}

}
