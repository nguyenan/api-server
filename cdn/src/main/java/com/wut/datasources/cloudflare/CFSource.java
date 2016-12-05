package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.support.settings.SettingsManager;

public class CFSource {
	private static CFAuth cloudflareAuth;

	private static final String CF_KEY = SettingsManager.getSystemSetting("cloudflare.api.secretkey");
	private static final String CF_EMAIL = SettingsManager.getSystemSetting("cloudflare.api.email");

	public CFSource() {
		cloudflareAuth = new CFAuth(CF_KEY, CF_EMAIL);
	}

	public JsonObject setPurgeURL(String URL) {
		JsonArray listPurge = new JsonArray();
		listPurge.add(URL);
		JsonObject postData = new JsonObject();
		postData.add(CFConstants.PURGE_FILE, listPurge);
		return postData;
	}

	public JsonObject setPurgeURL(List<String> URLs) {
		JsonArray listPurge = new JsonArray();
		for (String url : URLs) {
			listPurge.add(url);
		}
		JsonObject postData = new JsonObject();
		postData.add(CFConstants.PURGE_FILE, listPurge);
		return postData;
	}

	public boolean purgeCache(String customerDomain, String id) {
		String fullURL = CDNUtils.buildPurgeURL(customerDomain, id);
		String zoneId = getZoneID(customerDomain);

		JsonObject postData = setPurgeURL(fullURL);
		String requestURL = CDNUtils.buildPurgeCacheEndpoint(zoneId);

		// Send request
		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(requestURL);
		deleteReq.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		deleteReq.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		deleteReq.setHeader("Content-Type", "application/json");
		deleteReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		HttpClient client = new DefaultHttpClient();
		try {
			StringBuilder responseString = new StringBuilder();
			HttpResponse response = client.execute(deleteReq);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			JsonObject objResult = new JsonParser().parse(responseString.toString()).getAsJsonObject();
			if (!objResult.has("success")) {
				System.out.println("CF error");
				return false;
			}
			String result = objResult.get("success").toString();
			switch (result) {
			case "true":
				System.out.print(objResult.toString());
				return true;
			case "false":
				System.out.print(objResult.toString());
				break;
			default:
				System.out.print(objResult.toString());
				break;
			}
		} catch (UnsupportedEncodingException | ClientProtocolException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}
		return false;
	}

	public static String getZoneID(String customerDomain) {
		// Send request
		HttpGet deleteReq = new HttpGet(CDNUtils.buildGetZoneEndpoint(customerDomain));
		deleteReq.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		deleteReq.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		deleteReq.setHeader("Content-Type", "application/json");
		HttpClient client = new DefaultHttpClient();
		try {
			StringBuilder responseString = new StringBuilder();
			HttpResponse response = client.execute(deleteReq);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			JsonObject objResult = new JsonParser().parse(responseString.toString()).getAsJsonObject();
			if (!objResult.has("success")) {
				System.out.println("CF error");
				return null;
			}
			String isSuccess = objResult.get("success").toString();
			if (isSuccess == "true") {
				JsonArray resultArr = (JsonArray) objResult.get("result");
				for (Object obj : resultArr) {
					if (obj instanceof JsonObject) {						
						if (customerDomain == ((JsonObject) obj).get("name").toString()) {
							return ((JsonObject) obj).get("id").toString();
						}
					}
				}
				System.out.print("No data");
				return null;
			} else {
				System.out.print("FAIL "+objResult.toString());
				return null;
			}
		} catch (UnsupportedEncodingException | ClientProtocolException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}
}