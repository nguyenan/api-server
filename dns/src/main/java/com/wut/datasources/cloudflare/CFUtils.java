package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.model.map.MessageData;

public class CFUtils {
	public static String API_ENDPOINT = "https://api.cloudflare.com/client/v4/zones";

	public static JsonObject setCreateZoneData(String domain) {
		JsonObject postData = new JsonObject();
		postData.addProperty("name", domain);
		postData.addProperty("jump_start", true);
		return postData;
	}

	public static JsonObject setRecordData(String name, String content) {
		JsonObject postData = new JsonObject();
		postData.addProperty("type", "CNAME");
		postData.addProperty("name", name);
		postData.addProperty("content", content);
		postData.addProperty("ttl", 1);
		postData.addProperty("proxied", true);
		return postData;
	}

	public static JsonObject setSSLData(String value) {
		JsonObject postData = new JsonObject();
		postData.addProperty("value", value);
		return postData;
	}
	
	public static JsonObject setPageRulesData(PageRule rule) {
		JsonObject constraintObj = new JsonObject();
		constraintObj.addProperty("operator", "matches");
		constraintObj.addProperty("value", rule.getUrlPattern());

		JsonObject targetObj = new JsonObject();
		targetObj.add("constraint", constraintObj);
		targetObj.addProperty("target", "url");

		JsonArray targetsArr = new JsonArray();
		targetsArr.add(targetObj);

		// action
		JsonArray actionsArr = new JsonArray();
		for (Action item : rule.getActions()) {
			actionsArr.add(item.toJsonObject());
		}

		JsonObject postData = new JsonObject();
		postData.add("targets", targetsArr);
		postData.add("actions", actionsArr);
		if (rule.getPriority() > 0)
		postData.addProperty("priority", rule.getPriority());
		postData.addProperty("status", "active");
		return postData;
	}

	public static String listZoneEndpoint() {
		return String.format("%s?per_page=200", API_ENDPOINT);
	}

	public static String createZoneEndpoint() {
		return String.format("%s", API_ENDPOINT);
	}

	public static String deleteZoneEndpoint(String zoneId) {
		return String.format("%s/%s", API_ENDPOINT, zoneId);
	}
	public static String listRecordEndpoint(String zoneId) {
		return String.format("%s/%s/dns_records?per_page=100&type=CNAME", API_ENDPOINT, zoneId);
	}

	public static String detailRecordEndpoint(String zoneId, String recordId) {
		return String.format("%s/%s/dns_records/%s?type=CNAME", API_ENDPOINT, zoneId, recordId);
	}

	public static String createRecordEndpoint(String zoneId) {
		return String.format("%s/%s/dns_records", API_ENDPOINT, zoneId);
	}

	public static String updateRecordEndpoint(String zoneId, String recordId) {
		return String.format("%s/%s/dns_records/%s", API_ENDPOINT, zoneId, recordId);
	}
	
	public static String updateSSL(String zoneId) {
		return String.format("%s/%s/settings/ssl", API_ENDPOINT, zoneId);
	}
	
	public static String createPageRuleEndpoint(String zoneId) {
		return String.format("%s/%s/pagerules", API_ENDPOINT, zoneId);
	}

	public static void setCFHeader(HttpRequestBase req, CFAuth cloudflareAuth) {
		req.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		req.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		req.setHeader("Content-Type", "application/json");
	}

	public static void setBody(HttpPost req, JsonObject postData) {
		req.setEntity(new StringEntity(postData.toString(), "UTF-8"));
	}
	
	public static void setBody(HttpPatchWithBody req, JsonObject postData) {
		req.setEntity(new StringEntity(postData.toString(), "UTF-8"));
	} 
	public static void setBody(HttpPutWithBody req, JsonObject postData) {
		req.setEntity(new StringEntity(postData.toString(), "UTF-8"));
	}

	public static JsonObject parseCFResponse(HttpResponse response) {
		StringBuilder responseString = new StringBuilder();
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonObject objResult = new JsonParser().parse(responseString.toString()).getAsJsonObject();
		return objResult;
	}

	public static MessageData returnCFMessage(JsonObject cfErrordetail) {
		return MessageData.error(cfErrordetail.toString().replaceAll("\"", "\\\\\""));
	}
}
