package com.wut.support.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.support.settings.SystemSettings;

public class StackTraceData extends MappedData {
	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");
	private static final String _SUCCESS = String.valueOf(MessageData.SUCCESS.getCode());
	private static final String _FAILURE = String.valueOf(MessageData.FAILURE.getCode());

	private static final String ERROR_ID = "errorId";
	private static final String DATE = "date";
	private static final String UPDATED = "updated";

	private static final String CODE = "code";
	private static final String NAME = "name";
	private static final String MESSAGE = "message";
	private static final String REQUEST = "request";
	private static final String STACKTRACE = "stackTrace";

	private static MappedData WUT_REQUEST = new MappedData();

	public StackTraceData(StackTraceData msgDataToCopy) {
		for (ScalarData id : msgDataToCopy.getMap().keySet()) {
			String key = String.valueOf(id);
			Data d = msgDataToCopy.get(key);
			put(key, d);
		}
	}

	// Constructor
	public StackTraceData(String errorCode) {
		put(CODE, errorCode);
	}

	// Getter and Setter

	private void setCode(String errorCode) {
		put(CODE, errorCode);
	}

	private void setName(String name) {
		put(NAME, name);
	}

	private void setMessage(String message) {
		put(MESSAGE, message);
	}

	public void setStacktrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stackTraceStr = sw.toString();
		put(STACKTRACE, stackTraceStr.replaceAll("\n", "").replaceAll("\t", "    "));

	}

	public void save() {
		if (get(STACKTRACE) != null) {
			put(REQUEST, WUT_REQUEST.toString().replace("\"", "\\\""));
			put(DATE, String.valueOf(System.currentTimeMillis()));
			put(UPDATED, String.valueOf(System.currentTimeMillis()));

			StackTraceStore stackTraceStore = new StackTraceStore();
			IdData logId = stackTraceStore.create(adminCustId, "core", this.getMapAsPojo());
			put(ERROR_ID, logId.toRawString());

			remove(new StringData(STACKTRACE));
			remove(new StringData(REQUEST));
			remove(new StringData(DATE));
			remove(new StringData(UPDATED));
		}
	}

	public void setWutRequest(WutRequest req) {
		MappedData wutRequest = new MappedData();
		MappedData parameters = new MappedData();
		parameters.put("token", req.getToken());
		parameters.put("customerId", req.getCustomer());
		parameters.put("userId", req.getUserId());
		parameters.put("operation", req.getAction());
		parameters.put("application", req.getApplication());
		parameters.put("noCache", req.getSetting("noCache"));
		parameters.put("format", req.getFormat());
		wutRequest.put("parameters", parameters);

		MappedData body = req.getParameters();
		wutRequest.put("body", body);
		WUT_REQUEST = wutRequest;
	}

	//
	public static StackTraceData error(String errorCode, String name, String message, WutRequest req, Exception e) {
		StackTraceData stackTraceData = new StackTraceData(_FAILURE);
		if (errorCode != null)
			stackTraceData.setCode(errorCode);
		if (name != null)
			stackTraceData.setName(name);
		if (message != null)
			stackTraceData.setMessage(message);
		if (e != null)
			stackTraceData.setStacktrace(e);
		if (req != null)
			stackTraceData.setWutRequest(req);
		return stackTraceData;
	}

	public static StackTraceData returnCode(int returnCode) {
		if (returnCode == 0) {
			return success();
		} else {
			StackTraceData stackTraceData = new StackTraceData(_FAILURE);
			stackTraceData.setMessage("request failed with return code " + returnCode);
			return stackTraceData;
		}
	}

	public static StackTraceData errorMsg(String message) {
		StackTraceData stackTraceData = new StackTraceData(_FAILURE);
		stackTraceData.setMessage(message);
		return stackTraceData;
	}

	public static StackTraceData success() {
		StackTraceData stackTraceData = new StackTraceData(_SUCCESS);
		stackTraceData.put(NAME, MessageData.SUCCESS.get(NAME));
		stackTraceData.put(MESSAGE, MessageData.SUCCESS.get(MESSAGE));
		return stackTraceData;
	}

	public boolean isSuccess() {
		String errorCode = this.get(CODE).toString();
		return (errorCode.equals(_SUCCESS));
	}
}
