package com.wut.resources.common;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.resources.operations.ParameteredOperation;

public abstract class AbstractOperation extends ParameteredOperation {
	protected String name;

	public AbstractOperation(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Data cacheGet(WutRequest request) throws Exception {
		return null;
	}
	
}
