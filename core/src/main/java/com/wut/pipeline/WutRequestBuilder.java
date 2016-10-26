package com.wut.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//import com.wut.datasources.cache.WutEhCache;
import com.wut.format.Decoder;
import com.wut.format.FormatFactory;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.support.ErrorHandler;
import com.wut.support.Language;
import com.wut.support.logging.WutLogger;

public class WutRequestBuilder {
	private WutLogger log = new WutLogger(this.getClass());
	private String format = "json";
	private String operation = "read";
	private String resource = "help";
	private InputStream content;
	private Map<String, String> settings = new HashMap<String, String>();
	private Map<String, String> metrics = new HashMap<String, String>();
	private String parameters = "";
	private String userId = "default@default.com";
	private String token = "00000000000000"; // authentication token
	private String application = "diagnostic"; // authentication token
	private String customerId;
	private String id;
	
	public WutRequestBuilder id(String id) {
		this.id = id;
		return this;
	}
	
	public WutRequestBuilder format(String format) {
		this.format = format;
		return this;
	}
	
	public WutRequestBuilder token(String token) {
		this.token = token;
		return this;
	}
	
	public WutRequestBuilder application(String application) {
		this.application = application;
		return this;
	}
	
	public WutRequestBuilder addSetting(String settingName, String settingValue) {
		settings.put(settingName, settingValue);
		return this;
	}
	
	public WutRequestBuilder addMetric(String metricName, String metricValue) {
		metrics.put(metricName, metricValue);
		return this;
	}
	
	public WutRequestBuilder resource(String resource) {
		this.resource = resource;
		return this;
	}

	// TODO parameters should not be input as a string. they should be input in model format.
	public WutRequestBuilder parameters(String params) {
		this.parameters = params;
		return this;
	}

	public WutRequestBuilder content(InputStream content) {
		this.content = content;
		return this;
	}

	public WutRequestBuilder operation(String operation) {
		this.operation = operation;
		return this;
	}
	
	public WutRequest build() {
		MappedData paramData = new MappedData();
		if (!Language.isBlank(parameters)) { // TODO make it so this parameter is always parsed ??? not sure
			//JsonDecoder decoder = new JsonDecoder(); // TODO decouple from JSON. request needs a parameter format (or input format)
			Decoder decoder = FormatFactory.getDecoder(FormatFactory.Format.JSON);
			try {
				Data d = decoder.decode(parameters);
				if (!(d instanceof MappedData)) {
					ErrorHandler.userError("Invalid Parameters:" + parameters);
				} else {
					paramData = (MappedData) d;
				}
			} catch (IOException e) {
				ErrorHandler.systemError(e, "Error building request");
			}
		}
		WutRequest request = new WutRequest(paramData, application, format, operation, resource, id, metrics, userId, content, settings, token, customerId, this);
		log.info(String.valueOf(request));
		return request;
	}

	public WutRequestBuilder setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	public WutRequestBuilder setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}
}

