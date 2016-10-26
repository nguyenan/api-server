package com.wut.resource.payment;

import java.util.List;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.AbstractOperation;
import com.wut.resources.common.WutOperation;

public class TestOperation extends AbstractOperation {

	public TestOperation(String name) {
		super("read");
	}

	@Override
	public String getName() {
		return "read";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		MappedData map = new MappedData();
		map.put("hi", new StringData("test"));
		map.put("you", new StringData("my"));
		map.put("silly", new StringData("buttons"));
		map.put("human", new StringData("says the computer"));
		return map;
	}

	@Override
	public List<OperationParameter> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
