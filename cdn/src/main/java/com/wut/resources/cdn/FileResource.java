package com.wut.resources.cdn;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.CrudResource;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.resources.common.WutOperation;

@ResourceGroupAnnotation(name = "file", group = "cdn", desc = "clean cache")
public class FileResource extends CrudResource {
	private static final long serialVersionUID = 3301682262046459168L;

	public FileResource() {
		super("file", null);
	}

	@Override
	public String getName() {
		return "file";
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(new PurgeOperation());
		return operationList;
	}

}
