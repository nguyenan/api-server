package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
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
	public Data perform(WutRequest ri) {
		String application = ri.getApplication();
		String customer = ri.getCustomer();
		String username = ri.getUserId();
		try {
			String id = Authenticator.getUserId(customer, username);
			MappedData data = (MappedData) source.read(customer, application, id);
			if (data.equals(MessageData.NO_DATA_FOUND)) {
				data.put("message", customer + ":" + username);
				return data;
			}
			return MessageData.successOrFailure(data.get("username").toString().equals(username));
		} catch (Exception e) {
			return MessageData.error(e);
		}
	}
}
