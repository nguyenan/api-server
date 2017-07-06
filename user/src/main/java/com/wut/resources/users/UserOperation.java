package com.wut.resources.users;

import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.Token;
import com.wut.pipeline.User;
import com.wut.pipeline.UserStore;
import com.wut.resources.operations.ParameteredOperation;

public abstract class UserOperation extends ParameteredOperation {
	private static final String APPLICATION = "core";
	protected UserStore source;
	
	public UserOperation(CrudSource source) {
		this.source = (UserStore) source;
	}
	
	
	public StringData newToken(String customer, String username, String password, boolean updateData) {
		StringData token = new StringData(Token.generateNewToken(username, password).getToken());
		MappedData userData = new MappedData();
		userData.put("customer", new StringData(customer));
		userData.put("username", new StringData(username));
		userData.put("password", new StringData(password));
		String userId = Authenticator.getUserId(customer, username);
		userData.put("id", new StringData(userId));
		userData.put("token", token);
		Map<String,String> userDataPojo = userData.getMapAsPojo();
		if (updateData) source.update(customer, APPLICATION, userId, userDataPojo);
		
		User userObj = getUserObj(userData);
		Authenticator.updateUser(userObj);
		
		return token;
	}
	
	private User getUserObj(MappedData userData) {
		String customerStr = userData.get("customer").toString();
		String tokenStr = userData.get("token").toString();
		String idStr = userData.get("id").toString();
		String usernameStr = userData.get("username").toString();
		User user = new User();
		user.setCustomer(customerStr);
		user.setId(idStr);
		user.setUsername(usernameStr);
		user.setToken(tokenStr);
		return user;
	}
	
}
