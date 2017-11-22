package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.support.settings.SettingsManager;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CustomerDevTools {
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	//
	private static final IdData tableId = IdData.create("flat2");

	private static Logger logger = Logger.getLogger("CustomerDevTools");
	private static FileHandler fh;

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/customer_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
		String parentCustomerId = "betaadmin.tend.ag";
		List<String> allCustomers = getAllCustomers(parentCustomerId);
		for (String customerId : allCustomers) {
			if (customerId.isEmpty())
				continue;
			String template = SettingsManager.getClientSettings(customerId, "template.git.branch");
			System.out.println(customerId + "\t" + template);
			//updateTemplate(parentCustomerId, customerId, template);
		}
		System.exit(0);
	}

	private static void cloneCustomer() {
		String oldCustomer = "admin.tend.ag";
		String newCustomer = "www.tend.ag";
		ListData allCustomers = getAllCustomersDetails(oldCustomer);
		for (Object obj : allCustomers) {
			MappedData row = (MappedData) obj;
			System.out.println(row);
			addNewCustomers(newCustomer, row.get("customer").toString(), row.get("updated").toString());
		}
	}

	public static MappedData getCustomerInfo(String parentCustomerId, String customerId) {

		String table = String.format("core:%s:public:%s", parentCustomerId, "site");
		String rowId = table + ":" + customerId;
		MappedData row = cassSource.getRow(new IdData(parentCustomerId), application, tableId, new IdData(rowId));
		return row;
	}

	public static BooleanData updateTemplate(String parentCustomerId, String customerId, String template) {

		String table = String.format("core:%s:public:%s", parentCustomerId, "site");
		String rowId = table + ":" + customerId;
		MappedData data = new MappedData();
		data.put("template", template);
		return cassSource.updateRow(new IdData(parentCustomerId), application, tableId, new IdData(rowId), data);

	}

	public static ListData getAllCustomersDetails(String parentCustomerId) {
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", parentCustomerId, "site");
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData(parentCustomerId), application, tableId,
				filter);
		logger.info(rowsWithFilter.size() + " customers");

		return rowsWithFilter;
	}

	public static List<String> getAllCustomers(String parentCustomerId) {
		List<String> customers = new ArrayList<String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", parentCustomerId, "site");
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData(parentCustomerId), application, tableId,
				filter);
		logger.info(rowsWithFilter.size() + " customers");
		String customerId = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}

	public static BooleanData addNewCustomers(String parentCustomerId, String customerId, String update) {
		String table = String.format("core:%s:public:%s", parentCustomerId, "site");
		String rowId = table + ":" + customerId;
		MappedData data = new MappedData();
		data.put("customer", customerId);
		data.put("environment", "production");
		data.put("id", rowId);
		data.put("table", table);
		data.put("primarySubscription", "");
		data.put("seats", "1000");
		data.put("updated", update);

		return cassSource.updateRow(new IdData(parentCustomerId), application, tableId, new IdData(rowId), data);
	}

	public static BooleanData addNewCustomers(String parentCustomerId, String customerId) {
		return addNewCustomers(parentCustomerId, parentCustomerId, String.valueOf(System.currentTimeMillis()));
	}

	public static MappedData getRowFrontendMapResourceTable(String customerId, Logger logger) {
		try {
			IdData customer = new IdData(customerId);
			MappedData filter = new MappedData();
			String table = String.format("core:%s:public:%s", customerId, "frontendMapResourceTable");
			filter.put("table", new IdData(table));
			MappedData row = cassSource.getRow(customer, application, tableId, new IdData(table + ":settings"));
			logger.info(row.toString());
			logger.info(String.format("%s\t%s\t%s", customerId, row.get("customerDomain"), row.get("domain")));

			return null;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}

	}

	public static MappedData updateFrontendMapResourceTable(String customerId, Logger logger) {
		try {
			IdData customer = new IdData(customerId);
			MappedData filter = new MappedData();
			String table = String.format("core:%s:public:%s", customerId, "frontendMapResourceTable");
			filter.put("table", new IdData(table));
			MappedData row = cassSource.getRow(customer, application, tableId, new IdData(table + ":settings"));
			row.put("domain", customerId);
			row.put("customerDomain", customerId);
			logger.info(row.toString());
			cassSource.updateRow(customer, application, tableId, new IdData(row.get("id").toString()), row);

			return null;
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info(String.format("%s", customerId));
			return null;
		}

	}

}
