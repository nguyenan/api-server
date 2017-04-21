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
import com.wut.provider.table.TableResourceProvider;
import com.wut.resources.storage.TableResource;

public class CassandraImportUser {
	private static TableResourceProvider provider = TableResource.getProvider();
	private static UserStore userStore = new UserStore();
	private static final String appStr = "core";
	private static final IdData app = new IdData(appStr);
	private static final String tendCustId = "tend";
	private static final String domainsTable = "domains";
	private static final IdData user = new IdData("public");
	private static final IdData tableId = IdData.create("flat2");

	public static void main(String[] agrs) {
		System.out.println(getListAdminUsers("beta.tend.ag"));
		// cloneUser("beta.tend.ag");
		// cloneUser("www.farmer.events");
		// cloneUser("www.mapiii.com");
		// listAdminUsers();
		System.exit(0);
	}

	public static void cloneUser(String customerId) {
		List<String> listUsers = getListAdminUsers(customerId);
		System.out.println("cloning from " + customerId + "\t size: " + listUsers.size());
		for (String username : listUsers) {
			// check if account imported to Tend
			String tendUserId = Authenticator.getUserId(tendCustId, username);
			MappedData userData = (MappedData) (userStore.readSecureInformation(tendCustId, appStr, tendUserId));
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				updateListDomain(username, customerId);
				continue;
			}

			String oldUserId = Authenticator.getUserId(customerId, username);
			userData = (MappedData) userStore.readSecureInformation(customerId, appStr, oldUserId);
			if (!userData.equals(MessageData.NO_DATA_FOUND)) {
				createAdminUser(userData);
				updateListDomain(username, customerId);
				continue;
			}
			System.out.println("skipped " + username);
		}
	}

	public static void updateListDomain(String username, String customer) {
		StringData domains = new StringData("");
		MappedData result = (MappedData) provider.read(app, new IdData(tendCustId), user, new IdData(domainsTable),
				new IdData(username), null);
		if (result.equals(MessageData.NO_DATA_FOUND))
			domains = new StringData(customer);
		else {
			StringData data = (StringData) result.get("value");
			List<String> listDomains = Arrays.asList(data.toRawString().split(","));
			listDomains = new ArrayList<String>(listDomains);
			if (listDomains.contains(customer))
				return;
			listDomains.add(customer);
			domains = new StringData(StringUtils.join(listDomains, ","));
		}

		MappedData customerValue = new MappedData();
		customerValue.put("value", domains);
		customerValue.put("id", username);
		provider.update(app, new IdData(tendCustId), user, new IdData(domainsTable), new IdData(username),
				customerValue);
	}

	public static void createAdminUser(MappedData userData) {
		StringData username = (StringData) userData.get("username");
		String tendUserId = Authenticator.getUserId(tendCustId, username.toRawString());
		// update corresponde customer
		userData.put("id", new IdData(tendUserId));
		userData.put("customer", new IdData(tendCustId));
		// userData.put("password", "9e3e657b9979ccdca9039487152f7082");
		System.out.println("Create: " + userData);
		userStore.update(tendCustId, app.toRawString(), tendUserId, userData.getMapAsPojo());
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

	public static List<String> getListAdminUsers(String customerId) {
		IdData customer = new IdData(customerId);
		List<String> emails = new ArrayList<String>();
		CassandraSource cassSource = new CassandraSource();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "permission");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, app, tableId, filter);
		System.out.println(rowsWithFilter.size() + " users");
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			System.out.println(row);
			email = "";
			Data admin = row.get("admin");
			Data role = row.get("role");
			if ((new StringData("true")).equals(admin)) {
				email = row.get("id").toString().replaceAll(table + ":", "");
			} else if ((new StringData("crewmember")).equals(role) 
					|| (new StringData("admin")).equals(role)
					|| (new StringData("manager")).equals(role) 
					|| (new StringData("owner")).equals(role)) {
				email = row.get("id").toString().replaceAll(table + ":", "");
			}
			if (!email.isEmpty())
				emails.add(email);
		}
		return emails;
	}

	public static void listAdminUsers() {
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create(tendCustId), IdData.create("core"), usersTable);
		System.out.println("listAdminUsers size: " + listUsers.size());
		for (Object item : listUsers) {
			System.out.println(item);
		}
	}

	public static void deleteAdminUser() {
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create(tendCustId), IdData.create("core"), usersTable);
		System.out.println("deleting list users from " + tendCustId + "\t size: " + listUsers.size());
		for (Object item : listUsers) {
			MappedData user = (MappedData) item;
			Data delete = userStore.delete(tendCustId, appStr, user.get("id").toString());
			System.out.println(user.get("id").toString() + delete);
		}
	}

	public static void getUserInfo(String customerId, String email) {
		MappedData userData = (MappedData) (userStore.readSecureInformation(customerId, appStr,
				Authenticator.getUserId(customerId, email)));
		System.out.println(userData);
	}

	/*
	 * CassandraSource cassSource = new CassandraSource();
	 * cassSource.createTable("flat2"); TableProvider table =
	 * TableResource.getTableProvider(); IdData usersTable = new
	 * IdData("users"); ListData listUsers =
	 * table.getRows(IdData.create("test.farmer.guide"), IdData.create("core"),
	 * usersTable); System.out.println(listUsers);
	 */

	// CassandraSource cassSource = new CassandraSource();
	// cassSource.getAllRows(IdData.create(tendCustId),
	// IdData.create("users"), tableId);
	// cassSource.getAllRows(customer, application, tableId);

}
