package jdbc.basic.launch;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

//re-factured from constant fields to constant-interface and enum
public class Constants {

	// Menu options(constants)
	public static class Menu {
		public static final int CREATE_TABLE = 1;
		public static final int INSERT_DATA = 2;
		public static final int READ_DATA = 3;
		public static final int UPDATE_DATA = 4;
		public static final int DELETE_DATA = 5;
		public static final int EXIT = 9;
	}

	// Input status
	public static class Input {
		public static final int MAX_ATTEMPTS = 3;
		public static final int ZERO = 0;
		public static final int MIN_ATTEMPTS = ZERO;
		public static final int MIN_OPTION = Menu.CREATE_TABLE;
		public static final int MAX_OPTION = Menu.DELETE_DATA;
	}

	// for default values
	public static class DefaultValue {
		public static final int DEFAULT_INTEGER = 000;
		public static final double DEFAULT_DOUBLE = 00.00;
		public static final String DEFAULT_STRING = "unspecified";
		public static final String UNKNOWN = "N/A";
		public static final String DEFAULT_BOOLEAN = UNKNOWN;
		// Captures the system date and time when this class is first loaded.
		// Handy for things like logging app startup, using a fixed reference point, or applying default values.
		// Note: These won’t update during runtime—they’re locked to the moment the app begins.
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

	public enum OperationStatus {
		// Enum constants must be declared before fields and constructor
		// Each constant calls the constructor with its message

		SUCCESS("success"), FAILED("failed"), EXISTED("already exists"), ERROR("something went wrong!");

		private final String message;

		OperationStatus(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public enum OperationMessage {
		DB_CREATED("Database created successfully."), DB_CREATION_FAILED("Database creation failed."),
		DB_SKIPPED("Database creation skipped."), DB_NOT_FOUND("Database not found"),

		TABLE_CREATED("Table created successfully."), TABLE_CREATION_FAILED("Table creation failed."),
		TABLE_NOT_FOUND("Table not found."),TABLE_EXISTED("Table already existed."),

		DATA_INSERTED("Data inserted successfully."), DATA_INSERT_FAILED("Data insertion failed."),

		DATA_UPDATED("Data updated successfully."), DATA_UPDATE_FAILED("Data update failed."),

		DATA_DELETED("Data deleted successfully."), DATA_DELETE_FAILED("Data deletion failed."),

		UNKNOWN_ERROR("Something went wrong!");

		private String message;

		OperationMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

}
