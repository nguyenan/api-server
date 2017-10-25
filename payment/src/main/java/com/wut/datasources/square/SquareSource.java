package com.wut.datasources.square;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StringData;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class SquareSource {
	private static WutLogger logger = WutLogger.create(SquareSource.class);
	private final static String getTokenURL = "https://connect.squareup.com/oauth2/token";
	private final static String renewTokenURL = "https://connect.squareup.com/oauth2/clients/%s/access-token/renew"; // {client_id}
	private final static String getLocationURL = "https://connect.squareup.com/v2/locations";
	private final static String transactionURL = "https://connect.squareup.com/v2/locations/%s/transactions"; // {location}

	private static final String SQUARE_CLIENT_SERECT = SettingsManager.getSystemSetting("square.client_secret");
	private static final String SQUARE_APP_ID = SettingsManager.getSystemSetting("square.client_id");
	// private final static String REDIRECT_URL =
	// SettingsManager.getSystemSetting("square.redirect_url");

	public static final String ACCESS_TOKEN_SETTING = "payment.square.access-token";
	public static final String AUTHORIZE_CODE_SETTING = "payment.square.authorization-code";

	public SquareSource() {
	}

	public MappedData getAccessToken(String customer, StringData authorizationCode) {
		HttpPostWithBody paymentReq = new HttpPostWithBody(getTokenURL);
		paymentReq.setHeader("Content-Type", "application/json");

		JsonObject postData = new JsonObject();
		postData.addProperty("client_id", SQUARE_APP_ID);
		postData.addProperty("client_secret", SQUARE_CLIENT_SERECT);
		// postData.addProperty("redirect_uri", REDIRECT_URL);
		postData.addProperty("code", authorizationCode.toRawString());
		paymentReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		JsonObject JSONResponse = makeRequest(paymentReq);
		if (JSONResponse == null)
			return ErrorData.SQUARE_HTTP_ERROR;

		if (JSONResponse.get("access_token") == null)
			return ErrorData.error(JSONResponse.get("message").getAsString());

		JsonElement accessToken = JSONResponse.get("access_token");
		MessageData result =  new MessageData(100, "success", "request successful");
		result.put("access_token", accessToken.getAsString());
		return result;

	}

	public MappedData renewAccessToken(String customer, String oldAccessToken) {
		HttpPostWithBody paymentReq = new HttpPostWithBody(String.format(renewTokenURL, SQUARE_APP_ID));
		paymentReq.setHeader("Accept", "application/json");
		paymentReq.setHeader("Content-Type", "application/json");
		paymentReq.setHeader("Authorization", "Client " + SQUARE_CLIENT_SERECT);

		JsonObject postData = new JsonObject();
		postData.addProperty("access_token", oldAccessToken);
		paymentReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		JsonObject JSONResponse = makeRequest(paymentReq);
		if (JSONResponse == null)
			return ErrorData.SQUARE_HTTP_ERROR;

		if (JSONResponse.get("access_token") == null)
			return ErrorData.error(JSONResponse.get("message").getAsString());

		JsonElement accessToken = JSONResponse.get("access_token");
		MessageData result = new MessageData(100, "success", "request successful");
		result.put("access_token", accessToken.getAsString());
		return result;
	}

	public MappedData charge(String accessToken, String cardNonce, Integer amount) {
		String lookupActiveLocation = lookupActiveLocation(accessToken);
		HttpPostWithBody paymentReq = new HttpPostWithBody(String.format(transactionURL, lookupActiveLocation));
		paymentReq = addHeader(paymentReq, accessToken);

		JsonObject postData = new JsonObject();
		postData.addProperty("card_nonce", cardNonce);
		postData.addProperty("idempotency_key", UUID.randomUUID().toString());

		JsonObject amountMoney = new JsonObject();
		amountMoney.addProperty("amount", amount);
		amountMoney.addProperty("currency", "USD");
		postData.add("amount_money", amountMoney);

		paymentReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		JsonObject JSONResponse = makeRequest(paymentReq);
		if (JSONResponse == null)
			return MessageData.SQUARE_HTTP_ERROR;
		if (JSONResponse.get("transaction") == null) {
			JsonArray errors = JSONResponse.get("errors").getAsJsonArray();
			return ErrorData.error(errors.get(0).toString().replace("\"", "\\\""));
		} else {
			MappedData result = new MappedData();
			JsonObject transaction = JSONResponse.get("transaction").getAsJsonObject();
			result.put("code", new IntegerData(MessageData.SUCCESS.getCode()));
			result.put("id", transaction.get("id").getAsString());
			return result;
		}

	} 

	public String lookupActiveLocation(String accessToken) {
		HttpGet paymentReq = new HttpGet(getLocationURL);
		paymentReq = addHeader(paymentReq, accessToken);

		JsonObject JSONResponse = makeRequest(paymentReq);
		if (JSONResponse == null)
			return "";
		if (JSONResponse.get("locations") == null) {
			logger.error(JSONResponse.get("errors").getAsString());
			return "";
		}

		JsonArray asJsonArray = JSONResponse.get("locations").getAsJsonArray();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonObject location = asJsonArray.get(i).getAsJsonObject();
			String status = location.get("status").getAsString();
			if (status.equals("ACTIVE"))
				return location.get("id").getAsString();
		}
		return "";
	}

	public JsonObject makeRequest(HttpRequestBase req) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(req);
			JsonObject jsonResponse = getResponse(response);
			System.out.println(jsonResponse);
			return jsonResponse;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
	}

	public HttpGet addHeader(HttpGet req, String accessToken) {
		req.setHeader("Accept", "application/json");
		req.setHeader("Content-Type", "application/json");
		req.setHeader("Authorization", "Bearer " + accessToken);
		return req;
	}

	public HttpPostWithBody addHeader(HttpPostWithBody req, String accessToken) {
		req.setHeader("Accept", "application/json");
		req.setHeader("Content-Type", "application/json");
		req.setHeader("Authorization", "Bearer " + accessToken);
		return req;
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
