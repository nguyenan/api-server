package com.wut.resources.users;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.pipeline.Authenticator;
import com.wut.pipeline.WutRequest;

public class DeleteUserOperation extends UserOperation {
	
	public DeleteUserOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getStringParameter("customer");
		String application = ri.getApplication();
		String username = ri.getStringParameter("username");
		String id = Authenticator.getUserId(customer, username);
		
		MappedData userData = (MappedData) source.delete(customer, application, id);
		
		return userData;
	}
	
}

