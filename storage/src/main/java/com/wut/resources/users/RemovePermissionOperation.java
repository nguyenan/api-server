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
		return "removelist";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String application = ri.getApplication();
		String affectedUserId = Authenticator.getUserId(adminCustId, username);
		Data permissionData = permissionStore.read(adminCustId, application, affectedUserId);
		if (permissionData.equals(MessageData.NO_DATA_FOUND))
			return BooleanData.TRUE;

		MappedData data = (MappedData) permissionData;
		if (data.get(customer) == null)
			return BooleanData.TRUE;

		data.remove(new StringData(customer));
		permissionStore.delete(adminCustId, application, affectedUserId);
		Data updated = permissionStore.update(adminCustId, application, affectedUserId, data.getMapAsPojo());
		return updated;
	}
}
