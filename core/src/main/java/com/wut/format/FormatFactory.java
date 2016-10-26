package com.wut.format;

import com.wut.format.common.GenericTokenSet;
import com.wut.format.html.HtmlTableCharacterSet;
import com.wut.format.json.CallbackFormatter;
import com.wut.format.json.JsonCharacterSet;
import com.wut.format.json.JsonDecoder;
import com.wut.format.xml.XmlCharacterSet;

// TODO I don't think this class will be needed in the future
public class FormatFactory {
	private static FormatFactory singleton = new FormatFactory();
	public enum Format { JSON, JS, HTML, XML };
	
	//private static Formatter xmlFormatter = new XMLFormatter();
	//private static Formatter jsonFormatter = new NewJsonFormatter();
	private Formatter jsonFormatter;// = new SuperGenericFormatter(new FCharacterSet(new AbstractToken("{\"result\":"), new JsonCharacterSet().getTokens(), new AbstractToken("}")));

	private Formatter jsonCallback;// = new JsonCallbackFormatter(new FCharacterSet(new AbstractToken("{\"result\":"), new JsonCharacterSet().getTokens(), new AbstractToken("}")));
	private Formatter htmlTableFormatter;// = new HTMLTableFormatter();
	
	
	
	private JsonDecoder jsonDecoder;// = new JsonDecoder();
	private GenericDecoder xmlDecoder;// = new JsonDecoder();

	//private static Formatter jsonCallback = new JsonCallbackFormatter();
	
	private static Formatter xmlFormatter;
	
	private FormatFactory() {
		// JSON
		TokenSet jsonTokenSet = new JsonCharacterSet().getTokens();
//		Token jsonStartToken = new AbstractToken("{\"result\":");
//		Token jsonEndToken =  new AbstractToken("}");
		jsonFormatter = new GenericFormatter(jsonTokenSet);
		jsonCallback = new CallbackFormatter(jsonTokenSet, "crazyCallbackFunction");
		jsonDecoder = new JsonDecoder();
		
		// XML
		final XmlCharacterSet xmlCharacterSet = new XmlCharacterSet();
		final GenericTokenSet xmlTokens = xmlCharacterSet.getTokens();
		xmlFormatter = new GenericFormatter(xmlTokens);
		xmlDecoder = new GenericDecoder(xmlTokens);
		
		// HTML
		TokenSet htmlTokenSet = new HtmlTableCharacterSet().getTokens();
		htmlTableFormatter = new GenericFormatter(htmlTokenSet);
	}
	
	public Formatter getFormatter(String formatterName) {
		// TODO support XML again
//			if (formatterName.equalsIgnoreCase("xml")) {
//				return xmlFormatter;
//			} else {
				
			if (formatterName.equalsIgnoreCase("json")) {
				return jsonFormatter;
			} else if (formatterName.equalsIgnoreCase("js")) {
				return jsonCallback;
			} else if (formatterName.equalsIgnoreCase("html")) {
				return htmlTableFormatter;
			} else if (formatterName.equalsIgnoreCase("xml")) {
				return xmlFormatter;
			}
//			else if (formatterName.equalsIgnoreCase("jpg")) {
//				return new JPGFormatter(); // TODO fix
//			}
			// TODO add "raw" format which just returns text of a toString()
			return null;
	}
	
	public static Formatter getDefaultFormatter() {
		return singleton.jsonFormatter;
	}
	
	public static Decoder getDefaultDecoder() {
//		TokenSet jsonTokenSet = new JsonCharacterSet().getTokens();
//		return new GenericDecoder(jsonTokenSet);
		
		return singleton.jsonDecoder;
	}
	
	public static Decoder getDecoder(Format format) {
		if (format == Format.JSON) {
			return singleton.jsonDecoder;
		} else if (format == Format.XML) {
			return singleton.xmlDecoder;
		}
		
		return null;
	}
	
	
	
	public static FormatFactory getInstance() {
		return singleton;
	}
}
