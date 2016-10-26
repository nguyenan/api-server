package com.wut.pipeline;

import java.util.HashMap;
import java.util.Map;

import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
import com.wut.model.map.MessageData;
import com.wut.resources.ResourceFactory;
import com.wut.resources.common.OperationIdentifier;
import com.wut.resources.common.WutOperation;
import com.wut.support.Language;

// TODO is this really needed? can we just do this at the time of executing the
// request
public class PermissionProcessor extends AbstractProcessor {
	private ResourceFactory resFactory = ResourceFactory.getInstance();
	private Map<String, Integer> bitmapBits = new HashMap<String, Integer>();
	
	public PermissionProcessor() {
		bitmapBits.put("com.", 1);
	}
	
	@Override
	public boolean process(WutRequest request, WutResponse response) {
		// 
		OperationIdentifier opId = resFactory.getOperationId(request);
		WutOperation op = resFactory.getOperation(opId);
		//String operationClassName = op.getClass().getCanonicalName();
		//Integer operationBit = bitmapBits.get(operationClassName);
		
		// Check with operation
		//op.checkPermissions(request, response);
		return true;
		
		//response.setData(MessageData.INSUFFIENT_PRIVILEGES);
		//return false;
	}
	
}
