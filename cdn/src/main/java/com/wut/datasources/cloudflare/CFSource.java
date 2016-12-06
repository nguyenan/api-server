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

	public CFSource(String key, String email) {
		cloudflareAuth = new CFAuth(key, email);
	}
	public MessageData purgeCache(String customerDomain, String id) {
		String zoneId = getZoneID(customerDomain);
		if (zoneId == null) {
			return MessageData.error("get zoneId error");
		}
		if (zoneId.isEmpty()) {
			return MessageData.error("zoneId not found");
		}

		// Send request
		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CDNUtils.buildPurgeCacheEndpoint(zoneId));
		CDNUtils.setCFHeader(deleteReq, cloudflareAuth);
		JsonObject postData = CDNUtils.setPurgeURL(CDNUtils.buildPurgeURL(customerDomain, id));
		deleteReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(deleteReq);
			JsonObject objResult = CDNUtils.parseCFResponse(response);
			if (!objResult.has("success") || !objResult.get("success").getAsString().equals("true")) {
				return MessageData.error(objResult.get("errors").toString());
			}
			return MessageData.success();
		} catch (UnsupportedEncodingException | ClientProtocolException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	public String getZoneID(String customerDomain) {
		if (zoneIdMap.containsKey(customerDomain))
			return zoneIdMap.get(customerDomain);

		// Send request
		HttpGet getReq = new HttpGet(CDNUtils.buildGetZoneEndpoint(customerDomain));
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject objResult = CDNUtils.parseCFResponse(response);
			if (!objResult.has("success") || !objResult.get("success").getAsString().equals("true")) {
				System.out.println("getZoneID error" + objResult.get("errors").toString());
				return null;
			}
			JsonArray resultArr = (JsonArray) objResult.get("result");
			for (Object obj : resultArr) {
				if (obj instanceof JsonObject) {
					JsonObject item = (JsonObject) obj;
					if (customerDomain.equals(item.get("name").getAsString())) {
						zoneIdMap.put(customerDomain, item.get("id").getAsString());
						return item.get("id").getAsString();
					}
				}
			}
			return "";
		} catch (UnsupportedEncodingException | ClientProtocolException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

}