package user;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.support.settings.SystemSettings;

public class PermissionUnitTest {
	private static final String requestingCustomer = "test.api.farm";
	private static final String affectedCustomer = requestingCustomer;
	private static final String requestingUser = "admin2@test.api.farm";
	private static final String requestPassword = "db4a38e170d773744cfdc901ae790e22";
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");
	private static final String application = "core";

	// public static void main(String[] args) {
	// PermissionStore source = new PermissionStore();
	// String affectedUser = "admin2@test.api.farm";
	// String affectedUserId = Authenticator.getUserId(adminCustId,
	// affectedUser);
	//
	// MappedData permissionData = new MappedData();
	// permissionData.put(affectedCustomer, new StringData("owner"));
	// source.update(adminCustId, application, affectedUserId,
	// permissionData.getMapAsPojo());
	// }

	@Test
	public void testRead() {
		PermissionStore source = new PermissionStore();
		String affectedUser = "member1@tendtest.com";
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		// read Permission Data
		Data data = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		assertEquals(new StringData("crewmember"), data);
	}

	@Test
	public void testReadNoData() {
		PermissionStore source = new PermissionStore();
		String affectedUser = "admin@www.mapiii.com";
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		Data data = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		assertEquals(MessageData.NO_DATA_FOUND, data);
	}

	@Test
	public void testUpdate() {
		PermissionStore source = new PermissionStore();
		String affectedUser = "test" + System.currentTimeMillis() + "@tendtest.com";
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		// do update
		String role = "crewmember";
		MappedData permissionData = new MappedData();
		permissionData.put(affectedCustomer, new StringData(role));
		source.update(adminCustId, application, affectedUserId, permissionData.getMapAsPojo());

		Data read = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		assertEquals(new StringData("crewmember"), read);
	}

	@Test
	public void testUpdateExisted() {
		PermissionStore source = new PermissionStore();
		String affectedUser = "test" + System.currentTimeMillis() + "@tendtest.com";
		String application = "core";
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		// do update 1
		MappedData permissionData = new MappedData();
		permissionData.put(affectedCustomer, new StringData("crewmember"));
		source.update(adminCustId, application, affectedUserId, permissionData.getMapAsPojo());

		Data read1 = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		assertEquals(new StringData("crewmember"), read1);
		// do update 2
		permissionData = (MappedData) source.read(adminCustId, application, affectedUserId);
		permissionData.put(affectedCustomer, new StringData("admin"));
		source.update(adminCustId, application, affectedUserId, permissionData.getMapAsPojo());

		Data read2 = source.read(adminCustId, application, affectedUserId, affectedCustomer);
		assertEquals(new StringData("admin"), read2);
	}

	@Test
	public void testUpdateMultiple() {
		PermissionStore source = new PermissionStore();
		String affectedCustomer1 = affectedCustomer;
		String affectedCustomer2 = "test2.api.farm";
		String affectedUser = "test" + System.currentTimeMillis() + "@tendtest.com";
		String application = "core";
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);

		// do update
		MappedData permissionData = new MappedData();
		permissionData.put(affectedCustomer1, new StringData("admin"));
		source.update(adminCustId, application, affectedUserId, permissionData.getMapAsPojo());

		// do update
		permissionData = (MappedData) source.read(adminCustId, application, affectedUserId);
		permissionData.put(affectedCustomer2, new StringData("crewmember"));
		source.update(adminCustId, application, affectedUserId, permissionData.getMapAsPojo());

		Data read1 = source.read(adminCustId, application, affectedUserId, affectedCustomer1);
		assertEquals(new StringData("admin"), read1);

		Data read2 = source.read(adminCustId, application, affectedUserId, affectedCustomer2);
		assertEquals(new StringData("crewmember"), read2);
	}

}
