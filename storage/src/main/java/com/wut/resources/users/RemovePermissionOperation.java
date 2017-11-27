package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.PermissionStore;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

public class RemovePermissionOperation extends UserOperation {
	private static PermissionStore permissionStore = new PermissionStore();
	private static String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

	public RemovePermissionOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "remove-permission";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String application = ri.getApplication();
		String affectedUserId = Authenticator.getUserId(adminCustId, username);

		Data delete = permissionStore.delete(adminCustId, application, affectedUserId);
		return delete;
	}
}
