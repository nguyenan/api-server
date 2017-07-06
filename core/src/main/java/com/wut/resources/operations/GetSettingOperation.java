package com.wut.resources.operations;

import com.wut.model.map.MapModel;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.WutOperation;

public abstract class GetSettingOperation extends ParameteredOperation {
	
	public GetSettingOperation() {
		addParameter(OperationParameter.ID);
		addParameter(OperationParameter.create("data", MapModel.create()));
	}
	
	@Override
	public String getName() {
		return WutOperation.GET_SETTING;
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
