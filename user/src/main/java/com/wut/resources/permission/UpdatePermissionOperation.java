package com.wut.resources.permission;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

// TODO I dont think this operation is used currently
public class UpdatePermissionOperation extends PermissionOperation {
	
	public UpdatePermissionOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String token = ri.getToken();
		String requestingUser = ri.getEscapedUsername();
		String customer = ri.getStringParameter("customer");
		String requestingCustomer = ri.getCustomer();
		String affectedUser = ri.getStringParameter("username");
		String role = ri.getStringParameter("role");
		String application = ri.getApplication();
		String requestingUserId = Authenticator.getUserId(customer, requestingUser);
		String affectedUserId = Authenticator.getUserId(customer, affectedUser);		
		
		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		} 
		
		// check if user has permission
		Data userPermission = source.read(requestingCustomer, application, requestingUserId);
		if (MessageData.NO_DATA_FOUND.equals(userPermission))
		{
			return ErrorData.INVALID_PERMISSIONS;
		}  
		
		// do update
		MappedData permissionData = new MappedData();
		permissionData.put("role", new StringData(role));
		permissionData.put("id", new StringData(requestingUserId));
		MappedData update = (MappedData) source.update(customer, application, affectedUserId, permissionData.getMapAsPojo());
		
		if (update == null || update.equals(MessageData.NO_DATA_FOUND)) { 
			return MessageData.NO_DATA_FOUND;
		}		 
		
		return MessageData.success();
	}
	
}

