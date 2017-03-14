package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.support.ErrorHandler;

public class CloudFlareResponse {
	private boolean isSuccess = true;
	private JsonObject result;
	private JsonObject error;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public JsonObject getResult() {
		return result;
	}

	public void setResult(JsonObject result) {
		this.result = result;
	}

	public JsonObject getError() {
		return error;
	}

	public void setErrors(JsonObject error) {
		this.error = error;
	}

	public CloudFlareResponse(HttpResponse response) {
		StringBuilder responseString = new StringBuilder();
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			result = new JsonParser().parse(responseString.toString()).getAsJsonObject();
			if (!result.get("success").getAsString().equals("true")) {
				isSuccess = false;
				JsonArray errors = (JsonArray) result.get("errors");
				error = errors.get(0).getAsJsonObject();
			}
		} catch (IOException e) {
			isSuccess = false;
			error = new JsonObject();
			error.addProperty("msg", "Unable to get CloudFlare response");
			ErrorHandler.userError("Unable to get CloudFlare response", e);
		}
	}
}