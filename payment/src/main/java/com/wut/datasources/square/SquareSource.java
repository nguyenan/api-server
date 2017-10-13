package com.wut.datasources.square;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class SquareSource {

	private String accesToken;
	private static WutLogger logger = WutLogger.create(SquareSource.class);
	private final static String getTokenURL = "https://connect.squareup.com/oauth2/token";
	private final static String REDIRECT_URL = SettingsManager.getSystemSetting("square.client_id");;
	private final static String APPLICATION_ID = SettingsManager.getSystemSetting("square.client_id");
	private final static String APPLICATION_SECRET = SettingsManager.getSystemSetting("square.client_secret");

	public SquareSource(String accesToken) {
		this.accesToken = accesToken;
	}

	public SquareSource() {
	}

	public Data getAccessToken(String customer, StringData authorizationCode) {
		HttpPostWithBody postReq = new HttpPostWithBody(getTokenURL);

		postReq.setHeader("Content-Type", "application/json");

		JsonObject postData = new JsonObject();
		postData.addProperty("client_id", APPLICATION_ID);
		postData.addProperty("client_secret", APPLICATION_SECRET);
		postData.addProperty("redirect_uri", REDIRECT_URL);
		postData.addProperty("code", authorizationCode.toRawString());

		postReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(postReq);
			JsonObject response2 = getResponse(response);
			JsonElement access_token = response2.get("access_token");
			if (access_token != null) {
				SettingsManager.setClientSettings(customer, "payment.square.authorization-code",
						authorizationCode.toRawString());
				SettingsManager.setClientSettings(customer, "payment.square.access-token", access_token.toString());
				return MessageData.SUCCESS;
			} else
				return ErrorData.error(response2.get("message").toString().replace("\"", ""));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return MessageData.FAILURE;
		}
	}

	public JsonObject getResponse(HttpResponse response) {
		StringBuilder responseString = new StringBuilder();
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			JsonObject result = new JsonParser().parse(responseString.toString()).getAsJsonObject();
			return result;
		} catch (IOException e) {
			JsonObject error = new JsonObject();
			error.addProperty("msg", "Unable to get Square response");
			return error;
		}

	}
}
