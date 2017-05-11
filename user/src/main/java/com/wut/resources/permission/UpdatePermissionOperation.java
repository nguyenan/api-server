package com.wut.resources.permission;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.PermissionRole;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

// TODO I dont think this operation is used currently
public class UpdatePermissionOperation extends PermissionOperation {
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

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
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId();
		String requestingUserId = Authenticator.getUserId(adminCustId, requestingUser);

		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username");
		String affectedUserId = Authenticator.getUserId(adminCustId, affectedUser);
		String role = ri.getStringParameter("role");
		String application = ri.getApplication();
		
		// only authenticated users are allowed to access this feature
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(requestingCustomer, requestingUser, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		}

		// check if requestingUser has permission on affectedCustomer
		Data d = source.read(adminCustId, application, requestingUserId, affectedCustomer);
		if (MessageData.NO_DATA_FOUND.equals(d)) {
			return ErrorData.INVALID_PERMISSIONS;
		}
		
		if (!PermissionRole.contains(role))
			return ErrorData.ROLE_INVALID;

		// do update
		MappedData permissionData = (MappedData) source.read(adminCustId, application, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			permissionData = new MappedData();
		permissionData.put(affectedCustomer, new StringData(role));

		BooleanData update = (BooleanData) source.update(adminCustId, application, affectedUserId,
				permissionData.getMapAsPojo());
		
		return MessageData.successOrFailure(update);
	}
}
