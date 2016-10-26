package com.wut.protocols.http.resthybrid;
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wut.pipeline.ProcessingPipeline;
import com.wut.pipeline.WutRequest;
import com.wut.pipeline.WutRequestBuilder;
import com.wut.support.ErrorHandler;
import com.wut.support.Language;
import com.wut.support.StreamWriter;

public class RestHybridServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final ProcessingPipeline pipeline = new ProcessingPipeline();

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response, boolean isPost) throws IOException {
		String reqUrl = request.getRequestURL().toString();
		int lastSlashLocation = reqUrl.lastIndexOf('/');
		if (lastSlashLocation == -1) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad Request");
			return;
		}
		String resource = reqUrl.substring(lastSlashLocation + 1,
				reqUrl.length());
		if (resource.equals("")) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad Request");
			return;
		}

		WutRequestBuilder requestInfo = new WutRequestBuilder();
		requestInfo.resource(resource);

		Map<String, String> urlParameters = getRequestParameters(request);

		processRequestParameters(requestInfo, urlParameters);

		processResourceParameters(request, isPost, requestInfo, urlParameters);

		String noCache = urlParameters.get("noCache");
		if (!Language.isBlank(noCache)) {
			requestInfo.addSetting("noCache", noCache);
		}

		// final String referrer = String.valueOf(getReferrerRef());
		// requestInfo.addMetric("referrer", referrer);
		// final String agent = String.valueOf(getClientInfo().getAgent());
		// requestInfo.addMetric("agent", agent);
		// requestInfo.addMetric("uri", getReference().toUri().toString());

		executeRequest(response, requestInfo);

		//addHeaders(response);
	}

	private void executeRequest(HttpServletResponse response,
			WutRequestBuilder requestInfo) throws IOException {
		WutRequest wutRequest = requestInfo.build();
		PrintWriter out = response.getWriter();
		StreamWriter stream = new StreamWriter(out);
		pipeline.process(stream, wutRequest);
	}

	// TODO HTTP request parameter decoding 
	private Map<String, String> getRequestParameters(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Map<String, String> urlParameters = new HashMap<String, String>();
		String queryString = request.getQueryString(); // d=789
		
		// REQUESTS MUST HAVE A QUERY STRING -- OTHERWISE 404
	
		String[] parts = queryString.split("&");
		for (String part : parts) {
			String[] assignment = part.split("=");
			String variable = assignment[0];
			String value = assignment[1];
			//value = URLDecoder.decode(value, "UTF-8");

//			if (value.length() >= 3 && value.charAt(0) == '"'
//					&& value.charAt(value.length() - 1) == '"') {
//				// remove quotes
//				value = value.substring(1, value.length() - 1);
//			} else if (value.length() >= 3 && value.charAt(0) == '\''
//					&& value.charAt(value.length() - 1) == '\'') {
//				// remove quotes
//				value = value.substring(1, value.length() - 1);
//			}

			//if (!variable.equals("parameters")) {
				value = remoteQuotes(value);
			//}

			urlParameters.put(variable, value);
		}
		return urlParameters;
	}

	// TODO rename to convert requestParametersToPOJO()
	private void processRequestParameters(WutRequestBuilder requestInfo,
			Map<String, String> urlParameters) {
		String operation = urlParameters.get("operation");
		requestInfo.operation(operation);
		String format = urlParameters.get("format");
		requestInfo.format(format);
		String token = urlParameters.get("token");
		requestInfo.token(token);
		String application = urlParameters.get("application");
		requestInfo.application(application);
		String callback = urlParameters.get("callback");
		requestInfo.addSetting("callback", callback);
		String userId = urlParameters.get("userId");
		requestInfo.setUserId(userId);
		String customerId = urlParameters.get("customerId");
		requestInfo.setCustomerId(customerId);
		String id = urlParameters.get("id");
		requestInfo.id(id);
	}

	private void processResourceParameters(HttpServletRequest request, boolean isPost,
			WutRequestBuilder requestInfo, Map<String, String> urlParameters)
			throws IOException {
		String params = null;
		if (isPost) {
			// if is post read parameters from body of message
			// TODO use better code instead:
			// http://stackoverflow.com/questions/3422687/problem-reading-request-body-in-servlet
			StringBuilder bodyStringBuilder = new StringBuilder();
			BufferedReader bodyBufferedReader = null;
			try {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					bodyBufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					char[] charBuffer = new char[128];
					int bytesRead = -1;
					while ((bytesRead = bodyBufferedReader.read(charBuffer)) > 0) {
						bodyStringBuilder.append(charBuffer, 0, bytesRead);
					}
				} else {
					bodyStringBuilder.append("");
				}
			} catch (IOException ex) {
				throw ex;
			} finally {
				if (bodyBufferedReader != null) {
					try {
						bodyBufferedReader.close();
					} catch (IOException ex) {
						throw ex;
					}
				}
			}
			String body = bodyStringBuilder.toString();
			params = body;
		} else { // NOT POST (GET)
			params = urlParameters.get("parameters");
		}

		if (!Language.isBlank(params)) {
			requestInfo.parameters(remoteQuotes(params));
		}
	}

	private void addHeaders(HttpServletResponse response) {
//		response.addHeader("Access-Control-Allow-Origin", "*");
//		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST"); // TODO this might be related to CORS -- NOT SURE NEEDED
//		response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		//response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept"); // TODO this might be related to CORS -- NOT SURE NEEDED
		//Access-Control-Allow-Headers: x-requested-with
		
		// FOR CORS
		// See https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS
		//response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
		response.addHeader("Access-Control-Expose-Headers", "X-My-Custom-Header, X-Another-Custom-Header");
		response.addHeader("Access-Control-Allow-Credentials", "false");
		//response.addHeader("Content-Type", "application/x-www-form-urlencoded");
		response.addHeader("Access-Control-Max-Age", "86400");
		
//		Access-Control-Request-Method:  GET
//		Access-Control-Request-Header:  x-requested-with
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response, true);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response, false);
	}
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//addHeaders(response);
		super.doOptions(request, response);
	}

	private String remoteQuotes(String toFix) {
		if (toFix == null)
			return null;
		String fixed = toFix;
		try {
			fixed = URLDecoder.decode(toFix, "UTF-8");
			// remove single quotes
			if (fixed.indexOf("'") == 0) {
				fixed = fixed.replaceFirst("'", "");
			}
			if (fixed.charAt(fixed.length() - 1) == '\'') {
				fixed = fixed.substring(0, fixed.length() - 1);
			}
			// remove double quotes
			if (fixed.indexOf("\"") == 0) {
				fixed = fixed.replaceFirst("\"", "");
			}
			if (fixed.charAt(fixed.length() - 1) == '\"') {
				fixed = fixed.substring(0, fixed.length() - 1);
			}
		} catch (UnsupportedEncodingException e) {
			ErrorHandler.userError("problem with request argument", e);
		}
		return fixed;
	}
}
*/
