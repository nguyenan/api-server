package com.wut.cache;

import java.util.Iterator;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.Processor;
import com.wut.pipeline.RequestHelper;
import com.wut.pipeline.WutRequest;
import com.wut.pipeline.WutResponse;
import com.wut.resources.common.WutOperation;
import com.wut.support.ErrorHandler;

public class CacheProcessor implements Processor {

	@Override
	public void preProcess(WutRequest request, WutResponse response) {
		
	}

	/* For READ
	 * (non-Javadoc)
	 * @see com.wut.pipeline.Processor#process(com.wut.pipeline.WutRequest, com.wut.pipeline.WutResponse)
	 */
	@Override
	public boolean process(WutRequest request, WutResponse response) {
		String noCacheSetting = request.getSetting("noCache");
		boolean skipCache = noCacheSetting != null && noCacheSetting.equals("true");
		boolean isTableResouce = request.getResource().equalsIgnoreCase("table") || request.getResource().equalsIgnoreCase("row"); // TODO not needed in future
		WutOperation operation = RequestHelper.getOperation(request);
		
		if (!skipCache && isTableResouce && operation.isIdempotent()) {
			try {
				Data d = operation.cacheGet(request);
				
				if (operation.isSafe() && d != null) {
					response.setData(d);
				}
			} catch (Exception e) {
				final String msg = "failed to cache due to " + e;
				ErrorHandler.userError(request, msg, e);
				response.setData(new ErrorData(e)); // TODO do we want this???
				return false;
			}
		}
		
		return true;
	}
	
	/* For CREATE, UPDATE, and DELETE
	 * (non-Javadoc)
	 * @see com.wut.pipeline.Processor#postProcess(com.wut.pipeline.WutRequest, com.wut.pipeline.WutResponse)
	 */
	@Override
	public void postProcess(WutRequest request, WutResponse response) {
		String noCacheSetting = request.getSetting("noCache");
		boolean skipCache = noCacheSetting != null && noCacheSetting.equals("true");
		boolean isTableResouce = request.getResource().equalsIgnoreCase("table") || request.getResource().equalsIgnoreCase("row"); // TODO not needed in future 
		
		if (!isTableResouce) {
			return;
		}
		
		WutOperation operation = RequestHelper.getOperation(request);
		
		try {
			Data d = response.getData();
			if (d != MessageData.NO_DATA_FOUND) {
				operation.cachePut(request, d);
			}
		} catch (Exception e) {
			final String msg = "failed to cache due to " + e;
			ErrorHandler.userError(request, msg, e);
		}
//		
//		
//		if (!operation.isIdempotent()) {
//			try {
//				if (isTableResouce && operation.getName().equals("create")) {
//					Data d = response.getData();
//					ScalarData id = (ScalarData) d;
//					
//					WutOperation idempotentOperation = operation.getIdempotentCompanion();
//					
//					WutRequest requestWithId = request.clone();
//					requestWithId.setId(id.toRawString());
//					if (idempotentOperation != null) {
//						idempotentOperation.cacheGet(requestWithId);
//					}
//				}
//			} catch (Exception e) {
//				final String msg = "failed to cache due to " + e;
//				ErrorHandler.userError(request, msg, e);
//				response.setData(new ErrorData(e)); // TODO do we want this???
//			}
//		} else if (operation.isIdempotent() && isTableResouce && operation.getName().equals("read")) {
//			try {
//				WutOperation idempotentOperation = operation.getIdempotentCompanion();
//				WutRequest newRequest = request.builder();
//				if (idempotentOperation != null) {
//					idempotentOperation.cacheGet(newRequest);
//				}
//			} catch (Exception e) {
//				final String msg = "failed to cache due to " + e;
//				ErrorHandler.userError(request, msg, e);
//				response.setData(new ErrorData(e));
//			}
//		}
	}

}
