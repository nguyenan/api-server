package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

public class PingOperation extends UserOperation {

	public PingOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String application = ri.getApplication();
		String customer = "dev1.tend.ag";
		String username = "admin@dev1.tend.ag";
		String id = Authenticator.getUserId(customer, username);
		source.read(customer, application, id);
		return MessageData.success();
	}
}
