package com.wut.support.datafetch;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;

//import org.restlet.Client;
//import org.restlet.Request;
//import org.restlet.Response;
//import org.restlet.data.Method;
//import org.restlet.data.Protocol;
//import org.restlet.data.Status;
//import org.restlet.representation.Representation;
//import org.restlet.representation.StringRepresentation;
//
//import com.wut.model.Data;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.resources.common.WutOperation.TYPE;
//import com.wut.support.ErrorHandler;
//import com.wut.support.Language;
//import com.wut.support.xml.DomWrapper;

public class DataFetcher {
//	private String domain;
//	private int port;
//	private static Map<TYPE, Method> typeToMethod = new HashMap<TYPE, Method>();
//	static {
//		typeToMethod.put(TYPE.CREATE, Method.PUT);
//		typeToMethod.put(TYPE.READ, Method.GET);
//		typeToMethod.put(TYPE.UPDATE, Method.POST);
//		typeToMethod.put(TYPE.DELETE, Method.DELETE);
//	}
//	
//	public DataFetcher() {
//		domain = "localhost"; // use setting
//		port = 8000; // use setting
//	}
//
//	public DataFetcher(String domain, int port) {
//		this.domain = domain;
//		this.port = port;
//	}
//
//	public Data fetchXML(String uri, String expr, String[] subtags) {
////		Response response = new Client(Protocol.HTTP).get(uri);
////		DomRepresentation dom = response.getEntityAsDom();
////		// String expr = tag;
////		NodeSet nodes = dom.getNodes(expr);
//		DomWrapper dom = HTTPHelper.getPageAsDom(uri);
//		Iterator<DomWrapper> itr = dom.iterator();
//		XPathFactory factory = XPathFactory.newInstance();
//		XPath xpath = factory.newXPath();
//		Map<String, XPathExpression> xpathExprs = new HashMap<String, XPathExpression>();
//		for (String subtag : subtags) {
//			XPathExpression subExr;
//			try {
//				subExr = xpath.compile(subtag + "/text()");
//				xpathExprs.put(subtag, subExr);
//			} catch (XPathExpressionException e) {
//				ErrorHandler.systemError("bad xpath", e);
//			}
//		}
//
//		ArrayList<MappedData> list = new ArrayList<MappedData>();
//		while (itr.hasNext()) {
//			DomWrapper n = itr.next();
//			MappedData result = new MappedData();
//			for (String sTag : xpathExprs.keySet()) {
//				XPathExpression xpr = xpathExprs.get(sTag);
//				String val;
//				try {
//					val = String
//							.valueOf(xpr.evaluate(n, XPathConstants.STRING));
//					result.put(sTag, val);
//				} catch (XPathExpressionException e) {
//					ErrorHandler.systemError("bad xpath",e);
//				}
//			}
//			list.add(result);
//		}
//		return new ListData(list);
//	}
//	
//	public Response fetchUrl(TYPE method, String url) {
//		return fetchUrl(method, url, "", new HashMap<String, String>());
//	}
//
//	public Response fetchUrl(TYPE method, String url, String data, Map<String, String> headers) {
//		//url = URLEncoder.encode(url);
//		System.out.println("fetching " + url);
//
//		Status status = Status.SERVER_ERROR_INTERNAL;
//		try {
//			Method requestMethod = typeToMethod.get(method);
//			Representation representation = null;
//			if (Language.isBlank(data)) {
//				representation = new StringRepresentation("name=bob"); // HACK dont do this for 'GET'
//			} else {
//				representation = new StringRepresentation(data);
//			}
//			Request request = new Request(requestMethod, url);
//			request.setEntity(representation);
//			Client client = new Client(Protocol.HTTP);
//			Response response = client.handle(request);
//			// status = response.getStatus();
//			return response;
//		} catch (Exception e) {
//
//		}
//
//		return null;
//	}
//	
//	private void setupRequest() {
//		
//	}
//
//	public Response fetchResource(TYPE method, String format, String group,
//			String resource, String params) {
//		StringBuilder url = new StringBuilder();
//		url.append("http://");
//		url.append(domain);
//		url.append(":");
//		url.append(port);
//		url.append("/resource/");
//		url.append(format);
//		url.append("/");
//		url.append(group);
//		url.append("/");
//		url.append(resource);
//		url.append("/");
//		url.append(params);
//		String urlStr = url.toString();
//		return fetchUrl(method, urlStr);
//	}

}
