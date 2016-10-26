package com.wut.test;
/*
import java.io.IOException;

//import org.restlet.Client;
//import org.restlet.Response;
//import org.restlet.data.Protocol;
//import org.restlet.data.Status;

import com.wut.resources.common.WutOperation.TYPE;

public class TestingHelper {

	private String host = "localhost";
	private String port = "8888";
	private String token = "0";


	public String getResource(TYPE action, String format, String resource,
			String parameters) {
		StringBuilder url = new StringBuilder();
		url.append("http://" + host);
		url.append(":");
		url.append(port);
		url.append("/api/hybrid/");
		url.append(resource);
		url.append("?");
		url.append("token=" + "'" + token + "'");
		url.append("&operation=" + "'" + action + "'");
		url.append("&format=" + "'" + format + "'");
		url.append("&parameters=" + "'" + parameters + "'");
		String urlStr = url.toString();
	
		String response = getUrl(action, urlStr);
		return response;
	}
	

	public String getUrl(TYPE method, String url) {
//		System.out.println("URL:\n" + url);
//		Status status = Status.SERVER_ERROR_INTERNAL;
//		Response response = null;
//		try {
//			Client c = new Client(Protocol.HTTP);
//			if (method == TYPE.CREATE) {
//				response = c.put(url, null);
//			} else if (method == TYPE.READ) {
//				response = c.get(url);
//			} else if (method == TYPE.UPDATE) {
//				response = c.post(url, null);
//			} else if (method == TYPE.DELETE) {
//				response = c.delete(url);
//			}
//			status = response.getStatus();
//		} catch (Exception e) {
//			System.err.println("Error getting URL " + url);
//		}
//
//		Response r = response;
//
//		if (!status.isSuccess()) {
//			String result = String.valueOf(r != null ? r.getEntityAsText() : "null");
//			String responseBody = "";
//			try {
//				responseBody = r.getEntity().getText();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return "ERROR:" + r.getStatus() + ":" + r.getWarnings() + ":" + responseBody;
//		} else {
//			String result = String.valueOf(r != null ? r.getEntityAsText() : "null");
//			return result;
//		}
		return null; // TODO fix this again
	}
}
*/