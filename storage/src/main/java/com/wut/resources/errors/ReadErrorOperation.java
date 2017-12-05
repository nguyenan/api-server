package com.wut.resources.errors;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.pipeline.WutRequest;

public class ReadErrorOperation extends ErrorOperation {
	
	public ReadErrorOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "read";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getCustomer();
		String application = ri.getApplication(); 
		String id = ri.getStringParameter("id");
		
		MappedData userData = (MappedData) source.read(customer, application, id);
		
		return userData;
	}
	
}

