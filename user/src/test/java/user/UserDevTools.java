package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.Data;
//import com.wut.model.PermissionRole;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.UserStore;

public class UserDevTools {
	private static UserStore userStore = new UserStore();
	private static PermissionStore permissionStore = new PermissionStore();
	private static CassandraSource cassSource = new CassandraSource();
	private static final String applicationStr = "core";
	private static final IdData application = new IdData(applicationStr);
	//
	private static final String adminCustId = "admin.tend.ag";
	private static final IdData tableId = IdData.create("flat2");

	private static Logger logger = Logger.getLogger("UserDevTools");
	private static FileHandler fh;

	public static void main(String[] agrs) throws InterruptedException, SecurityException, IOException {
		fh = new FileHandler("/data/scripts/devlog/user_" + System.currentTimeMillis());
		logger.addHandler(fh);
		fh.setFormatter(new LogFormatter());
		List<String> customerIds = new ArrayList<String>();
//		customerIds.add("www.everlasting-garden.com");
//		customerIds.add("www.groundswellfarmsantacruz.com");
//		customerIds.add("www.hawkshaven.farm");
//		customerIds.add("www.lincolnhillsfarm.com");
//		customerIds.add("www.oldhousefarm.net");
//		customerIds.add("www.redearthgardens.com");
//		customerIds.add("www.roosterridgefarmaptos.com");
//		customerIds.add("www.cluckandtrowel.com");
//		customerIds.add("www.escargrowfarms.com");
//		customerIds.add("www.jackiesroots.com");
//		customerIds.add("www.lockewoodacres.com");
//		customerIds.add("www.lolasonoma.com");
//		customerIds.add("www.mahoniagardens.com");
//		customerIds.add("www.strong.ag");
//		customerIds.add("www.tend.ag");
//		customerIds.add("www.thepeachjamboree.farm ");
//		customerIds.add("www.trilliumfarmwa.com");
//		customerIds.add("demo.tend.ag");
//
//		for (String customerId : customerIds) {
//			MappedData userInfo = getUserInfo(customerId, "admin@" + customerId);
//			if (userInfo.equals(MessageData.NO_DATA_FOUND))
//				System.out.println(customerId + "\t" + "no-admin");
//			else
//				System.out.println(customerId + "\t" + userInfo.get("username"));
//			Map<String, String> listAdminUsers = getListAdminUsers(customerId);
//			for (Map.Entry<String, String> entry : listAdminUsers.entrySet()) {
//				System.out.println(customerId + "\t" + entry.getKey());
//			}
//		}
		String username = "robert@tend.ag";
//		permissionStore.delete(adminCustId, applicationStr, Authenticator.getUserId(adminCustId, username));
//		getUserPermission(adminCustId, "robert@tend.ag");
		updateTendPermission("www.tend.ag",  Authenticator.getUserId(adminCustId, username), "admin");
		getUserPermission(adminCustId, "robert@tend.ag");

		System.exit(0);
	}

	public static void getAllUserAuthen(String customerId) {
		Data userData = (userStore.readRows(customerId, applicationStr));
		logger.info(userData.toString());
	}

	public static void migrateTendUsers(String customerId, int count) {
		Map<String, String> listUsers = getListAdminUsers(customerId);
		logger.info("#" + count + " " + customerId + "\t size: " + listUsers.size());

		String username = "";
		String role = "";
		String adminUserId = "";
		String oldUserId = "";
		for (Map.Entry<String, String> entry : listUsers.entrySet()) {

			role = entry.getValue();
			username = entry.getKey();
			adminUserId = Authenticator.getUserId(adminCustId, username);

			if (username.startsWith("l1s1")) {
				logger.info("skipped 2 " + username);
				continue;
			}
			// check if account was imported
			MappedData userData = (MappedData) (userStore.readSecureInformation(adminCustId, applicationStr,
					adminUserId));
			if (userData.equals(MessageData.NO_DATA_FOUND)) {
				oldUserId = Authenticator.getUserId(customerId, username);
				userData = (MappedData) userStore.readSecureInformation(customerId, applicationStr, oldUserId);
				if (userData.equals(MessageData.NO_DATA_FOUND)) {
					logger.info("skipped " + username);
					continue;
				}
				cloneUserWithNewCustomer(userData, adminCustId);
			}
			updateTendPermission(customerId, adminUserId, role);
		}
	}

	public static void cloneUserWithNewCustomer(MappedData userData, String toCustomerId) {
		StringData username = (StringData) userData.get("username");
		String newUserId = Authenticator.getUserId(toCustomerId, username.toRawString());

		// update corresponde customer
		logger.info("added " + userData);
		userData.put("id", new IdData(newUserId));
		userData.put("customer", new IdData(toCustomerId));
		userStore.update(toCustomerId, application.toRawString(), newUserId, userData.getMapAsPojo());
	}

	public static void updateTendPermission(String affectedCustomerId, String affectedUserId, String role) {
		MappedData permissionData = (MappedData) permissionStore.read(adminCustId, applicationStr, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(affectedCustomerId, new StringData(role));

		permissionStore.update(adminCustId, applicationStr, affectedUserId, permissionData.getMapAsPojo());
		logger.info("permission " + affectedUserId);
	}

	public static Map<String, String> getListAdminUsersWithContact(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "user");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		// logger.info("\n" + customerId + "\t" + rowsWithFilter.size());
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = "";
			StringData contact = (StringData) row.get("contact");
			email = row.get("id").toString().replaceAll(table + ":", "");

			admins.put(email, contact == null ? "null"
					: contact.toString().replaceAll("\\[\\\\\"", "").replaceAll("\\\\\"\\]", ""));

			// System.out.println(String.format("%s\t%s", email, contact == null
			// ? "null"
			// : contact.toString().replaceAll("\\[\\\\\"",
			// "").replaceAll("\\\\\"\\]", "")));
		}
		return admins;
	}

	public static String getContact(String customerId, String contactId) {
		IdData customer = new IdData(customerId);
		String table = String.format("core:%s:public:%s", customerId, "contact");
		MappedData row = cassSource.getRow(customer, application, tableId, new IdData(table + ":" + contactId));
		String email = "";
		StringData firstName = new StringData("");
		StringData lastName = new StringData("");
		if (row == null)
			return "null";
		email = row.get("id").toString().replaceAll(table + ":", "");
		firstName = (StringData) row.get("firstName");
		lastName = (StringData) row.get("lastName");
		/*
		 * admins.put(email, (firstName == null ? "null" : firstName.toString())
		 * + "-" + (lastName == null ? "null" : lastName.toString()));
		 */
		return (String.format("%s\t%s\t%s", email, firstName == null ? "null" : firstName.toString(),
				lastName == null ? "null" : lastName.toString()));
	}

	public static Map<String, String> getContact(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "contact");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
		logger.info("\n" + customerId + "\t" + rowsWithFilter.size());
		String email = "";
		StringData firstName = new StringData("");
		StringData lastName = new StringData("");
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = row.get("id").toString().replaceAll(table + ":", "");
			firstName = (StringData) row.get("firstName");
			lastName = (StringData) row.get("lastName");
			/*
			 * admins.put(email, (firstName == null ? "null" :
			 * firstName.toString()) + "-" + (lastName == null ? "null" :
			 * lastName.toString()));
			 */
			System.out.println(String.format("%s\t%s\t%s", email, firstName == null ? "null" : firstName.toString(),
					lastName == null ? "null" : lastName.toString()));
		}
		return admins;
	}

	public static Map<String, String> getListAdminUsers(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, "permission");
		filter.put("table", new IdData(table));
		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, application, tableId, filter);
//		logger.info("\n" + customerId + "\t" + rowsWithFilter.size() + " users have permission");
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			email = "";
			Data admin = row.get("admin");
			Data role = row.get("role");
			email = row.get("id").toString().replaceAll(table + ":", "");
			if ((new StringData("true")).equals(admin)) {
				admins.put(email, "admin");
			}
			/*
			 * if (role != null && PermissionRole.contains(role.toString())) {
			 * admins.put(email, role.toString()); }
			 */
		}
		return admins;
	}

	public static void deleteUser(String customerId, String username) {
		String userId = Authenticator.getUserId(customerId, username);

		Data delete = userStore.delete(customerId, applicationStr, userId);
		logger.info(username + ": " + delete);
	}

	public static MappedData getUserInfo(String customerId, String username) {
		String userId = Authenticator.getUserId(customerId, username);

		return (MappedData) (userStore.readSecureInformation(customerId, applicationStr, userId));
	}

	public static void getUserPermission(String customerId, String username) {
		System.out.println(
				permissionStore.read(adminCustId, applicationStr, Authenticator.getUserId(adminCustId, username)));
	}

	public static void resetPassword(String customerId, String username, String encryptedpassword) {
		String userId = Authenticator.getUserId(customerId, username);

		MappedData userData = (MappedData) (userStore.readSecureInformation(customerId, applicationStr, userId));
		// update password
		userData.put("password", new IdData(encryptedpassword));
		userStore.update(customerId, application.toRawString(), userId, userData.getMapAsPojo());
		getUserInfo(customerId, username);
	}
}
