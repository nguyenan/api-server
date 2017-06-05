package com.wut.resources.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wut.cache.Cache;
import com.wut.cache.SimpleCacheProcessor;
import com.wut.format.FormatFactory;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorMessage;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.ProcessingPipeline;
import com.wut.pipeline.WutRequest;
import com.wut.pipeline.WutRequestBuilder;
import com.wut.resources.ResourceFactory;
import com.wut.support.ErrorHandler;
import com.wut.support.Language;
import com.wut.support.StreamWriter;
import com.wut.support.settings.SettingsManager;

// TODO name this AbstractHttpResource
public abstract class AbstractResource extends HttpServlet implements WutResource {
	private String name;

	public AbstractResource(String name) {
		this.name = name;

		ResourceFactory.getInstance().addResource(this);
	}

	@Override
	public List<String> getExamples() {
		return Collections.singletonList("find me some of them examples");
	}

	@Override
	public String getHelp() {
		return "help this"; // TODO get from an file (or db) for this resource
							// (based on name)
	}

	@Override
	public String getRevision() {
		return "1.00";
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}


	public List<String> getReadableSettings() {
		return new ArrayList<String>();
	}
	
	private Data getSetting(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		String setting = request.getParameter("id").toString();
		List<String> readableSettings = getReadableSettings();
		if (!readableSettings.contains(setting))
			return ErrorMessage.INVALID_SETTING;
	
		Boolean refreshSettings = request.getOptionalBooleanParameter("refreshSettings", false);
		String clientSettings = SettingsManager.getClientSettings(customer, setting, refreshSettings);
		if (clientSettings.isEmpty())
			return MessageData.NO_DATA_FOUND;
		return new StringData(clientSettings);
	}

	public class GetSettingOperation extends com.wut.resources.operations.GetSettingOperation {
		
		public GetSettingOperation() {
		}
		
		@Override
		public String getName() {
			return WutOperation.GET_SETTING;
		}

		@Override
		public Data perform(WutRequest request) throws Exception {
			return getSetting(request);
		}
		
	}
	
	public List<String> getWriteableSettings() {
		return getReadableSettings();
	}
	
	private Data setSetting(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		String setting = request.getParameter("id").toString();
		String value = request.getParameter("value").toString();
		List<String> writeableSettings = getWriteableSettings();
		if (!writeableSettings.contains(setting))
			return ErrorMessage.INVALID_SETTING;
		
		Boolean updateCustomerSettings = SettingsManager.setClientSettings(customer, setting, value);
		return MessageData.successOrFailure(updateCustomerSettings);
	}

	public class SetSettingOperation extends com.wut.resources.operations.SetSettingOperation {
		
		public SetSettingOperation() {
		}
		
		@Override
		public String getName() {
			return WutOperation.SET_SETTING;
		}

		@Override
		public Data perform(WutRequest request) throws Exception {
			return setSetting(request);
		}
		
	}
	// ////// HTTP SERVLET RELATED METHODS /////////

	private static final long serialVersionUID = 1L;

	private static final ProcessingPipeline pipeline = new ProcessingPipeline();

	private void processRequest(HttpServletRequest request, HttpServletResponse response, boolean isPost) throws IOException {
		try {
			String reqUrl = request.getRequestURL().toString();
			int lastSlashLocation = reqUrl.lastIndexOf('/');
			if (lastSlashLocation == -1) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bad Request");
				return;
			}
	
			String resource = reqUrl.substring(lastSlashLocation + 1, reqUrl.length());
	
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

			// addHeaders(response);
		} catch (Exception e) {
			MessageData error = MessageData.error(e);
			error.setCode(MessageData.SERVER_ERROR.getCode());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String stackTraceStr = sw.toString();
			error.put("stacktrace", stackTraceStr.replaceAll("\n", "").replaceAll("\t", "    "));
			PrintWriter out = response.getWriter();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FormatFactory.getDefaultFormatter().format(error, new StreamWriter(baos));
			out.print(new String(baos.toByteArray()));
		}

	}

	private void executeRequest(HttpServletResponse response, WutRequestBuilder requestInfo) throws IOException {
		WutRequest wutRequest = requestInfo.build();

		PrintWriter out = response.getWriter();

		StreamWriter stream = new StreamWriter(out);

		pipeline.process(stream, wutRequest);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		addHeaders(resp);

		super.service(req, resp);
	}

	// TODO HTTP request parameter decoding

	private Map<String, String> getRequestParameters(HttpServletRequest request) throws UnsupportedEncodingException {

		Map<String, String> urlParameters = new HashMap<String, String>();

		String queryString = request.getQueryString(); // d=789

		// queryString = URLDecoder.decode(queryString, "UTF-8");

		// REQUESTS MUST HAVE A QUERY STRING -- OTHERWISE 404

		String[] parts = queryString.split("&");

		for (String part : parts) {

			String[] assignment = part.split("=");

			if (assignment.length != 2) {

				throw new RuntimeException(
						"unable to parse request query string");

			}

			String variable = assignment[0];

			String value = assignment[1];

			value = decodeAndRemoteQuotes(value);

			urlParameters.put(variable, value);

		}

		return urlParameters;

	}

	// TODO rename to convert requestParametersToPOJO()

	private void processRequestParameters(WutRequestBuilder requestInfo, Map<String, String> urlParameters) {

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

	private void processResourceParameters(HttpServletRequest request,
			boolean isPost,

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

			// TODO fix this encoding issue with jeremy

			// String bodyDecoded = URLDecoder.decode(body, "UTF-8");

			// params = bodyDecoded;

			params = body;

		} else { // NOT POST (GET)

			params = urlParameters.get("parameters");

		}

		if (!Language.isBlank(params)) {

			requestInfo.parameters(params);

		}

	}

	private void addHeaders(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");

		// response.addHeader("Access-Control-Expose-Headers",
		// "X-My-Custom-Header, X-Another-Custom-Header");

		response.addHeader("Access-Control-Allow-Credentials", "false");

		response.addHeader("Access-Control-Max-Age", "86400");
	}
	
	
//	@Override
//	public List<Cache> getCaches() {
//		ArrayList<Cache> caches = new ArrayList<Cache>();
//		caches.add(new SimpleCacheProcessor(true));
//		return caches;
//	}
	
	
	/////// SERVLET METHODS ///////

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response, true);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response, false);
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doOptions(request, response);
	}

	// TODO rename to be decodeAndRemoveQuotes

	private String decodeAndRemoteQuotes(String toFix) {
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

	// / SERVLET

	public void init() throws ServletException
	{
		boolean intialized = initialize();

		if (!intialized) {
			throw new RuntimeException("failed to initialize");
		}
	}

	public void destroy()
	{
		// do nothing.

	}

}
