package com.wut.resources.errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;
import com.wut.support.logging.StackTraceStore;

public class ErrorsResource extends CrudResource {
	private static final long serialVersionUID = -2367984055220639780L;
	private static StackTraceStore stackTraceStore = new StackTraceStore();

	public ErrorsResource() {
		super("errors", stackTraceStore);
	}

	@Override
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[] {});
	}

	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[] {});
	}

	@Override
	public ReadErrorOperation getReadOperation() {
		return new ReadErrorOperation(getSource());
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(getReadOperation());
		operationList.add(new GetSettingOperation());
		operationList.add(new SetSettingOperation());
		return operationList;
	}

}
