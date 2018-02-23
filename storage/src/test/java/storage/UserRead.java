package storage;

import java.util.HashMap;
import java.util.Map;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.UserStore;

public class UserRead {
	// CONST
	private static final String _APP_STR = "core";
	private static final IdData _APP = new IdData(_APP_STR);
	private static final IdData _TABLEID = IdData.create("flat2");

	private static final String _TEND_ADMIN = "admin.tend.ag";
	private static final String _TBL_PERMISSION = "permission";

	// TABLE SOURCE
	private static CassandraSource cassSource = new CassandraSource();
	private static UserStore userStore = new UserStore();
	private static PermissionStore permissionStore = new PermissionStore();

	public static void main(String[] agrs) {

		System.out.println(getUserInfo("an.nguyenhoang@spiraledge.com"));
		System.out.println(getStorefrontUserInfo("www.trilliumfarmwa.com", "an.nguyenhoang@tend.ag"));
		System.out.println(getUserPermissions("an.nguyenhoang@spiraledge.com"));
		System.out.println(getUserRole("www.mapiii.com", "an.nguyenhoang@spiraledge.com"));
		System.out.println(listAdminUsers("www.mapiii.com"));

		System.exit(0);
	}

	/**
	 * get Login info (password, token,...) of an admin user
	 * 
	 * @param email
	 * @return
	 */
	private static MappedData getUserInfo(String email) {
		String userId = Authenticator.getUserId(_TEND_ADMIN, email);
		return (MappedData) (userStore.readSecureInformation(_TEND_ADMIN, _APP_STR, userId));
	}

	/**
	 * get Login info (password, token,...) of a storefront user
	 * 
	 * @param customerId
	 * @param email
	 * @return
	 */
	private static MappedData getStorefrontUserInfo(String customerId, String email) {
		String userId = Authenticator.getUserId(customerId, email);
		return (MappedData) (userStore.readSecureInformation(customerId, _APP_STR, userId));
	}

	/**
	 * get Permission when login (list farms user have permission to access)
	 * 
	 * @param email
	 * @return
	 */
	private static MappedData getUserPermissions(String email) {
		String userId = Authenticator.getUserId(_TEND_ADMIN, email);
		return (MappedData) (permissionStore.read(_TEND_ADMIN, _APP_STR, userId));
	}

	/**
	 * get user's role with a specific farm
	 * 
	 * @param customerId
	 * @param email
	 * @return
	 */
	private static MappedData getUserRole(String customerId, String email) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, _TBL_PERMISSION);
		filter.put("table", new IdData(table));

		return (MappedData) cassSource.getRow(customer, _APP, _TABLEID, new IdData(table + ":" + email));
	}

	/**
	 * list Admin users of farm (based on permission table)
	 * 
	 * @param customerId
	 * @return
	 */
	private static Map<String, String> listAdminUsers(String customerId) {
		IdData customer = new IdData(customerId);
		Map<String, String> admins = new HashMap<String, String>();
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, _TBL_PERMISSION);
		filter.put("table", new IdData(table));

		ListData rowsWithFilter = cassSource.getRowsWithFilter(customer, _APP, _TABLEID, filter);
		String email = "";
		for (Object obj : rowsWithFilter) {
			MappedData row = (MappedData) obj;
			if (row.get("role") != null) {
				email = row.get("id").toString().replaceAll(table + ":", "");
				admins.put(email, row.get("role").toString());
			}
		}
		return admins;
	}
}
