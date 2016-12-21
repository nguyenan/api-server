package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

public class ValidateToken extends UserOperation {

	public ValidateToken(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "validate";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		String token = ri.getStringParameter("token");
		Authenticator authenticator = new Authenticator();
		boolean checkToken = authenticator.checkToken(customer, username, token);
		if (!checkToken) {
			return ErrorData.INVALID_TOKEN;
		} else {
			MessageData success = MessageData.success();
			success.put("message", "validate token successfully");
			return success;
		}
	}
}