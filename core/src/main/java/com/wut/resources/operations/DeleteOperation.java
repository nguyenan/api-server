package com.wut.resources.operations;

import com.wut.resources.OperationParameter;
import com.wut.resources.common.WutOperation;

public abstract class DeleteOperation extends ParameteredOperation {
	public DeleteOperation() {
		addParameter(OperationParameter.ID);
	}
	
	@Override
	public String getName() {
		return WutOperation.DELETE;
	}
	
	@Override
	public boolean isIdempotent() {
		return true;
	}

	@Override
	public boolean isSafe() {
		return false;
	}
	
}
