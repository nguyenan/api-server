package user;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.UserStore;
import com.wut.resources.users.AuthenticateUser;
import com.wut.resources.users.UsersResource;

public class TestAuthenticate {

	@Test
	public void testAuthenticate() {
		String customer = "dev1.tend.ag";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "465eaca53428a2109210c602f7bda1ae";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		assertNotNull(credentials);
		assertNotNull(credentials.get("password"));

		String actualPassword = credentials.get("password").toString();
		assertEquals("Authenticate fail", actualPassword, requestPassword);
	}

	@Test
	public void testAuthenticateFail() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "b389d7ba1a7dc968c893d2c5823fc80f";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		assertNotNull(credentials);
		assertNotNull(credentials.get("password"));

		String actualPassword = credentials.get("password").toString();
		assertNotEquals("Authenticate must fail", actualPassword, requestPassword);
	}

	@Test
	public void testAuthenticateNotFound() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend";
		String application = "core";

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		assertEquals(MessageData.NO_DATA_FOUND, credentials);
	}

	@Test
	public void testValidateToken() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "465eaca53428a2109210c602f7bda1ae";
		UsersResource usersResourceInts = new UsersResource();
		AuthenticateUser authenticateOperation = usersResourceInts.getAuthenticateOperation();
		StringData newToken = authenticateOperation.newToken(customer, username, requestPassword);
		assertNotNull(newToken);

		Authenticator authenticator = new Authenticator();
		boolean validateToken = authenticator.validateToken(customer, username, newToken.toString());
		assertTrue(validateToken);
	}

	@Test
	public void testValidateTokenWrongUser() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "465eaca53428a2109210c602f7bda1ae";
		UsersResource usersResourceInts = new UsersResource();
		AuthenticateUser authenticateOperation = usersResourceInts.getAuthenticateOperation();
		StringData newToken = authenticateOperation.newToken(customer, username, requestPassword);
		assertNotNull(newToken);

		Authenticator authenticator = new Authenticator();
		boolean validateToken = authenticator.validateToken(customer, "an.nguyenhoang@tend", newToken.toString());
		assertFalse(validateToken);
	}

	@Test
	public void testValidateTokenFail() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String newToken = "t000728.8367a75";

		Authenticator authenticator = new Authenticator();
		boolean validateToken = authenticator.validateToken(customer, username, newToken);
		assertFalse(validateToken);
	}

	@Test
	public void testValidateTokenNull() {
		String customer = "dev.retailkit.com";
		String username = null;
		String newToken = "t000728.8367a75";

		Authenticator authenticator = new Authenticator();
		boolean validateToken = authenticator.validateToken(customer, username, newToken);
		assertFalse(validateToken);
	}
}