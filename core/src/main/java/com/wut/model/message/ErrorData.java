package com.wut.model.message;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;

public class ErrorData extends MessageData {

	private Data innerData;
	
	public ErrorData(Exception e) {
		super(e.getMessage());
		innerData = new StringData(e.getMessage());
	}
	
	public ErrorData(int code, String message) {
		super(code, message, "no description of this error");
		innerData = new StringData(message);
	}
	
	public ErrorData(String message) {
		super(5999, message, "no description of this error");
		innerData = new StringData(message);
	}
	
	public Data getData() {
		return innerData;
	}

	public static final ErrorData GENERIC_ERROR = new ErrorData(5001, "error completing request");
	public static final ErrorData INVALID_RESOURCE = new ErrorData(5002, "resource not found");
	public static final ErrorData INTERNAL_RESOURCE_ERROR = new ErrorData(5003, "error occured while completing request for specified resource");
	public static final ErrorData INVALID_OPERATION = new ErrorData(5004, "operation not found");
	public static final ErrorData INVALID_PARAMETERS = new ErrorData(5005, "parameters provided are invalid for resource/operation");
	public static final ErrorData INVALID_FORMAT = new ErrorData(5006, "invalid format");
	public static final ErrorData OPERATION_ERROR = new ErrorData(5007, "error completing operation");
	public static final Data OP_NOT_IMPLIMENTED = new ErrorData(5008, "operation no implemented");
	public static final Data AUTHENTICATION_ERROR = new ErrorData(5009, "authentication error");
	public static final Data WTF = new ErrorData(5010, "WTF");
	public static final Data WTF2 = new ErrorData(5011, "DOUBLE WTF!!!!");
	public static final Data INVALID_LOGIN = new ErrorData(5012, "Invalid Login");
	public static final Data INVALID_TOKEN = new ErrorData(5013, "Invalid Token");	
	public static final Data FORMAT_PARSE_ERROR = new ErrorData(5014, "error parsing formatted data");
	public static final Data MISSING_OPERATION = new ErrorData(5015, "missing operation");
	public static final Data INVALID_DOMAIN = new ErrorData(5016, "domain not found");
	public static final Data NOT_INITIALIZED = new ErrorData(5040, "client templates not initialized");	
	public static final Data CATASTROPHIC = new ErrorData(5666, "catastrophic error occurred");

}
