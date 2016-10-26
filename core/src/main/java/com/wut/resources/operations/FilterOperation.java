package com.wut.resources.operations;

import com.wut.model.map.MapModel;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.WutOperation;

public abstract class FilterOperation extends ParameteredOperation {
	
	public FilterOperation() {
		addParameter(OperationParameter.ID);
		addParameter(OperationParameter.create("data", MapModel.create()));
	}
	
	@Override
	public String getName() {
		return WutOperation.READ;
	}
	
}
