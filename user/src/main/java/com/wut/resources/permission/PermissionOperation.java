package com.wut.resources.permission;

import com.wut.datasources.CrudSource;
import com.wut.pipeline.PermissionStore;
import com.wut.resources.operations.ParameteredOperation;

public abstract class PermissionOperation extends ParameteredOperation { 
	protected PermissionStore source;
	
	public PermissionOperation(CrudSource source) {
		this.source = (PermissionStore) source;
	} 
	
}
