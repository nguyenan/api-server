package storage;

import java.io.IOException;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class TableDevTools {
	// CONST
	private static final String _APP_STR = "core";
	private static final IdData _APP = new IdData(_APP_STR);
	private static final IdData _TABLEID = IdData.create("flat2");

	// TABLE SOURCE
	private static CassandraSource cassSource = new CassandraSource();

	public static void copyTable(String fromCustomerId, String toCustomerId, String tablename) throws IOException {

		ListData listData = getRows(fromCustomerId, tablename);
		for (Object obj : listData) {
			MappedData mappedData = (MappedData) obj;
			// copy product
			String rowId = mappedData.get("id").toString();
			String realdId = rowId.substring(rowId.lastIndexOf(":") + 1, rowId.length());
			addNewRow(toCustomerId, tablename, realdId, mappedData);
		}
	}

	public static BooleanData addNewRow(String customerId, String tableName, String id, MappedData mappedData) {
		// Obtain tableId and new RowId
		String table = getTableName(customerId, tableName);
		IdData rowId = getRowIdData(table, id);

		mappedData.put("table", table);
		mappedData.put("id", rowId.toString());

		cassSource.updateRow(new IdData(customerId), _APP, _TABLEID, rowId, mappedData);
		return new BooleanData(true);
	}

	public static MappedData getRow(String customerId, String tableName, String id) {
		IdData customer = new IdData(customerId);
		String table = getTableName(customerId, tableName);
		IdData rowId = getRowIdData(table, id);
		return cassSource.getRow(customer, _APP, _TABLEID, rowId);
	}

	public static ListData getRowsWithFilter(String customerId, String tableName, MappedData filter) {
		IdData customer = new IdData(customerId);
		String table = getTableName(customerId, tableName);
		filter.put("table", new IdData(table));
		return cassSource.getRowsWithFilter(customer, _APP, _TABLEID, filter);
	}

	public static ListData getRows(String customerId, String tableName) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = getTableName(customerId, tableName);
		filter.put("table", new IdData(table));
		return cassSource.getRowsWithFilter(customer, _APP, _TABLEID, filter);
	}

	// UTIL FUNCTION
	public static String getTableName(String customerId, String resource) {
		return String.format("core:%s:public:%s", customerId, resource);
	}

	public static IdData getRowIdData(String tableName, String row) {
		return new IdData(String.format("%s:%s", tableName, row));
	}
}
