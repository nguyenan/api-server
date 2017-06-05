package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

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

		String application = ri.getApplication();

		customer = ri.getUser().getCustomer();

		MappedData credentials = (MappedData) source.readSecureInformation(customer, application, id);

		if (credentials == null || credentials.equals(MessageData.NO_DATA_FOUND)) {
			return MessageData.USER_EXISTS;
		}

		newToken(customer, username, password);

		return MessageData.success();
	}

}
