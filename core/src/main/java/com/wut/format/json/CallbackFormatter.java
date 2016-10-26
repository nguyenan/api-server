package com.wut.format.json;

import com.wut.format.GenericFormatter;
import com.wut.format.TokenSet;
import com.wut.model.ModelHelper;
import com.wut.pipeline.WutRequestInterface;
import com.wut.support.StreamWriter;

public class CallbackFormatter extends GenericFormatter {
	private String callbackFunctionName;
	
	public CallbackFormatter(TokenSet tokens, String callbackFunctionName) {
		super(tokens);
		this.callbackFunctionName = callbackFunctionName;
	}
	
	// TODO make start method generic and not specific to json callbacks
	@Override
	public void start(StreamWriter stream) {
		//ModelHelper characterSet = new ModelHelper(getTokens());
		
		String callback = callbackFunctionName; //request.getSetting("callback");
		if (callback == null) {
			callback = "callback";
		}
		//write(stream, callback + "(" + characterSet.getFormatOpen());
		ModelHelper.write(stream, callback + "(" + getStartToken().getData());

	}

	@Override
	public void end(StreamWriter stream) {
//		write(stream, characterSet.getFormatClose() + ");");
		ModelHelper.write(stream, getEndToken().getData() + ");");
	}

	// @Override
	// public void start(StreamWriter stream, WutRequest request) {
	// String op = request.getOperationIdentifier().getOperationName();
	// String resource = request.getResource();
	// //resource = resource.substring(0,1).toUpperCase() +
	// resource.substring(1);
	// String capitalizedRes =
	// TextHelper.perWordCapitalize(resource);//.replaceAll(".", "");
	// final String resNoDots = capitalizedRes.replaceAll("\\.", "");
	// String callback = op + resNoDots;
	// write(stream, callback + "({");
	// }
	//
	// @Override
	// public void end(StreamWriter stream) {
	// write(stream, "})");
	// }

}
