package storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class CustomerDevTools {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String _APP_STR = "core";
	private static final IdData _APP = new IdData(_APP_STR);
	//
	private static final IdData _TABLEID = IdData.create("flat2");

	private static final String _TBL_SITE = "site";
	private static final String _TBL_SETTINGS = "settings";
	private static final String _TBL_MAP = "frontendMapResourceTable";

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
 
		System.out.println(getSettings("dev2.tend.ag"));
		System.exit(0);
	}

	public static void cloneCustomer(String oldCustomer, String newCustomer) {
		ListData allCustomers = listAllCustomersDetails(oldCustomer);
		for (Object obj : allCustomers) {
			MappedData row = (MappedData) obj;
			System.out.println(row);
			addNewCustomers(newCustomer, row.get("customer").toString(), row.get("updated").toString());
		}
	}

	public static BooleanData addNewCustomers(String parentCustomerId, String customerId, String update) {
		String table = String.format("core:%s:public:%s", parentCustomerId, _TBL_SITE);
		String rowId = table + ":" + customerId;
		MappedData data = new MappedData();
		data.put("customer", customerId);
		data.put("environment", "production");
		data.put("id", rowId);
		data.put("table", table);
		data.put("primarySubscription", "");
		data.put("seats", "1000");
		data.put("updated", update);

		return cassSource.updateRow(new IdData(parentCustomerId), _APP, _TABLEID, new IdData(rowId), data);
	}

	public static BooleanData addNewCustomers(String parentCustomerId, String customerId) {
		return addNewCustomers(parentCustomerId, parentCustomerId, String.valueOf(System.currentTimeMillis()));
	}

	public static MappedData getCustomerInfo(String parentCustomerId, String customerId) {

		String table = String.format("core:%s:public:%s", parentCustomerId, _TBL_SITE);
		IdData rowId = new IdData(table + ":" + customerId);
		MappedData row = cassSource.getRow(new IdData(parentCustomerId), _APP, _TABLEID, rowId);
		return row;
	}

	public static ListData getSettings(String customerId) {
		try {
			IdData customer = new IdData(customerId);
			MappedData filter = new MappedData();
			String table = String.format("core:%s:public:%s", customerId, _TBL_SETTINGS);
			filter.put("table", new IdData(table));

			ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, _APP, _TABLEID, filter);

			return rowsWithFilter;
		} catch (Exception e) {
			return null;
		}

	}

	public static MappedData getRowFrontendMapResourceTable(String customerId, Logger logger) {
		try {
			IdData customer = new IdData(customerId);
			MappedData filter = new MappedData();
			String table = String.format("core:%s:public:%s", customerId, _TBL_MAP);
			filter.put("table", new IdData(table));
			return cassSource.getRow(customer, _APP, _TABLEID, new IdData(table + ":settings"));

		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}

	}

	public static BooleanData updateFrontendMapResourceTable(String customerId, Logger logger) {
		try {
			IdData customer = new IdData(customerId);
			MappedData filter = new MappedData();
			String table = String.format("core:%s:public:%s", customerId, _TBL_MAP);
			filter.put("table", new IdData(table));
			MappedData row = cassSource.getRow(customer, _APP, _TABLEID, new IdData(table + ":settings"));
			row.put("domain", customerId);
			row.put("customerDomain", customerId);
			logger.info(row.toString());
			return cassSource.updateRow(customer, _APP, _TABLEID, new IdData(row.get("id").toString()), row);

		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info(String.format("%s", customerId));
			return null;
		}

	}

	public static ListData listAllCustomersDetails(String parentCustomerId) {
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", parentCustomerId,_TBL_SITE);
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData(parentCustomerId), _APP, _TABLEID, filter);

		return rowsWithFilter;
	}

	public static List<String> listAllCustomers(String parentCustomerId) {
		List<String> customers = new ArrayList<String>();
		String table = String.format("core:%s:public:%s", parentCustomerId, _TBL_SITE);
		ListData rowsWithFilter = listAllCustomersDetails(parentCustomerId);

		String customerId = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}

}
