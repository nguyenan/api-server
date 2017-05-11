package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

// TODO I dont think this operation is used currently
public class UpdateUserOperation extends UserOperation {
	
	public UpdateUserOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String password = ri.getStringParameter("password");
		String id = Authenticator.getUserId(customer, username);
		
		// only admin users are allowed to create new users
		// TODO bring this back!!!
//		if (!ri.getUser().getUsername().equals("admin")) {
//			return MessageData.INVALID_PERMISSIONS;
//		}
		
		String application = ri.getApplication();
		
		// only the retailkit admin is allowed to create new customers
		if (!ri.getUser().getCustomer().equals("retailkit.com")) {
			// TODO instead just fail if customer != ri.getUser().getCustomer()
			customer = ri.getUser().getCustomer();
		}
		
		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);
		
		if (credentials != null && !credentials.equals(MessageData.NO_DATA_FOUND)) { // reverse this logic cred == null || cred == data not found
			return MessageData.USER_EXISTS;
		}
		
		newToken(customer, username, password);
		
		return MessageData.success();
	}
	
}

