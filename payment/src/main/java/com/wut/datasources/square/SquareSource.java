package com.wut.datasources.square;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StringData;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class SquareSource {
	private static WutLogger logger = WutLogger.create(SquareSource.class);
	private final static String BILLING_API = "http://billing-api.endpoints.practyce-gctesting.cloud.goog";
	// private final static String BILLING_API = "http://localhost:8084";
	private final static String getTokenURL = BILLING_API + "/api/payment/getaccesstoken";
	private final static String transactionURL = BILLING_API + "/api/payment/transaction";
	private final static String refundURL = BILLING_API + "/api/payment/transaction/refund";
	private final static String renewTokenURL = BILLING_API + "/api/payment/renewaccesstoken";

	public static final String ACCESS_TOKEN_SETTING = "payment.square.access-token";
	public static final String AUTHORIZE_CODE_SETTING = "payment.square.authorization-code";

	public SquareSource() {
	}

	public MappedData getAccessToken(String customer, StringData authorizationCode) {
		HttpPostWithBody authenticateReq = new HttpPostWithBody(getTokenURL);
		authenticateReq = addBillingHeader(authenticateReq);

		JsonObject postData = new JsonObject();
		postData.addProperty("authorizateCode", authorizationCode.toRawString());
		authenticateReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		BillingResponse resp = makeRequest(authenticateReq);
		if (!resp.isSuccess())
			return MessageData.error(resp.getMeta().getMessage().replace("\"", "\\\""));

		String accessToken = resp.getData().get("accessToken").getAsString();
		MessageData result = new MessageData(100, "success", "request successful");
		result.put("access_token", accessToken);
		return result;

	}

	public MappedData renewAccessToken(String customer, String oldAccessToken) {
		HttpPostWithBody renewTokenReq = new HttpPostWithBody(renewTokenURL);
		renewTokenReq = addBillingHeader(renewTokenReq);

		JsonObject postData = new JsonObject();
		postData.addProperty("accessToken", oldAccessToken);
		renewTokenReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		BillingResponse resp = makeRequest(renewTokenReq);
		if (!resp.isSuccess())
			return MessageData.error(resp.getMeta().getMessage().replace("\"", "\\\""));

		String accessToken = resp.getData().get("accessToken").getAsString();
		MessageData result = new MessageData(100, "success", "request successful");
		result.put("access_token", accessToken);
		return result;
	}

	public MappedData charge(String accessToken, String cardNonce, Integer amount) {
		HttpPostWithBody paymentReq = new HttpPostWithBody(transactionURL);
		paymentReq = addBillingHeader(paymentReq);

		JsonObject postData = new JsonObject();
		postData.addProperty("cardNonce", cardNonce);
		postData.addProperty("amount", amount);
		postData.addProperty("accessToken", accessToken);

		paymentReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		BillingResponse resp = makeRequest(paymentReq);
		if (!resp.isSuccess())
			return MessageData.error(resp.getMeta().getMessage().replace("\"", "\\\""));

		MappedData result = new MappedData();
		result.put("code", new IntegerData(MessageData.SUCCESS.getCode()));
		result.put("id", resp.getData().get("paymentTransactionId").getAsString());
		result.put("status", resp.getData().get("status").getAsString());
		result.put("tenderId", resp.getData().get("tenderId").getAsString());
		return result;

	}

	public MappedData refund(String accessToken, String paymentTransactionId, String tenderId, Integer amount) {
		HttpPostWithBody paymentReq = new HttpPostWithBody(refundURL);
		paymentReq = addBillingHeader(paymentReq);

		JsonObject postData = new JsonObject();
		postData.addProperty("tenderId", tenderId);
		postData.addProperty("paymentTransactionId", paymentTransactionId);
		postData.addProperty("amount", amount);
		postData.addProperty("accessToken", accessToken);

		paymentReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		BillingResponse resp = makeRequest(paymentReq);
		if (!resp.isSuccess())
			return MessageData.error(resp.getMeta().getMessage().replace("\"", "\\\""));

		MappedData result = new MappedData();
		result.put("code", new IntegerData(MessageData.SUCCESS.getCode()));
		result.put("id", resp.getData().get("paymentTransactionId").getAsString());
		result.put("status", resp.getData().get("status").getAsString());
		return result;

	}

	public BillingResponse makeRequest(HttpRequestBase req) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(req);
			BillingResponse resp = getBillingResponse(response);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return BillingResponse.fail(e.getMessage());
		}
	}

	public HttpGet addHeader(HttpGet req, String accessToken) {
		req.setHeader("Accept", "application/json");
		req.setHeader("Content-Type", "application/json");
		req.setHeader("Authorization", "Bearer " + accessToken);
		return req;
	}

	public HttpPostWithBody addBillingHeader(HttpPostWithBody req) {
		req.setHeader("Accept", "application/json");
		req.setHeader("Content-Type", "application/json");
		req.setHeader("X-Auth-Token", "Basic "
				+ "aGEubmd1eWVuQHByYWN0eWNlLmNvbTp2cGxxbGt1aDhqcHBjaDBycTl6M3FqZGhiMDNzMndkNUY3OTJGQjVCRkNBOUM5QUJGN0IxRDUxNDk1MjQ5MzlDMTc4QjM1OEE2RkY3RDY3Njk3RDVBNjhENEU2RDBDODA6dXNlcg==");
		return req;
	}

	public BillingResponse getBillingResponse(HttpResponse response) {
		StringBuilder responseString = new StringBuilder();
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			JsonObject result = new JsonParser().parse(responseString.toString()).getAsJsonObject();
			BillingResponse billingResponse = new BillingResponse(result);
			return billingResponse;
		} catch (Exception e) {
			return BillingResponse.fail("Unable to get Square response");
		}

	}
}
