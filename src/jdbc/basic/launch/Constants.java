package jdbc.basic.launch;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//re-factured from constant fields to constant-interface and enum
public class Constants {
	public static class IntConstant {
		public static final int ZERO = 0;
		public static final int ONE = 1;
		public static final int TWO = 2;
		public static final int THREE = 3;
		public static final int FOUR = 4;
		public static final int FIVE = 5;
		public static final int SIX = 6;
		public static final int SEVEN = 7;
		public static final int EIGHT = 8;
		public static final int NINE = 9;

	}

	// alter table options
	public static class SubMenu {
		public static final int ADD_COLUMN = IntConstant.ONE;
		public static final int MODIFY_COLUMN = IntConstant.THREE;
		public static final int DROP_COLUMN = IntConstant.TWO;
		public static final int ADD_CONSTRAINS = IntConstant.FOUR;
		public static final int DROP_CONSTRAINS = IntConstant.FIVE;
		public static final int BACK = IntConstant.ZERO;

	}

	// Menu options
	public static class Menu {
		public static final int CREATE_TABLE = IntConstant.ONE;
		public static final int INSERT_DATA = IntConstant.TWO;
		public static final int READ_DATA = IntConstant.THREE;
		public static final int UPDATE_DATA = IntConstant.FOUR;
		public static final int DELETE_DATA = IntConstant.FIVE;
		public static final int DROP_TABLE = IntConstant.SIX;
		public static final int DROP_DATABASE = IntConstant.SEVEN;
		public static final int ALTER_TABLE = IntConstant.EIGHT;
		public static final int EXIT = IntConstant.NINE;

	}

	// Input status
	public static class Input {
		public static final int MAX_ATTEMPTS = IntConstant.THREE;
		public static final int MIN_ATTEMPTS = IntConstant.ZERO;
		public static final int MIN_OPTION = IntConstant.ONE;
		public static final int MAX_OPTION = Menu.ALTER_TABLE;
	}

	// for default values
	public static class DefaultValue {
		public static final int DEFAULT_INTEGER = IntConstant.ZERO;
		public static final double DEFAULT_DOUBLE = 0.0;
		public static final String DEFAULT_STRING = "unspecified";
		public static final String UNKNOWN = "N/A";
		public static final String DEFAULT_BOOLEAN = UNKNOWN;
		// Captures the system date and time when this class is first loaded.
		// Handy for things like logging app startup, using a fixed reference point, or
		// applying default values.
		// Note: These won’t update during runtime—they’re locked to the moment the app
		// begins.
		public static final LocalDate DEFAULT_DATE = LocalDate.now();
		public static final Timestamp DEFAULT_TIMESTAMP = Timestamp.valueOf(LocalDateTime.now());

	}

	public static class ColumnType {
		public static final String INTEGER = "integer";
		public static final String STRING = "string";
		public static final String DATE = "date";
		public static final String TIMESTAMP = "timestamp";
		public static final String BOOLEAN = "boolean";
		public static final String UNKNOWN = "unknown";
		public static final String NUMERIC = "numeric";
	}

	public enum QueryStatus {
		// Enum constants must be declared before fields and constructor
		// Each constant calls the constructor with its message
		SUCCESS("SUCCESS", "Operation successful"), FAILED("FAILED", "Operation failed"),
		ERROR("ERROR", "Something went wrong"), NOT_FOUND("NOT_FOUND", "No matching record found"),
		EXCEPTION("EXCEPTION", "Exception occured"), MAX_LIMIT("MAX_LIMIT", "Invalid  try limit reached"),
		SQL_EXCEPTION("SQL_EXCEPTION", "SQLException occurs"),TODO("TODO","Implementation needed"),

		ID_NOT_FOUND("ID_NOT_FOUND", "Id not found"), USERNAME_NOT_MATCHED("USER_NOT_MATCHED", "Usename not matched"),
		USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
		PASSWORD_NOT_MATCHED("PASSWORD_NOT_MATCHED", "Password not matched"),

		DB_CREATED("DB_CREATED", "Database created successfully"),
		DB_CREATION_FAILED("DB_CREATION_FAILED", "Database creation failed"),
		DB_NOT_FOUND("DB_NOT_FOUND", "Database not found"), DB_EXISTED("DB_EXISTED", "Database already existed"),
		DB_SKIPPED("DB_SKIPPED", "Databae creation skipped"), DB_DROPPED("DB_DROPPED", "Database dropped successfully"),

		TABLE_CREATED("TABLE_CREATED", "Table created successfully"),
		TABLE_CREATION_FAILED("TABLE_CREATION_FAILED", "Table creation failed"),
		TABLE_NOT_FOUND("TABLE_NOT_FOUND", "Table not found"), TABLE_EXISTED("TABLE_EXISTED", "Table already existed"),
		TABLE_SKIPPED("TABLE_SKIPPED", "Table creation skipped"),
		TABLE_DROPPED("TABLE_DROPPED", "Table dropped successfully"),

		// Alter actions
		ADD_COLUMN("ADD_COLUMN", "Column added successfully"),
		DROP_COLUMN("DROP_COLUMN", "Column dropped successfully"),
		MODIFY_COLUMN("MODIFY_COLUMN", "Column modified successfully"),
		COLUMN_ADDED_FAILED("COLUMN_ADDED_FAILED", "Column addition failed"),
		COLUMN_DROPPED_FAILED("COLUMN_DROPPED_FAILED", "Column drop failed"),
		COLUMN_EXISTED("COLUMN_EXISTED", "Column already existed"),
		COLUMN_NOT_PRESENT("COLUMN_NOT_PRESENT", "Column not present in table"),

		DATA_INSERTED("DATA_INSERTED", "Data inserted successfully"),
		DATA_INSERT_FAILED("DATA_INSERT_FAILED", "Data insertion failed"),

		DATA_UPDATED("DATA_UPDATED", "Data updated successfully"),
		DATA_UPDATE_FAILED("DATA_UPDATE_FAILED", "Data update failed"),
		PRIMARY_KEY_NOT_FOUND("PRIMARY_KEY_NOT_FOUND", "Primary key not found in table"),
		NONE_FIELD_CHOOSEN("NONE_FIELD_CHOOSEN", "You didn't select/choose any field to update"),
		UPDATE_SUSPENDED("UPDATE_SUSPENDED", "Update table process suspended"),

		ROW_DELETED("ROW_DELETED", "Row deleted successfully"),
		ROW_DELETION_FAILED("ROW_DELETION_FAILED", "Row deletion failed"),
		ROW_NOT_EXITED("ROW_NOT_EXISTED","Row is not found in table "),
		

		UNSUPPORTED("UNSUPPORTED", "Unsupported data type"),

		// modify column
		RENAME_SUCCESS("RENAME_SUCCESS", "Column renamed successfully"),
		RENAME_FAILED("RENAME_FAILED", "Column rename failed"),

		TYPE_CHANGE_SUCCESS("TYPE_CHANGE_SUCCESS", "Column type changed successfully"),
		TYPE_CHANGE_FAILED("TYPE_CHANGE_FAILED", "Column type change failed"),
		TYPE_CHANGE_SKIPPED("TYPE_CHANGE_SKIPPED", "Column type change skipped"),

		NULL_TOGGLED("NULL_TOGGLED", "Nullability toggled successfully"),
		NULL_TOGGLE_FAILED("NULL_TOGGLE_FAILED", "Nullability toggle failed"),

		//like auto-increment constrain
		EXTRA_UPDATED("EXTRA_UPDATED", "Extra flags updated successfully"),
		EXTRA_UPDATE_FAILED("EXTRA_UPDATE_FAILED", "Extra flags update failed"),
		
		
		KEY_MODIFY_SUCCESS("KEY_MODIFY_SUCCESS", "Key constraint modified successfully"),
		KEY_DROP_SUCCESS("KEY_DROP_SUCCESS","Dropped key successful"),
		KEY_DROP_FAILED("KEY_DROP_FAILED","Fail to drop key"),
		KEY_MODIFY_FAILED("KEY_MODIFY_FAILED", "Key constraint modification failed"),
		PRIMARY_KEY_MODIFY_NOT_ALLOWED("PRIMARY_KEY_MODIFY_NOT_ALLOWED", "Primary key modification is not allowed"),
		MULTI_PK_NOT_SUPPORTED("MULTI_PK_NOT_SUPPORTED","Composit PK not supported "),
		MODIFICATION_SKIPPED("MODIFICATION SKIPPED","Modification process is skipped");

		private final String code;
		private final String message;

		QueryStatus(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("Status", code);
			map.put("Message", message);
			return map;
		}
	}

	public enum ColumnAction {
		RENAME(1, "Rename Column"), TYPE(2, "Change Data Type"), KEY(3, "Modify Key Constraint"),
		NULL(4, "Toggle Nullability"), EXTRA(5, "Update Extra Flags"), BACK(0, "Back");

		private final int code;
		private final String label;

		ColumnAction(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

		public static ColumnAction fromCode(int choice) {
			for (ColumnAction action : ColumnAction.values()) {
				if (action.code == choice) {
					return action;
				}
			}
			throw new IllegalArgumentException(
					"Invalid  choice " + choice + "  please choose between(1-5) or 0 to back ");
		}
	}

	public enum ReadMode {
		ALL, BY_ID, BY_CREDENTIALS;
	}

	public enum CrudMode {
		INSERT, UPDATE, DELETE;
	}
}
