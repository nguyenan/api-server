package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.model.map.MessageData;
import com.wut.support.settings.SettingsManager;

public class CFSource {
	private static CFAuth cloudflareAuth;

	private static final String CF_KEY = SettingsManager.getSystemSetting("cloudflare.api.secretkey");
	private static final String CF_EMAIL = SettingsManager.getSystemSetting("cloudflare.api.email");
	private static Map<String, String> zoneIdMap = new HashMap<>();

	public CFSource() {
		cloudflareAuth = new CFAuth(CF_KEY, CF_EMAIL);
	}

	public MessageData purgeCache(String customerDomain, String id) {
		String zoneId = getZoneID(customerDomain);
		if (zoneId == null || zoneId.isEmpty()) {
			System.out.println("get zoneId error, customerDomain: " + customerDomain);
			return MessageData.FAILURE;
		}

		// Send request
		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CDNUtils.buildPurgeCacheEndpoint(zoneId));
		CDNUtils.setCFHeader(deleteReq, cloudflareAuth);
		JsonObject postData = setPurgeURL(CDNUtils.buildPurgeURL(customerDomain, id));
		deleteReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(deleteReq);
			JsonObject objResult = CDNUtils.parseCFResponse(response);
			if (!objResult.has("success")) {
				return MessageData.CACHE_CDN_ERROR;
			}
			Boolean isSuccess = Boolean.valueOf(objResult.get("success").toString());
			if (!isSuccess)
				System.out.print(objResult.toString());
			return MessageData.SUCCESS;
		} catch (UnsupportedEncodingException | ClientProtocolException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}
		return MessageData.FAILURE;
	}

	public static String getZoneID(String customerDomain) {
		if (zoneIdMap.containsKey(customerDomain))
			return zoneIdMap.get(customerDomain);

		// Send request
		HttpGet getReq = new HttpGet(CDNUtils.buildGetZoneEndpoint(customerDomain));
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject objResult = CDNUtils.parseCFResponse(response);
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
							zoneIdMap.put(customerDomain, ((JsonObject) obj).get("id").toString());
							break;
						}
					}
				}
			} else {
				System.out.print("FAIL " + objResult.toString());
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
		return zoneIdMap.get(customerDomain);
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
}