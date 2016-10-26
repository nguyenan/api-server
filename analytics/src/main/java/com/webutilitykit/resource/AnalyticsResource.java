package com.webutilitykit.resource;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

public class AnalyticsResource extends AbstractResource {
	private static final long serialVersionUID = 1L;
	//private static AnalyticsSearchOperation analyticsSearchOp = new AnalyticsSearchOperation();
	
	public AnalyticsResource() {
		super("analytics");
	}
	
	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new AnalyticsSearchOperation());
		return operations;
	}



}
