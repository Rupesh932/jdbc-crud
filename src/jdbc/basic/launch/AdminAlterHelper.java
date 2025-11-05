package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jdbc.basic.launch.Constants.ColumnAction;
import jdbc.basic.launch.Constants.QueryStatus;

public class AdminAlterHelper {
	public static QueryStatus handleAlterTable(String dbName, String tableName, QueryStatus status) {
		return switch (status) {
		case ADD_COLUMN -> addColumn(dbName, tableName);
		case DROP_COLUMN -> dropColumn(dbName, tableName);
		case MODIFY_COLUMN -> modifyColumn(dbName, tableName);

		default -> QueryStatus.UNSUPPORTED;

		};

	}

	public static QueryStatus addColumn(String dbName, String tableName) {

		List<ColumnMeta> meta = SchemaInspector.getInsertableColumns(dbName, tableName);
		Set<String> existedCols = meta.stream().map(ColumnMeta::colName).map(String::toLowerCase)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		String colName;
		int count = 3;
		while (true) {
			System.out.println(StyledMessage.Status.info("Available table fields: " + existedCols));
			colName = Validation.validateColumnName("Enter column name to add to table");
			if (existedCols.contains(colName.toLowerCase())) {
				if (count > 0) {

					System.out.println(
							StyledMessage.Status.warning("Column '" + colName + "'" + " already existed, try new one"));
					count--;
				} else {
					return QueryStatus.MAX_LIMIT;
				}
			} else {
				break;
			}
		}
		String colConstrain = InputManager
				.stringInput("Enter constraints for column '" + colName + "' (e.g. VARCHAR(20) NOT NULL UNIQUE)");
		String qry = String.format("ALTER TABLE `%s`.`%s` ADD COLUMN `%s` %s ", dbName, tableName, colName,
				colConstrain);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			return QueryStatus.ADD_COLUMN;
		} catch (SQLException e) {

			return QueryStatus.COLUMN_ADDED_FAILED;
		}
	}

	public static QueryStatus modifyColumn(String dbName, String tableName) {
//		List<ColumnMeta> meta = SchemaInspector.getInsertableColumns(dbName, tableName);
//		Set<String> existedCols = meta.stream().map(ColumnMeta::colName).map(String::toLowerCase)
//				.collect(Collectors.toCollection(LinkedHashSet::new));
		List<String[]> info = new ArrayList<>();
		String qry = String.format("SHOW FULL COLUMNS FROM `%s` FROM `%s`", tableName, dbName);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(qry)) {

			while (rs.next()) {
				String[] metaInfo = new String[] { rs.getString("Field"), rs.getString("Type"), rs.getString("Key"),
						rs.getString("Null"), rs.getString("Extra") };
				info.add(metaInfo);
			}
			Set<String> existedCols = info.stream().map(arr -> arr[0].toLowerCase())
					.collect(Collectors.toCollection(LinkedHashSet::new));

			System.out.println(StyledMessage.Status.info("Available table fields: " + existedCols));
			String colName = Validation.validateColumnName("Enter column(field) name to modify it");
			if (!existedCols.contains(colName)) {
				return QueryStatus.COLUMN_NOT_PRESENT;
			}

			for (String[] arr : info) {
				String fieldName = arr[0];
				if (fieldName.equalsIgnoreCase(colName)) {
					String keyType = arr[2];
					if ("PRI".equalsIgnoreCase(keyType)) {
						String msg = StyledMessage.Status.warning(" Field '" + arr[0] + "' is a PRIMARY KEY.");
						System.out.println(msg);
						return QueryStatus.PRIMARY_KEY_MODIFY_NOT_ALLOWED;

					}
					System.out.println(StyledMessage.Status.info(String.format(
							"Properties =>' Column: '%s',  Type: '%s',  Key: '%s',  Null: '%s',  Extra: '%s' '", arr[0],
							arr[1], arr[2], arr[3], arr[4])));
				}
			}
		
			String currentColName = colName;
			while (true) {
				ServiceMenu.renderModifyColumnMenu();
				int choice = InputManager.intInput("Enter your choice to modify column's property");
				try {
					ColumnAction action = ColumnAction.fromCode(choice);
					
					ColumnModifier modifier = new ColumnModifier(dbName, tableName, currentColName, info);
					QueryStatus status =  switch (action) {
					case RENAME -> {
						QueryStatus renameStatus = modifier.rename();
						if(renameStatus == QueryStatus.RENAME_SUCCESS) {
							currentColName = modifier.getColName();
						}
						yield renameStatus;
					}
					case TYPE -> modifier.changeType();
					case KEY -> modifier.modifyKeyConstraint();
					case EXTRA -> modifier.updateExtraFlags();
					case NULL -> modifier.toggleNullability();
					case BACK -> QueryStatus.MODIFICATION_SKIPPED;
					};
				if(action == ColumnAction.BACK) {
					return status;
				}
				if(!handelMoreModification(status)) {
					return status;
				}

				} catch (IllegalArgumentException e) {
					String msg = StyledMessage.Status.warning(e.getMessage());
					System.out.println(msg);
				}
			}

		} catch (Exception e) {
			String msg = StyledMessage.Status.error(e.getMessage());
			System.out.println(msg);
		}

		return null;
	}

	public static QueryStatus dropColumn(String dbName, String tableName) {
		List<ColumnMeta> meta = SchemaInspector.getInsertableColumns(dbName, tableName);
		Set<String> dropableCols = new HashSet<String>();
		for (ColumnMeta info : meta) {
			if (info.isUnique()) {
				String msg = StyledMessage.Status
						.warning("Skipping drop column '" + info.colName() + "' — unique constraint detected.");
				System.out.println(msg);
			} else {
				dropableCols.add(info.colName());
			}
		}

		System.out.println(StyledMessage.Status.info("Droapable table fields: " + dropableCols));
		String dropCol = Validation.validateColumnName("Enter column name to drop from table, can choose from "
				+ Emoji.Navigation.UP_ARROW + " available list " + " ");
		if (!dropableCols.contains(dropCol)) {
			String msg = "Column '" + dropCol + "'  not present in table '" + tableName + "'";
			System.out.println(StyledMessage.Status.warning(msg));
			return QueryStatus.COLUMN_NOT_PRESENT;
		}
		String qry = String.format("ALTER TABLE `%s`.`%s` DROP COLUMN `%s` ", dbName, tableName, dropCol);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName); Statement st = con.createStatement()) {
			st.executeUpdate(qry);
			return QueryStatus.DROP_COLUMN;
		} catch (SQLException e) {
			e.printStackTrace();
			return QueryStatus.COLUMN_DROPPED_FAILED;
		}

	}

	public static void addConstrains(String dbName, String tableName) {

	}

	public static void dropConstrains(String dbName, String tableName) {

	}
	public static boolean handelMoreModification(QueryStatus status) {
		System.out.println(StyledMessage.Status.success(status.getMessage()));
		 int yesNo = InputManager.charInput("Press y/Y to modify more or press any key to ignore");
		 return Character.toLowerCase(yesNo) == 'y';
		  
	}
}
