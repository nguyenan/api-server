package com.wut.format.json;

//import org.restlet.data.MediaType;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.wut.model.Data;
import com.wut.pipeline.WutRequestInterface;
import com.wut.support.StreamWriter;

public class JsonFormatter { // implements Formatter {
//	private XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
	
	public JsonFormatter() {
		
	}

	
//	public MediaType getMediaType() {
//		return MediaType.IMAGE_JPEG;
//	}

	//@Override
	public void format(Data data, StreamWriter stream, WutRequestInterface request) {
		throw new RuntimeException("format not supported for jsonformatter");
	}
}
