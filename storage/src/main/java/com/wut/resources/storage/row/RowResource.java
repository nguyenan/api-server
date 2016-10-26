package com.wut.resources.storage.row;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

public class RowResource extends AbstractResource {

	private static final long serialVersionUID = 546123423159822894L;

	public RowResource() {
		super("row");
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new CreateRowOperation());
		operations.add(new UpdateRowOperation());
		operations.add(new ReadRowOperation());
		operations.add(new DeleteRowOperation());
		return operations;
	}

}
