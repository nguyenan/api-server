package com.wut.resources.operations;

import com.wut.model.scalar.ScalarModel;
import com.wut.resources.OperationParameter;

public abstract class SearchOperation extends ParameteredOperation {
	
	public SearchOperation() {
		addParameter(OperationParameter.create("term", ScalarModel.create()));
	}
	
	@Override
	public boolean isIdempotent() {
		return true;
	}

	@Override
	public boolean isSafe() {
		return true;
	}
	
	
}
