package storage;

import com.wut.datasources.cassandra.CassandraSource;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.UserStore;

public class UserWrite {
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

		System.exit(0);
	}

	/**
	 * update Permission when login (list farms user have permission to access)
	 * 
	 * @param customerId
	 * @param email
	 * @param role
	 * @return
	 */
	public static BooleanData updateUserPermission(String customerId, String email, String role) {
		MappedData permissionData = (MappedData) permissionStore.read(_TEND_ADMIN, _APP_STR, email);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(customerId, new StringData(role));
		return (BooleanData) permissionStore.update(_TEND_ADMIN, _APP_STR, email, permissionData.getMapAsPojo());
	}

	/**
	 * Update user's role
	 * 
	 * @param customerId
	 * @param email
	 * @param role
	 *            owner, admin, manager, crewmember
	 * @return
	 */
	public static BooleanData updateUserRole(String customerId, String email, String role) {
		IdData customer = new IdData(customerId);
		MappedData filter = new MappedData();
		String table = String.format("core:%s:public:%s", customerId, _TBL_PERMISSION);
		IdData rowId = new IdData(table + ":" + email);
		filter.put("table", new IdData(table));

		MappedData row = cassSource.getRow(customer, _APP, _TABLEID, rowId);
		row.put("role", role);

		return (BooleanData) cassSource.updateRow(new IdData(customerId), _APP, _TABLEID, rowId, row);

	}

	/**
	 * remove user's Login info (can't login anymore)
	 * 
	 * @param email
	 * @return
	 */

	public static BooleanData deleteUserFromLogin(String email) {
		String userId = Authenticator.getUserId(_TEND_ADMIN, email);
		return (BooleanData) userStore.delete(_TEND_ADMIN, _APP_STR, userId);
	}

	/**
	 * Reset user's password
	 * 
	 * @param email
	 * @param encryptedpassword
	 *            new pwd must be encrypted by the MD5 function (by our js lib)
	 * @return
	 */
	public static BooleanData resetPassword(String email, String encryptedpassword) {
		String userId = Authenticator.getUserId(_TEND_ADMIN, email);
		MappedData userData = (MappedData) (userStore.readSecureInformation(_TEND_ADMIN, _APP_STR, userId));
		userData.put("password", new IdData(encryptedpassword));
		return (BooleanData) userStore.update(_TEND_ADMIN, _APP.toRawString(), userId, userData.getMapAsPojo());

	}

}
