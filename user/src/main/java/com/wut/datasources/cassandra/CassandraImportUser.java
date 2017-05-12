package com.wut.datasources.cassandra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.model.Data;
import com.wut.model.PermissionRole;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.UserStore;
import com.wut.provider.table.TableProvider;
import com.wut.resources.storage.TableResource;
import com.wut.support.settings.SystemSettings;

public class CassandraImportUser {
	private static UserStore userStore = new UserStore();
	private static PermissionStore permissionStore = new PermissionStore();
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");
	private static final IdData tableId = IdData.create("flat2");

	public static void main(String[] agrs) {
		// cloneAllCustomer();
		cloneAdminAccounts("test.an.farmer.guide");
		// System.out.println(getListAdminUsers(adminCustId));
		//getUserInfo(adminCustId, "an.nguyen.hoang@tendtest.com");
		System.exit(0);
	}

	public static void cloneAllCustomer() {
		List<String> allCustomers = getAllCustomers();
		for (String customerId : allCustomers) {
			cloneAdminAccounts(customerId);
		}
	}

	public static void cloneAdminAccounts(String customerId) {
		Map<String, String> listUsers = getListAdminUsers(customerId);
		System.out.println("cloning from " + customerId + "\t size: " + listUsers.size());

		String username = "";
		String role = "";
		String adminUserId = "";
		String oldUserId = "";
		for (Map.Entry<String, String> entry : listUsers.entrySet()) {

			role = entry.getValue();
			username = entry.getKey();
			adminUserId = Authenticator.getUserId(adminCustId, username);

			// check if account was imported
			MappedData userData = (MappedData) (userStore.readSecureInformation(adminCustId, applicationStr,
					adminUserId));
			if (userData.equals(MessageData.NO_DATA_FOUND)) {
				oldUserId = Authenticator.getUserId(customerId, username);
				userData = (MappedData) userStore.readSecureInformation(customerId, applicationStr, oldUserId);
				if (userData.equals(MessageData.NO_DATA_FOUND)) {
					System.out.println("skipped " + username);
					continue;
				}
				createAdminUser(userData);
			}
			updatePermission(adminUserId, customerId, role);
		}
	}

	public static void updatePermission(String affectedUserId, String affectedCustomer, String role) {
		MappedData permissionData = (MappedData) permissionStore.read(adminCustId, applicationStr, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(affectedCustomer, new StringData(role));

		permissionStore.update(adminCustId, applicationStr, affectedUserId, permissionData.getMapAsPojo());
	}

	public static void createAdminUser(MappedData userData) {
		StringData username = (StringData) userData.get("username");
		String adminUserId = Authenticator.getUserId(adminCustId, username.toRawString());

		// update corresponde customer
		System.out.println("Create: " + userData);
		userData.put("id", new IdData(adminUserId));
		userData.put("customer", new IdData(adminCustId));
		userStore.update(adminCustId, application.toRawString(), adminUserId, userData.getMapAsPojo());
	}

	public static List<String> getAllCustomers() {
		List<String> customers = new ArrayList<String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", "www.tend.ag", "site");
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(new IdData("www.tend.ag"), application, tableId, filter);
		System.out.println(rowsWithFilter.size() + " customers");
		String customerId = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			customerId = row.get("id").toString().replaceAll(table + ":", "");
			customers.add(customerId);
		}
		return customers;
	}

	public static Map<String, String> getListAdminUsers(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "permission");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		System.out.println(rowsWithFilter.size() + " users");
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			System.out.println(row);
			email = "";
			Data admin = row.get("admin");
			Data role = row.get("role");
			email = row.get("id").toString().replaceAll(table + ":", "");
			if ((new StringData("true")).equals(admin)) {
				admins.put(email, "admin");
			}
			if (role != null && PermissionRole.contains(role.toString())) {
				admins.put(email, role.toString());
			}
		}
		return admins;
	}

	public static void deleteTendAdminUser() {
		TableProvider table = TableResource.getTableProvider();
		IdData usersTable = new IdData("users");
		ListData listUsers = table.getRows(IdData.create(adminCustId), IdData.create("core"), usersTable);
		System.out.println("deleting list users from " + adminCustId + "\t size: " + listUsers.size());
		for (Object item : listUsers) {
			MappedData user = (MappedData) item;
			Data delete = userStore.delete(adminCustId, applicationStr, user.get("id").toString());
			System.out.println(user.get("id").toString() + delete);
		}
	}

	public static void getUserInfo(String customerId, String email) {
		MappedData userData = (MappedData) (userStore.readSecureInformation(customerId, applicationStr,
				Authenticator.getUserId(customerId, email)));
		System.out.println(userData);
	}

	/*
	 * public static void listAdminUsers() { TableProvider table =
	 * TableResource.getTableProvider(); IdData usersTable = new
	 * IdData("users"); ListData listUsers =
	 * table.getRows(IdData.create(tendCustId), IdData.create("core"),
	 * usersTable); System.out.println("listAdminUsers size: " +
	 * listUsers.size()); for (Object item : listUsers) {
	 * System.out.println(item); } } public static void main2(String[] agrs) {
	 * System.out.println(getListAdminUsers("beta.tend.ag")); //
	 * cloneUser("beta.tend.ag"); // cloneUser("www.farmer.events"); //
	 * cloneUser("www.mapiii.com"); // listAdminUsers(); System.exit(0); }
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
