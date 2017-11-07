package com.wut.datasources.square;

import java.io.Serializable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BillingResponse {
	private static String successCode = "0_1_s";
	private static String failCode = "0_1_f";
	private MetaInfo meta = new MetaInfo();
	private JsonObject data;

	public BillingResponse() {

	}

	public BillingResponse(MetaInfo meta) {
		this.meta = meta;

	}

	public BillingResponse(JsonObject result) {
		if (result.has("meta")) {
			JsonObject metadata = new JsonParser().parse(result.get("meta").toString()).getAsJsonObject();
			this.meta = new MetaInfo(metadata);

		}
		if (result.has("data") && !result.get("data").toString().equals("null")) {
			JsonObject data = new JsonParser().parse(result.get("data").toString()).getAsJsonObject();
			this.data = data;
		}
	}

	public static BillingResponse fail(String errormsg) {
		BillingResponse billingResponse = new BillingResponse();
		billingResponse.getMeta().setCode(failCode);
		billingResponse.getMeta().setMessage(errormsg);
		return billingResponse;
	}

	public boolean isSuccess() {
		if (meta == null)
			return false;
		else
			return meta.getCode().equals(successCode);
	}

	public MetaInfo getMeta() {
		return meta;
	}

	public void setMeta(MetaInfo meta) {
		this.meta = meta;
	}

	public JsonObject getData() {
		return data;
	}

	public void setData(JsonObject data) {
		this.data = data;
	}

	public class MetaInfo implements Serializable {
		private static final long serialVersionUID = 570925035213747308L;
		private String code;
		private String message;

		public String getCode() {
			return code;
		}

		public MetaInfo(JsonObject metadata) {
			super();
			this.code = metadata.get("code").getAsString();
			this.message = metadata.get("message").getAsString();
		}

		public MetaInfo(String code, String message) {
			super();
			this.code = code;
			this.message = message;
		}

		public MetaInfo() {
			// TODO Auto-generated constructor stub
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
