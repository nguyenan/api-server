package user;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.Token;
import com.wut.pipeline.UserStore;

public class TestUser {

	@Test
	public void testReadSecureInformation() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend.ag";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		assertNotNull(credentials);
		assertNotNull(credentials.get("username"));
		assertNotNull(credentials.get("password"));
		assertNotNull(credentials.get("token"));
	}

	@Test
	public void testReadSecureInformationNoData() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		Data credentials = source.readSecureInformation(customer, application, id);

		assertNotNull(credentials);
		assertEquals(MessageData.NO_DATA_FOUND, credentials);
	}

	@Test
	public void testRead() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend.ag";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.read(customer, application, id);

		assertNotNull(credentials);
		assertNotNull(credentials.get("username"));
		assertNull(credentials.get("password"));
		assertNull(credentials.get("token"));
	}

	@Test
	public void testReadNoData() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		Data credentials = source.read(customer, application, id);

		assertEquals(MessageData.NO_DATA_FOUND, credentials);
	}

	@Test
	public void testUpdate() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend.ag";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		StringData newToken = new StringData(Token.generateNewToken(username, username).getToken());
		credentials.put("token", newToken);
		Data update = source.update(customer, application, id, credentials.getMapAsPojo());
		assertEquals(BooleanData.TRUE, update);

		MappedData newCredentials = (MappedData) source.readSecureInformation(customer, application, id);
		assertEquals(newToken, newCredentials.get("token"));
	}

	@Test
	public void testUpdateNewUser() {
		String customer = "www.mapiii.com";
		String username = System.currentTimeMillis() + "@tend.ag";
		String application = "core";

		// Create
		String id = Authenticator.getUserId(customer, username);
		MappedData userMap = new MappedData();
		userMap.put("firstName", "");
		userMap.put("lastName", "");
		userMap.put("username", username);
		userMap.put("id", id);
		userMap.put("password", "465eaca53428a2109210c602f7bda1ae");
		userMap.put("token", new StringData(Token.generateNewToken(username, username).getToken()));
		userMap.put("customer", customer);

		UserStore source = new UserStore();
		Data update = source.update(customer, application, id, userMap.getMapAsPojo());
		assertEquals(BooleanData.TRUE, update);

		// Read
		MappedData credentials = (MappedData) source.read(customer, application, id);
		assertNotNull(credentials);
		assertEquals(new StringData(username), credentials.get("username"));

		Data delete = source.delete(customer, application, id);
		assertEquals(BooleanData.TRUE, delete);
	}

	@Test
	public void testDeleteUserNotFound() {
		String customer = "www.mapiii.com";
		String username = System.currentTimeMillis() + "@tend.ag";
		String application = "core";
		UserStore source = new UserStore();

		// Create
		String id = Authenticator.getUserId(customer, username);

		Data delete = source.delete(customer, application, id);
		assertEquals(BooleanData.TRUE, delete);
	}

	@Test
	public void testDeleteUser() {
		String customer = "www.mapiii.com";
		String username = System.currentTimeMillis() + "@tend.ag";
		String application = "core";

		// Create
		String id = Authenticator.getUserId(customer, username);
		MappedData userMap = new MappedData();
		userMap.put("firstName", "");
		userMap.put("lastName", "");
		userMap.put("username", username);
		userMap.put("id", id);
		userMap.put("password", "465eaca53428a2109210c602f7bda1ae");
		userMap.put("token", new StringData(Token.generateNewToken(username, username).getToken()));
		userMap.put("customer", customer);

		UserStore source = new UserStore();
		source.update(customer, application, id, userMap.getMapAsPojo());

		Data delete = source.delete(customer, application, id);
		assertEquals(BooleanData.TRUE, delete);

		// Read
		MappedData credentials = (MappedData) source.read(customer, application, id);
		assertEquals(MessageData.NO_DATA_FOUND, credentials);

	}
}