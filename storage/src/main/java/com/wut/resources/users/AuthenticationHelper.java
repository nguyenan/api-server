package com.wut.resources.users;

import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.Token;
import com.wut.pipeline.User;
import com.wut.pipeline.UserStore;

public class AuthenticationHelper {
	private com.wut.pipeline.UserStore source;

	public AuthenticationHelper(CrudSource source) {
		this.source = (UserStore) source;
	}
	
//	// used by authenticator in processing pipeline
//	private User getUserObj(String customer, String username) {
//		String id = Authenticator.getUserId(customer, username);
//		MappedData userData = (MappedData) source.readSecureInformation(id);
//		// TODO handle missing users with a better error message
//		// TODO simulate this problem misspelling a customer name
//		String customerStr = userData.get("customer").toString();
//		String tokenStr = userData.get("token").toString();
//		String idStr = userData.get("id").toString();
//		String usernameStr = userData.get("username").toString();
//		User user = new User();
//		user.setCustomer(customerStr);
//		user.setId(idStr);
//		user.setUsername(usernameStr);
//		user.setToken(tokenStr);
//		return user;
//	}
	
//	private User getUserObj(MappedData userData) {
//		String customerStr = userData.get("customer").toString();
//		String tokenStr = userData.get("token").toString();
//		String idStr = userData.get("id").toString();
//		String usernameStr = userData.get("username").toString();
//		User user = new User();
//		user.setCustomer(customerStr);
//		user.setId(idStr);
//		user.setUsername(usernameStr);
//		user.setToken(tokenStr);
//		return user;
//	}

	
//	public Data authenticate(String customer, String username, String requestPassword) {
//		String id = Authenticator.getUserId(customer, username);
//		
//		MappedData credentials = (MappedData) source.readSecureInformation(id);
//		
//		if (credentials == null || credentials.get("password") == null) {
//			return MessageData.UNKNOWN_USER;
//		}
//
//		String actualPassword = credentials.get("password").toString();
//		if (requestPassword.equals(actualPassword)) {
//			return newToken(customer, username, requestPassword);
//		} else {
//			return ErrorData.INVALID_LOGIN;
//		}
//	}
	
//	public StringData newToken(String customer, String username, String password) {
//		StringData token = new StringData(Token.generateNewToken(username, password).getToken());
//		MappedData userData = new MappedData();
//		userData.put("customer", new StringData(customer));
//		userData.put("username", new StringData(username));
//		userData.put("password", new StringData(password));
//		String userId = Authenticator.getUserId(customer, username);
//		userData.put("id", new StringData(userId));
//		userData.put("token", token);
//		Map<String,String> userDataPojo = userData.getMapAsPojo();
//		source.update(userId, userDataPojo);
//		
//		User userObj = getUserObj(userData);
//		Authenticator.updateUser(userObj);
//		
//		return token;
//	}
}
