package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

public class AuthenticateUser extends UserOperation {
	//private AuthenticationHelper authHelper;
	
	public AuthenticateUser(CrudSource source) {
		super(source);
	}
	
	@Override
	public String getName() {
		return "authenticate";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String requestPassword = ri.getStringParameter("password");
		String application = ri.getApplication();
				
		//return authHelper.authenticate(customer, username, requestPassword);
		
		String id = Authenticator.getUserId(customer, username);
		
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);
		
		if (credentials == null || credentials.get("password") == null) {
			return MessageData.UNKNOWN_USER;
		}

		String actualPassword = credentials.get("password").toString();
		if (requestPassword.equals(actualPassword)) {
			return newToken(customer, username, requestPassword);
		} else {
			return ErrorData.INVALID_LOGIN;
		}
		
	}
	
}

