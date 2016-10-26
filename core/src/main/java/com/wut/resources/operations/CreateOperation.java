package com.wut.resources.operations;

import com.wut.model.map.MapModel;
import com.wut.model.scalar.ScalarModel;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.WutOperation;

public abstract class CreateOperation extends ParameteredOperation {
	
	public CreateOperation() {
		addParameter(OperationParameter.create("id", ScalarModel.create(), true));
		addParameter(OperationParameter.create("data", MapModel.create()));
	}
	
	@Override
	public String getName() {
		return WutOperation.CREATE;
	}

	@Override
	public boolean isIdempotent() {
		return false;
	}

	@Override
	public boolean isSafe() {
		return false;
	}
	
}
