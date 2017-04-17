package com.wut.datasources.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.wut.model.Data;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.UserStore;
import com.wut.provider.table.TableProvider;
import com.wut.resources.storage.TableResource;

public class CassandraImportUser {
	// private static final IdData customer = new IdData("www.tend.ag");
	// private static final IdData customer = new IdData("demo.retailkit.com");
	private static UserStore userStore = new UserStore();
	private static final String appStr = "core";
	private static final String tendCustId = "tend";
	private static final String farmRows = "farms";
	private static final IdData app = new IdData(appStr);
	private static final IdData tableId = IdData.create("flat2");

	// @Test
	public static void main(String[] agrs) {

		/*
		 * List<String> allCustomers = getAllCustomers(); for (String customer :
		 * allCustomers) { System.out.println(customer);
		 * cloneAllUserCustomer(customer); }
		 */

/*		 cloneUser("www.mapiii.com");
		 cloneUser("test.farmer.guide");
		 cloneUser("www.farmer.events");*/
		/*CassandraSource cassSource = new CassandraSource();
		cassSource.createTable("flat2");
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create("test.farmer.guide"), IdData.create("core"), usersTable);
		System.out.println(listUsers);*/
		// System.out.println("deleting list users from " + tendCustId + "\t
		// size: " + listUsers.size());
		
		// get list user 
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create(tendCustId), IdData.create("core"), usersTable);
		System.out.println(listUsers);
		System.exit(0);

		// deleteTendUser();

		// CassandraSource cassSource = new CassandraSource();
		// System.out .println(cassSource.getAllRows(IdData.create(tendCustId),
		// IdData.create("users"), tableId));
		// cassSource.getAllRows(customer, application, tableId);

		/*
		 * Get specific user MappedData userData = (MappedData)
		 * (userStore.readSecureInformation("www.farmer.events", applicationStr,
		 * Authenticator.getUserId("www.farmer.events",
		 * "quan.nguyen@tend.ag"))); System.out.println(userData);
		 */
		// www.mapiii.com
		// test.farmer.guide
		// www.farmer.events
	}

	public static void cloneUser(String customerId) {
		String oldUserId = "";
		String tendUserId = "";
		List<String> listUsers = getListUsers(customerId);
		System.out.println("cloning from " + customerId + "\t size: " + listUsers.size());
		for (String username : listUsers) {
			tendUserId = Authenticator.getUserId(tendCustId, username);
			oldUserId = Authenticator.getUserId(customerId, username);
			System.out.println(oldUserId);

			// check if account imported to Tend
			MappedData userData = (MappedData) (userStore.readSecureInformation(tendCustId, appStr, tendUserId));
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				// already imported with another domain => update listFarm
				addFarm(userData, new StringData(username), new StringData(customerId));
				continue;
			}
			userData = (MappedData) userStore.readSecureInformation(customerId, appStr, oldUserId);
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				importUser(userData);
				continue;
			}
			System.out.println("skipped" + userData);
		}
	}

	public static void addFarm(MappedData userData, StringData username, StringData additionalCustomer) {
		// existed with another domain => update listDomain
		StringData farmIds = (StringData) userData.get(farmRows);
		IdData tendUserId = (IdData) userData.get("id");
		if (farmIds == null)
			farmIds = additionalCustomer;
		else {
			List<String> listFarmIds = Arrays.asList(farmIds.toRawString().split(","));
			listFarmIds = new ArrayList<String>(listFarmIds);
			System.out.println("listFarmIds: " + listFarmIds);
			if (listFarmIds.contains(additionalCustomer.toRawString()))
				return;
			listFarmIds.add(additionalCustomer.toRawString());
			farmIds = new StringData(StringUtils.join(listFarmIds, ","));
		}
		userData.put(farmRows, farmIds);

		System.out.println("update: " + userData);
		userStore.update(tendCustId, app.toRawString(), tendUserId.toRawString(), userData.getMapAsPojo());
	}

	public static void importUser(MappedData userData) {
		StringData username = (StringData) userData.get("username");
		System.out.println("Import: " + userData);
		StringData oldCustomer = (StringData) userData.get("customer");
		String tendUserId = Authenticator.getUserId(tendCustId, username.toRawString());
		// update the corresponde customer and listDomain
		userData.put("id", new IdData(tendUserId));
		userData.put("customer", new IdData(tendCustId));
		userData.put("password", "9e3e657b9979ccdca9039487152f7082");
		addFarm(userData, username, oldCustomer);
	}

	public static List<String> getListUsers(String customerId) {
		IdData customer = new IdData(customerId);
		List<String> emails = new ArrayList<String>();
		CassandraSource cassSource = new CassandraSource();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "user");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, app, tableId, filter);
		System.out.println(rowsWithFilter.size() + " users");
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			// System.out.println(row);
			email = row.get("id").toString().replaceAll(table + ":", "");
			emails.add(email);
		}
		return emails;
	}

	public static List<String> getAllCustomers() {
		IdData customer = new IdData("www.tend.ag");
		List<String> customers = new ArrayList<String>();
		CassandraSource cassSource = new CassandraSource();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", "www.tend.ag", "site");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, app, tableId, filter);
		System.out.println(rowsWithFilter.size() + " customers");
		String customerId = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}

	public static void deleteTendUser() {

		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create(tendCustId), IdData.create("core"), usersTable);
		System.out.println("deleting list users from " + tendCustId + "\t size: " + listUsers.size());
		for (Object item : listUsers) {
			MappedData user = (MappedData) item;
			// check if account imported
			Data delete = userStore.delete(tendCustId, appStr, user.get("id").toString());
			System.out.println(user.get("id").toString() + delete);
		}
	}

}
