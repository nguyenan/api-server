package com.wut.resources.permission;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

public class ReadPermissionUserOperation extends PermissionOperation {
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");
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
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId();
		
		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username");
		
		String application = ri.getApplication();
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);			
		
		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		}
		
		// read Permission Data
		Data data = source.read(adminCustId, application, affectedUserId, affectedCustomer);		
		return data;	 
	}
	
}

