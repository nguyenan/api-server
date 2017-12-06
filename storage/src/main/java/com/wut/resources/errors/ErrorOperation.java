package com.wut.resources.errors;

import com.wut.datasources.CrudSource;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.logging.StackTraceStore;

public abstract class ErrorOperation extends ParameteredOperation {
	private static final String APPLICATION = "core";
	protected StackTraceStore source;
 
	public ErrorOperation(CrudSource source) {
		this.source = (StackTraceStore) source; 
	}
	 
}
