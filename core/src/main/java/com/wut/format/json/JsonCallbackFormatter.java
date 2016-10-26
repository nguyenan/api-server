package com.wut.format.json;

//import com.wut.format.SuperGenericFormatter;
//import com.wut.format.common.FCharacterSet;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.StreamWriter;

public class JsonCallbackFormatter { //extends SuperGenericFormatter {

//	public JsonCallbackFormatter(FCharacterSet characterSet) {
//		super(characterSet);
//	}
//
//	@Override
//	public void start(StreamWriter stream, WutRequestInterface request) {
//		String callback = request.getSetting("callback");
//		if (callback == null) {
//			callback = "callback";
//		}
//		write(stream, callback + "(" + characterSet.getFormatOpen());
//	}
//
//	@Override
//	public void end(StreamWriter stream) {
//		write(stream, characterSet.getFormatClose() + ");");
//	}

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
