package com.wut.model.map;

import com.wut.model.Data;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.ScalarData;

// TODO make this class immutable
public class MessageData extends MappedData {
	
	//private static final long serialVersionUID = -5373420936202243031L;
	
	public MessageData(MessageData msgDataToCopy) {
		for (ScalarData id : msgDataToCopy.getMap().keySet()) {
			String key = String.valueOf(id);
			Data d = msgDataToCopy.get(key);
			put(key, d);
		}
	}
	
	public MessageData(int id, String name, String desc) {
		//put("id", String.valueOf(id));
     	put("code", new IntegerData(id));
		put("name", name);
		put("message", desc);
	}
	
	public MessageData context(String contextMsg) {
		put("context", contextMsg);
		return this;
	}
	
	// TODO deprecate in future
	//@Deprecated
	public MessageData(String msg) {
		this(1, "default-message", msg);
	}
	
	public MessageData(String msg, Data data) {
		this(1, msg, "");
		setData(data);
	}
	
	// TODO rename this customError() and create a new one based off failure
	// might need to create a copy constructor for this
	@Deprecated
	public static MessageData error() {
		return new MessageData(FAILURE);
	}
	
	public static MessageData error(Exception e) {
		return new MessageData(908070, "exception", e.getMessage());
	}
	
	public void setData(Data data) {
		put("data", data);
	}
	
	public void setCode(int code) {
     	put("code", new IntegerData(code));
	}
	
	public static MessageData success() {
		return SUCCESS;
	}
	
	public static MessageData info(String infoMessage) {
		return new MessageData(1500, "info", infoMessage);
	}
	
	/**
	 * returns an appropriate data on success / failure. if wasSucessful true then "success". if
	 * wasSucessful false then "failure"
	 * @param wasSucessful
	 * @return
	 */
	public static Data successOrFailure(boolean wasSucessful) {
		return wasSucessful ? SUCCESS : FAILURE;
	}

	public static final MessageData EMPTY = new MessageData(90, "emtpy", "please contact your administrator for additional support on this error");
	public static final MessageData SUCCESS = new MessageData(100, "success", "request successful");
	public static final MessageData SERVER_ERROR = new MessageData(101, "server error", "request successful");
	public static final MessageData FAILURE = new MessageData(120, "failure", "request failed");
	public static final MessageData NO_DATA_FOUND = new MessageData(130, "no data", "no data found");
	public static final MessageData NOT_IMPLEMENTED = new MessageData(140, "not implemented", "functionality requested not implemented");
	public static final MessageData UNKNOWN_USER = new MessageData(150, "unknown user", "the system was not able to find the requested user");
	public static final MessageData CARD_DECLINED = new MessageData(170, "card declined", "the provided card was declined");
	public static final MessageData INVALID_PERMISSIONS = new MessageData(180, "invalid permissions", "the current user does not have permissions to complete this operation");
	public static final MessageData USER_EXISTS = new MessageData(190, "user exists", "the requested user already exists");
	public static final MessageData PAYMENT_PROBLEM = new MessageData(200, "payment problem", "there was a problem processing payment");
	public static final MessageData EMAIL_SEND_PROBLEM = new MessageData(300, "email problem", "there was a problem processing your email");
	public static final MessageData INVALID_SETTING = new MessageData(400, "invalid setting", "the setting specified was not recognized");
	public static final MessageData INSUFFIENT_PRIVILEGES = new MessageData(500, "insufficient priveleges", "the user does not have sufficient priveleges to complete the requested operation");
	public static final MessageData TEMPLATE_NOT_INTIALIZED = new MessageData(600, "requires intialization", "client templates not initialized");
	public static final MessageData CACHE_OUT_OF_DATE = new MessageData(700, "cache out of date", "well shucks, the cache is old so i better not return anything");
	public static final MessageData CACHE_CDN_ERROR = new MessageData(701, "cdn error", "problem connecting to cdn");

	//@Deprecated
	public static MessageData error(String string) {
		//return FAILURE;
		MessageData error = new MessageData(EMPTY);
		error.put("message", string);
		return error;
	}

	public static MessageData returnCode(int returnCode) {
		if (returnCode == 0) {
			return SUCCESS;
		} else {
			// TODO remove this dependency
			return new MessageData(120, "failure", "request failed with return code " + returnCode);
		}
	}

	public static Data successOrFailure(BooleanData isSuccess) {
		return isSuccess.equals(BooleanData.TRUE) ? MessageData.SUCCESS : MessageData.FAILURE;
	}

	public int getCode() {
		IntegerData code = (IntegerData) get("code");
		int codeNativeType = code.getInteger();
		return codeNativeType;
	}
	
	

}
