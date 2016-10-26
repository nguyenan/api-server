package com.wut.pipeline;

import com.wut.model.Data;

public class LogResponseProcessor extends AbstractProcessor {

	@Override
	public boolean process(WutRequest request, WutResponse response) {
		Data d = response.getData();
		
		System.out.println("RESPONSE: " + d);
		
		return true;
	}

}
