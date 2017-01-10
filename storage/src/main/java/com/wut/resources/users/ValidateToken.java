package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.User;
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
		String token = ri.getStringParameter("token");
		String customer = ri.getStringParameter("customer");
		String username = ri.getStringParameter("username");
		Authenticator authenticator = new Authenticator();
		boolean isTokenValid = authenticator.validateToken(customer, username, token);
		if (!isTokenValid) {
			return ErrorData.INVALID_TOKEN;
		} else {
			return MessageData.success();
		}
	}
}