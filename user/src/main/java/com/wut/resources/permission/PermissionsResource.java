package com.wut.resources.permission;

import java.util.ArrayList;
import java.util.Collection;
import com.wut.pipeline.PermissionStore;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;

public class PermissionsResource extends CrudResource {
	private static final long serialVersionUID = -2367984055220639780L;
	private static PermissionStore permissionStore = new PermissionStore();

	public PermissionsResource() {
		super("permission", permissionStore);
	}

	@Override
	public UpdatePermissionOperation getUpdateOperation() {
		return new UpdatePermissionOperation(getSource());
	}
	
	@Override
	public ReadPermissionUserOperation getReadOperation() {
		return new ReadPermissionUserOperation(getSource());
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(getReadOperation());
		operationList.add(getUpdateOperation());
		return operationList;
	}

}
