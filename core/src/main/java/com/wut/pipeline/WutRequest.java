package com.wut.pipeline;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.ErrorHandler;
//import com.wut.support.StreamWriter;
import com.wut.support.URLArguments;

// TODO make immutable
// TODO use builder pattern to build requestinfo
// TODO make request a package visible class (along with all the methods)
//@Immutable // NOT TRUE
public class WutRequest implements WutRequestInterface {
	
	private MappedData parameters = new MappedData();
	private String format = null;
	private String operation; // TODO rename action
	private String resource; // TODO remove
	private String id;
	//private TYPE operationType; // TODO does this really need to be a "TYPE"??? instead let's use action???
	private InputStream bodyData; // TODO rename content
	private Map<String, String> settings;
	private Map<String, String> metrics;
	private String token;
	private String application;
	private String userId;
	private String customer;
	private User user; // TODO transient (used to cache user here) // need method loadUser() -- gets from auth using token
	private WutRequestBuilder builder;
	
	public WutRequest(MappedData parameters, String application, String format,
			String operation, String resource, String id, Map<String, String> metrics,
			String userId, InputStream content,
			Map<String, String> settings, String token, String customer, WutRequestBuilder builder) {
		this.parameters = parameters;
		this.application = application;
		this.format = format;
		this.operation = operation;
		this.resource = resource;
		this.bodyData = content;
		this.settings = settings;
		this.metrics = metrics;
		this.token = token;
		this.userId = userId;
		this.customer = customer;
		this.builder = builder;
	}
	
	@Override
	public WutRequest clone() {
		return new WutRequest(parameters, application, format, operation, resource, id, metrics, userId, bodyData, settings, token, customer, builder);
	}
	
	public String getAction() {
		return operation;
	}

	// duplicated by authenticationToken()
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	// TODO rename getOptionParameterAsString (or get option string parameter)
	public String getOptionalParameterAsString(String name) {
		Data param = parameters.get(name);
		return param == null ? null : String.valueOf(param);
	}
	
	// TODO rename getOptionalParameter
	public Data getOptionalNonStringParameter(String name) {
		Data data = null;
		try {
			data = getParameter(name, true);
		} catch (MissingParameterException e) {
			ErrorHandler.systemError("should never throw missing parameter here");
		}
		return data;
	}
	
	public String getStringParameter(String name) throws MissingParameterException {
		Data param = parameters.get(new StringData(name));
		if (param == null) {
			throw new MissingParameterException(name);
		}
		return String.valueOf(param);
	}
	
	public MappedData getParameters() {
		return parameters;
	}
	
	public String getEncdodedParameter(String name) {
		try {
			return URLEncoder.encode(String.valueOf(parameters.get(name)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			ErrorHandler.systemError(e, "failed to get encoded paramter");
			return null;
		}
	}

	public String getFormat() {
		return format;
	}

	public String getResource() {
		return resource;
	}

	@Override
	public String toString() {
		StringBuilder sob = new StringBuilder();
		sob.append("Format:" + format);
		sob.append(" Token:" + token);
		sob.append(" Resource:" + resource);
		sob.append(" Action:" + operation);
		sob.append(" Parameters:" + String.valueOf(parameters));
		sob.append(" Body: ");
		writeBody(sob);
		return sob.toString();
	}
	
	// TODO no longer needed
	public String getRequestIdentifier() {
		StringBuilder sob = new StringBuilder();
		sob.append("U:" + userId + " ");
		//sob.append("A:" + operation + " ");
		sob.append("R:" + resource + " ");
		sob.append("I:" + id + " ");
		sob.append("P:" + String.valueOf(parameters));
		return sob.toString();
	}
	
	public String getRequestIdentifierNoParameters() {
		StringBuilder sob = new StringBuilder();
		sob.append("U:" + userId + " ");
		//sob.append("A:" + operation + " ");
		sob.append("R:" + resource + " ");
		sob.append("I:" + id + " ");
		return sob.toString();
	}
	
	private void writeBody(StringBuilder sb) {
		if (bodyData != null) {
			if (bodyData.markSupported()) {
				bodyData.mark(Integer.MAX_VALUE); // TODO this might be expensive?
				try {
					// TODO make more efficient
					// use something like PeekableInputStream
					int c;
					while ((c = bodyData.read()) != -1) {
						sb.append(new Character((char)c));
					}
					bodyData.reset();
				} catch (IOException e) {
					ErrorHandler.dataLossError("Unable to reset request content therefore unable to save xml form of request", e);
				}
			} else {
				ErrorHandler
						.dataLossError("Unable to persist request content. InputStream mark method not supported.");
			}
		}
	}

	// TODO this method needs fixing before it's useful
	public String toXMLString() {
		// TODO create XMLBuilder utility class
		StringBuilder sob = new StringBuilder();
		sob.append("<request>").append("\n");
		Date dateTime = new Date();
		String desc = "Request at " + dateTime;
		sob.append("\t<description>").append(desc).append("</description>\n");
		// TODO what happened to URI -- do we need it, yes probably
		//sob.append("\t<uri>").append(uri).append("</uri>\n");
		sob.append("\t<headers>\n");
		for (String settingName : settings.keySet()) {
			String settingValue = settings.get(settingName);
			sob.append("\t\t<header name=\"" + settingName + "\" value=\""
					+ settingValue + "\"/>\n");
		}
		sob.append("\t</headers>\n");
		sob.append("\t<body>");
		writeBody(sob);
		sob.append("</body>\n");
		sob.append("</request>\n");
		return sob.toString();
	}
	
	// TODO get User out of request
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	// TODO move into User!!!!
	public String getEscapedUsername() {
		// replace with userid (so users can change usernames)
		// return URLEncoder.encode(this.user);
		//return this.user.getUsername().replaceAll("@", "_").replaceAll("\\.", "_");
		return escapeUsername(this.user.getId());
	}

	public String getUserId() {
		return this.userId;
	}
	
	public InputStream getBodyData() {
		return this.bodyData;
	}

	public String getSetting(String name) {
		return settings.get(name);
	}
	
	// TODO get rid of this function
	public String getScope() {
		return makeScope(application, user.getCustomer(), user.getId(), null);
	}
	
	// TODO merge with all the other scope methods
	public String getScoppedID() {
		return makeScope(application, customer, userId, getId());
	}
	
	// TODO remove
	public static String makeScope(String application, String customer, String username, String variable) {
		StringBuilder scope = new StringBuilder();
		scope.append(application);
		scope.append(":");
		scope.append(customer);
		scope.append(":");
		scope.append(escapeUsername(username));
		if (variable != null) { // TODO NOT NEEDED SHOULD NOT PASS NULL IN HERE
			scope.append(":");
			scope.append(variable);
		}
		String scopeStr = scope.toString();
		return scopeStr;
	}
	
	public String scopify(String variable) {
		String scope = makeScope(application, user.getCustomer(), user.getUsername(), variable);
		return scope;
	}
	
	public static String escapeUsername(String username) {
		// replace with userid (so users can change usernames)
		// return URLEncoder.encode(this.user);
		return username.replaceAll("@", "_").replaceAll("\\.", "_");
	}
	
	// TODO get rid of this function (use scopify() )
	public String getScopeWithId() throws MissingParameterException {
		if (!resource.equals("user")) {
			StringData idData = getParameter("id");
			String id = idData != null ? idData.toRawString() : null;
			return makeScope(application, customer, userId, id);
		} else {
			return customer + "-" + getParameter("id");
			// TODO update to be:
			// return makeScope("web-utility-kit", customer, userId, "none");
		}
	}
		
	public String getCustomer() {
		return customer;
	}

	public String getApplication() {
		return application;
	}

	// TODO get rid of this use scopify instead
	public String getCustomerScopeId() throws MissingParameterException {
		String customer = getCustomer();
		String id = getId();
		String identifier = customer + "." + id;
		return identifier;
	}

	// TODO instead make a function that takes a parameterized type T
	public Map<String,String> getParameterAsMap(String parameterName) throws MissingParameterException {
		String mapStr = getStringParameter(parameterName);
		return URLArguments.parseMap(mapStr);
	}

	// TODO ummmm use "id" member field???
	public String getId() {
		return getOptionalParameterAsString("id");
	}
	
	public void setId(String id) {
		this.id = id;
		parameters.put("id", id);
	}
	
	// TODO can we rename metric to be property (or something else) -- call it setting or preference
	public String getMetric(String metricName) {
		return metrics.get(metricName);
	}

	// TODO can authentication token be a metric
	public String getAuthenticationToken() {
		return token;
	}
	
	public <T> T getOptionalParameter(String parameterName) throws MissingParameterException {
		return getParameter(parameterName, true);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParameter(String parameterName, boolean isOptional) throws MissingParameterException {
		T param = (T) parameters.get(parameterName);
		if (!isOptional && param == null) {
			throw new MissingParameterException(parameterName);
		}
		return param;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParameter(String parameterName) throws MissingParameterException {
		return (T) getParameter(parameterName, false);
	}
	
	public boolean isAuthenticationRequest() {
		// TODO remove magic strings
		return getResource().equalsIgnoreCase("user") && operation.equalsIgnoreCase("authenticate");
	}
	
	public boolean isValidateTokenRequest() {
		// TODO remove magic strings
		return getResource().equalsIgnoreCase("user") && operation.equalsIgnoreCase("validate");
	}
	
	public WutRequestBuilder getBuilder() {
		return this.builder;
	}
}
