package com.wut.resources.operations;

import java.util.ArrayList;
import java.util.List;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.WutOperation;

public abstract class ParameteredOperation extends WutOperation {
	private List<OperationParameter> parameters = new ArrayList<OperationParameter>();

	public List<OperationParameter> getParameters() {
		return parameters;
	}
	
	public void addParameter(OperationParameter parameter) {
		parameters.add(parameter);
	}
	
	// TODO dont implement this here. either put in AbstractOperation class
	// or make each implementor (ReadOp, UpdateOp, CreateUp, etc) handle
	// this function themselves. Overridable if needed.
	public boolean checkPermission(WutRequest request) {
		return false;
	}
	
	@Override
	public Data cacheGet(WutRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isIdempotent() {
		return false;
	}
	
	@Override
	public boolean isSafe() {
		return false;
	}

	@Override
	public WutOperation getIdempotentCompanion() {
		return null;
	}
 
}
