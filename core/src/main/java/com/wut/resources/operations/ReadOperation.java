package com.wut.resources.operations;

import com.wut.model.Data;
import com.wut.model.map.MapModel;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.WutOperation;

public abstract class ReadOperation extends ParameteredOperation {
	
	public ReadOperation() {
		addParameter(OperationParameter.ID);
		addParameter(OperationParameter.create("data", MapModel.create()));
	}
	
	@Override
	public String getName() {
		return WutOperation.READ;
	}
	
	@Override
	public Data cacheGet(WutRequest request) throws MissingParameterException {
		// TODO implement cache
		
//		IdData tableId = getTableScope(request);
//		IdData rowId = getRowIdParam(request, true);
//		MappedData filter = request.getParameter("filter", true);
//		Data result = provider.read(tableId, rowId, filter);
//		return result;
		
		return null;
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
