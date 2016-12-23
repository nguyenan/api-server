package com.wut;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.User;
import com.wut.pipeline.UserStore;
import com.wut.resources.users.AuthenticateUser;
import com.wut.resources.users.UsersResource;
import com.wut.resources.users.ValidateToken;

public class TestValidateToken {

	@Test
	public void testAuthenticate() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "b389d7ba1a7dc968c893d2c5823fc80e";
		String application = "core";

		// return authHelper.authenticate(customer, username, requestPassword);

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

		// return authHelper.authenticate(customer, username, requestPassword);

		String id = Authenticator.getUserId(customer, username);
		UserStore source = new UserStore();
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		assertNotNull(credentials);
		assertNotNull(credentials.get("password"));

		String actualPassword = credentials.get("password").toString();
		assertNotEquals("Authenticate must fail", actualPassword, requestPassword);

	}

	@Test
	public void testValidateTokenPublic() {
		String customer = "dev.retailkit.com";
		String username = "public";
		String token = "public";
		Authenticator authenticator = new Authenticator();
		boolean checkToken = authenticator.checkToken(customer, username, token);
		assertTrue("token public must pass", checkToken);
	}

	@Test
	public void testValidateTokenPublicFail() {
		String customer = "dev.retailkit.com";
		String username = "public";
		String token = "b389d7ba1a7dc968c893d2c5823fc80e";
		Authenticator authenticator = new Authenticator();
		boolean checkToken = authenticator.checkToken(customer, username, token);
		assertFalse("token public must fail", checkToken);
	}

	@Test
	public void testValidateToken() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String requestPassword = "b389d7ba1a7dc968c893d2c5823fc80e";
		UsersResource usersResourceInts = new UsersResource();
		AuthenticateUser authenticateOperation = usersResourceInts.getAuthenticateOperation();
		StringData newToken = authenticateOperation.newToken(customer, username, requestPassword);
		assertNotNull(newToken);

		Authenticator authenticator = new Authenticator();
		User userInfo = authenticator.validateToken(customer, username, newToken.toString());
		System.out.println("userInfo: " + userInfo.toString());
		assertEquals("token must pass", username, userInfo.getUsername());
	}

	@Test
	public void testValidateTokenFail() {
		String customer = "dev.retailkit.com";
		String username = "an.nguyenhoang@tend.ag";
		String newToken = "t000251u8067a75";

		Authenticator authenticator = new Authenticator();
		User userInfo = authenticator.validateToken(customer, username, newToken);
		assertNull("token must fail", userInfo);
//		System.out.println(newToken.toString());

	}
}
