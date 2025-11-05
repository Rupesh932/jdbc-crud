package jdbc.basic.launch;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import jdbc.basic.launch.Constants.CrudMode;
import jdbc.basic.launch.Constants.QueryStatus;

public class AdminUpdateHelper {

	public static CrudMode mode = Constants.CrudMode.INSERT;

	public static QueryStatus updateTable(String dbName, String tableName) {
		mode = Constants.CrudMode.UPDATE;
		List<ColumnMeta> meta = SchemaInspector.getInsertableColumns(dbName, tableName);

		List<ColumnMeta> tempMeta = new ArrayList<>();// temp to store row's meta-info

		Set<String> existedCols = meta.stream().map(ColumnMeta::colName)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		System.out.println(StyledMessage.Action.fixed("Updatable columns: " + existedCols));

		Map<String, Object> newValues = new LinkedHashMap<>();

		List<String> pkCols = new ArrayList<>();
		String msg = " ";
		try {

			for (ColumnMeta info : meta) {
				msg = StyledMessage.Input.prompt("Want to update '" + info.colName() + "' ?");
				System.out.println(msg);
				char yesNo = InputManager.charInput("Enter y/Y to update or enter any key to cancel update ");
				if (Character.toLowerCase(yesNo) == 'y') {

					tempMeta.add(new ColumnMeta(info.colName(), info.intCode(), info.isMandatory(), info.isUnique(),
							info.isPasswordField()));

					newValues.putAll(new CrudFlowHelper().dataCollector(dbName, tableName, tempMeta));

					tempMeta.clear();
				} else {
					msg = StyledMessage.Status.warning("no updation for field '" + info.colName() + "'\n");
					System.out.println(msg);
				}

			}

			if (newValues.isEmpty()) {
				msg = StyledMessage.Status.info("LOL none field are choosen to be updated...");
				System.out.println(msg);
				return QueryStatus.NONE_FIELD_CHOOSEN;
			}
			// System.out.println("new values: "+newValues);
			pkCols = SchemaInspector.getPrimaryKeyCols(dbName, tableName);

			if (pkCols.isEmpty()) {
				msg = StyledMessage.Status
						.warning("Primary key is not defined in table, DDl operation denied " + tableName);
				System.out.println(msg);
				return QueryStatus.PRIMARY_KEY_NOT_FOUND;
			}
			//System.out.println(pkCols);
			//System.out.println("size: "+pkCols.size());
			if (pkCols.size() == Constants.IntConstant.ONE) {
				String pkCol = pkCols.getFirst();
				//System.out.println("pkCol "+ pkCol);
			try(Connection con = DbConnectionManager.getServerConnection()){
				DatabaseMetaData getMeta = con.getMetaData();
				ResultSet rs = getMeta.getColumns(dbName, null, tableName, pkCol);
				if(rs.next()) {
					int sqlType = rs.getInt("DATA_TYPE");
					String colType = CrudHelper.getColumnType(sqlType);
					 msg = StyledMessage.Status
							.info("Column '" + pkCol + "' is defined as 'primary key' of typed '" + colType
									+ "' for table '" + tableName + "' ");
					System.out.println(msg);
					Object rowIdentity = CrudHelper.getColumnValue(colType, pkCol);
					//System.out.println(pkCol+" value: "+rowIdentity);
					boolean isRowExited = AdminDeleteHelper.isRowExist(dbName, tableName, pkCol, rowIdentity);
					if(!isRowExited) {
						System.out.println(QueryStatus.ROW_NOT_EXITED.getMessage());
							return QueryStatus.ROW_NOT_EXITED;
						}
					return fireExecuteUpdate(dbName, tableName, newValues, pkCol,rowIdentity);			
				}
				return null;
				
			}catch(SQLException e) {
				System.out.println(StyledMessage.Status.error(e.getMessage()));
				return QueryStatus.SQL_EXCEPTION;
			}
	
			} else {
//				msg = StyledMessage.Status.info("Defined primary key on table '" + tableName + "' " + pkCols);
//				System.out.println(msg);
//				String pkCol = Validation.validateColumnName(
//						"Enter one primary key(choose from above list) on which to perform updation");
//				return fireExecuteUpdate(dbName, tableName, newValues, pkCol,rowIdentity);
				return QueryStatus.MULTI_PK_NOT_SUPPORTED;
			}
		} finally {
			mode = Constants.CrudMode.INSERT;
		}

//		UPDATE `dbName`.`tableName`
//		SET `col1` = ?, `col2` = ?, ...
//		WHERE `pkCol` = ?;

	}

	private static QueryStatus fireExecuteUpdate(String dbName, String tableName, Map<String, Object> newValues,
			String pkCol,Object rowIdentity) {

		String initialQry = String.format("UPDATE `%s`.`%s` SET  ", dbName, tableName);
		StringBuilder sb = new StringBuilder().append(initialQry);
		for (Entry<String, Object> info : newValues.entrySet()) {
			String colName = "`" + info.getKey() + "` = ?, ";
			sb.append(colName);
		}
		sb.setLength(sb.length() - 2);
		sb.append(" WHERE ").append(pkCol).append(" = ? ");
		String finalQuery = sb.toString();
		// System.out.println("final qry : " + finalQuery);
		try (Connection con = DbConnectionManager.getDatabaseConnection(dbName);
				PreparedStatement ps = con.prepareStatement(finalQuery)) {

			int index = Constants.IntConstant.ONE;
			for (Entry<String, Object> entry : newValues.entrySet()) {

				ps.setObject(index, entry.getValue());
//				 System.out.println("Param " + index + " (" + entry.getKey() + "): " +
//				 entry.getValue());
				index++;
			}
			ps.setObject(index, rowIdentity);
			//System.out.println("Param " + index + " (PK): " + pkCol);
			return ps.executeUpdate() > 0 ? QueryStatus.DATA_UPDATED : QueryStatus.DATA_UPDATE_FAILED;

		} catch (SQLException e) {
			e.printStackTrace();
			return QueryStatus.SQL_EXCEPTION;
		}
	}

}
