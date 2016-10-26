package com.wut.resources.services;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.ByteStreamData;
import com.wut.model.stream.OutputStreamAdapter;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;
import com.wut.support.fileio.WutFile;
import com.wut.threading.WutProcessExecuter;
import com.wut.threading.WutSystemCommandProcess;

public class StopServiceOperation extends AbstractOperation {

	public StopServiceOperation() {
		super("stop");
	}

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		return null;
	}

}
