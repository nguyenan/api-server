package com.wut.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.wut.resources.OperationParameter;
import com.wut.resources.ResourceFactory;
import com.wut.resources.common.OperationIdentifier;
import com.wut.resources.common.WutOperation;

public class RequestHelper {
	private static ResourceFactory resFactory = ResourceFactory.getInstance();

	public static WutOperation getOperation(WutRequest request) {
		OperationIdentifier opId = resFactory.getOperationId(request);
		WutOperation op = resFactory.getOperation(opId);
		return op;
	}

	public static List<OperationParameter> getScopeParameters(WutRequest request) {
		WutOperation op = getOperation(request);
		List<OperationParameter> scopedParams = new ArrayList<OperationParameter>();
		List<OperationParameter> params = op.getParameters();
		for (OperationParameter param : params) {
			if (param.isPartOfScope()) {
				scopedParams.add(param);
			}
		}
		return scopedParams;
	}
	
}
