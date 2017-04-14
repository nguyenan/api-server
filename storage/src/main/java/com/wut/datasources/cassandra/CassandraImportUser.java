package com.wut.datasources.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wut.model.Data;

//import org.junit.Test;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.UserStore;

public class CassandraImportUser {
	// private static final IdData customer = new IdData("www.tend.ag");
	// private static final IdData customer = new IdData("demo.retailkit.com");
	private static UserStore userStore = new UserStore();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final IdData tableId = new IdData("flat2");

	// @Test
	public static void main(String[] agrs) {
		// List<String> allCustomers = getAllCustomers();
		// for (String customer : allCustomers) {
		// System.out.println(customer);
		// cloneAllUserCustomer(customer);
		// }
		cloneAllUserCustomer("www.mapiii.com");
		cloneAllUserCustomer("test.farmer.guide");
		cloneAllUserCustomer("www.farmer.events");
		// getAllUsers("tend");
		/*
		 * CassandraSource cassSource = new CassandraSource();
		 * System.out.println(cassSource.getAllRows(new
		 * IdData("www.mapiii.com"), application, tableId));
		 */

		// MappedData userData = (MappedData)
		// (userStore.readSecureInformation("www.farmer.events", applicationStr,
		// Authenticator.getUserId("www.farmer.events",
		// "quan.nguyen@tend.ag")));
		// System.out.println(userData);
		// www.mapiii.com
		// test.farmer.guide
		// www.farmer.events
		// cassSource.getAllRows(customer, application, tableId);
	}

	public static void cloneAllUserCustomer(String customerId) {
		String oldUserId = "";
		String newUserId = "";
		List<String> allUsers = getAllUsers(customerId);
		System.out.println("cloning from " + customerId + "\t size: " + allUsers.size());
		for (String user : allUsers) {
			newUserId = Authenticator.getUserId("tend", user);
			oldUserId = Authenticator.getUserId(customerId, user);
			System.out.println(oldUserId);

			// check if account imported
			MappedData userData = (MappedData) (userStore.readSecureInformation("tend", applicationStr, newUserId));
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				// existed with another domain => update listDomain
				updateUser(newUserId, userData, customerId);
				continue;
			}
			userData = (MappedData) userStore.readSecureInformation(customerId, applicationStr, oldUserId);
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				importUser("tend", userData);
				continue;
			}
			System.out.println("skipped" + userData);
		}
	}

	public static void updateUser(String userId, MappedData userData, String additionalCustomer) {
		// existed with another domain => update listDomain
		StringData listDomains = (StringData) userData.get("farm");

		if (listDomains == null)
			listDomains = new StringData("," + additionalCustomer + ",");
		else {
			if (!listDomains.toRawString().contains("," + additionalCustomer + ","))
				listDomains.fromRawString(listDomains.toRawString() +  additionalCustomer + ",");
		}
		
		userData.put("farm", listDomains);
		userData.put("password", "9e3e657b9979ccdca9039487152f7082");
		System.out.println("Update: " + userData);
		userStore.update("tend", application.toRawString(), userId, userData.getMapAsPojo());
	}

	public static void importUser(String customerId, MappedData userData) {
		System.out.println("Import: " + userData);
		StringData username = (StringData) userData.get("username");
		String userId = Authenticator.getUserId(customerId, username.toRawString());

		// update the corresponde customer and listDomain
		userData.put("id", new StringData(userId));
		userData.put("customer", new StringData(customerId));
		userData.put("farm", new StringData("," + customerId + ","));
		userData.put("password", "9e3e657b9979ccdca9039487152f7082");

		Map<String, String> userDataPojo = userData.getMapAsPojo();
		Data update = userStore.update(customerId, application.toRawString(), userId, userDataPojo);

		// Track how many duplicated user
		System.out.println(String.format("clonned\t%s\t%s", userData.get("customer"), username.toRawString()));
	}

	public static List<String> getAllUsers(String customerId) {
		IdData customer = new IdData(customerId);
		List<String> emails = new ArrayList<String>();
		CassandraSource cassSource = new CassandraSource();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "user");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		System.out.println(rowsWithFilter.size() + " users");
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			System.out.println(row);
			String email = row.get("id").toString().replaceAll(table + ":", "");
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
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		System.out.println(rowsWithFilter.size() + " customers");
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			String customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}

}
