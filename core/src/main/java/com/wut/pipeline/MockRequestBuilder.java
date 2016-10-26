package com.wut.pipeline;

import com.wut.model.scalar.ScalarData;
import com.wut.support.StreamWriter;

// TODO extract this into its own class
public class MockRequestBuilder extends WutRequestBuilder {
	
	public MockRequestBuilder() {
		operation("authenticate"); // TODO ????
		format("json");
		resource("user");
		content(null);
		token("-1");
		application("MockRequest");
	}
	
	public MockRequestBuilder authenticate() {
		// build authentication request
		MockRequestBuilder mockAuthReq = new MockRequestBuilder();
		mockAuthReq.operation("authenticate");
		mockAuthReq.format("json");
		mockAuthReq.resource("user");
		mockAuthReq.content(null);
		mockAuthReq.token("-1");
		mockAuthReq.parameters("{\"password\":\"public\", \"username\":\"public\", \"customer\":\"public\"}");
		mockAuthReq.application("MockRequest");
		
		// get request object
		WutRequest request = mockAuthReq.build();
		
		// make request
		//WutResponseInterface response = request.getResponse();
		
		ProcessingPipeline pipeline = ProcessingPipeline.create();
		WutResponse response = new WutResponse();
		response.setStream(StreamWriter.create(System.out));
		pipeline.process(response, request);
		
		// store token
		ScalarData token = (ScalarData) response.getData();
		token(token.toRawString());
		
		return this;
	}
	
}

