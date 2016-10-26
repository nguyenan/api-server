package com.wut.resources.services;

import java.io.BufferedReader;
import java.io.FileReader;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.stream.ByteStreamData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;

public class StartServiceOperation extends AbstractOperation {
	
	public StartServiceOperation() {
		super("start");
	}

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		return null;
	}

}
