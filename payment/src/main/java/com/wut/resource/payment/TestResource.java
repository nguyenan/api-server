package com.wut.resource.payment;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

public class TestResource extends AbstractResource {
	private static final long serialVersionUID = 1L;

	public TestResource() {
		super("test");
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> ops = new ArrayList<WutOperation>();
		//ops.add(new TestOperation());
		return ops;
	}
}
