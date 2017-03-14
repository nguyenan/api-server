package com.wut.resources.cdn;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.CrudResource;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.resources.common.WutOperation;

@ResourceGroupAnnotation(name = "file", group = "cdn", desc = "clean cache")
public class ZoneResource extends CrudResource {
	private static final long serialVersionUID = 3301682262046459168L;

	public ZoneResource() {
		super("zone", null);
	}

	@Override
	public String getName() {
		return "zone";
	}
	
	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(new PurgeAllOperation());
		return operationList;
	}

}
