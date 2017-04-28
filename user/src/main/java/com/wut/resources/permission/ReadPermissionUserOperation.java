package com.wut.resources.permission;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

public class ReadPermissionUserOperation extends PermissionOperation {
	
	public ReadPermissionUserOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "read";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String token = ri.getToken();
		String requestingUser = ri.getEscapedUsername();
		String customer = ri.getStringParameter("customer");
		String requestingCustomer = ri.getCustomer();
		String affectedUser = ri.getStringParameter("username");
		String application = ri.getApplication();
		String affectedUserId = Authenticator.getUserId(customer, affectedUser);			
		
		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		}
		
		MappedData permisisonData = (MappedData) source.read(customer, application, affectedUserId);
		
		if (permisisonData == null || permisisonData.get("role") == null) {
			return MessageData.UNKNOWN_USER;
		}	  
		return permisisonData;
	}
	
}

