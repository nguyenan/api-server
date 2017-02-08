package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wut.model.map.MessageData;

public class CFUtils {
	public static String API_ENDPOINT = "https://api.cloudflare.com/client/v4/zones";

	public static String getCFCacheStatus(String url) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				if (header.getName().equals("CF-Cache-Status"))
					return header.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject setPurgeCacheData(List<String> URLs) {
		JsonArray listPurge = new JsonArray();
		for (String url : URLs) {
			listPurge.add(url);
		}
		JsonObject postData = new JsonObject();
		postData.add("files", listPurge);
		return postData;
	}

	public static String purgeCacheEndpoint(String zoneId) {
		return String.format("%s/%s/%s", API_ENDPOINT, zoneId, "purge_cache");
	}

	public static String listZoneEndpoint() {
		// TODO: implement read list by page
		return String.format("%s?per_page=200", API_ENDPOINT);
	}

	public static void setCFHeader(HttpRequestBase req, CFAuth cloudflareAuth) {
		req.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		req.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		req.setHeader("Content-Type", "application/json");
	}

	public static void setBody(HttpDeleteWithBody req, JsonObject postData) {
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

	public static List<String> buildPurgeURL(String domain, String path) {
		domain = domain.startsWith("www.") ? domain : ("www." + domain);
		List<String> URLs = new ArrayList<String>();
		URLs.add(String.format("http://%s/%s", domain, path));
		URLs.add(String.format("https://%s/%s", domain, path));
		return URLs;
	}

	public static String buildHttpsPurgeURL(String domain, String path) {
		domain = domain.startsWith("www.") ? domain : ("www." + domain);
		return String.format("https://%s/%s", domain, path);
	}

}
