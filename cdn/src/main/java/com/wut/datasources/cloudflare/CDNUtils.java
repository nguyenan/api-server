package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CDNUtils {

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

	public static void main(String[] args) {

		String id = "http://www.mapiii.com/index.html"; // file to purge
		System.out.println(getCFCacheStatus(id));

	}

	public static JsonObject setPurgeURL(String URL) {
		JsonArray listPurge = new JsonArray();
		listPurge.add(URL);
		JsonObject postData = new JsonObject();
		postData.add(CFConstants.PURGE_FILE, listPurge);
		return postData;
	}

	public static JsonObject setPurgeURL(List<String> URLs) {
		JsonArray listPurge = new JsonArray();
		for (String url : URLs) {
			listPurge.add(url);
		}
		JsonObject postData = new JsonObject();
		postData.add(CFConstants.PURGE_FILE, listPurge);
		return postData;
	}

	public static String buildHttpPurgeURL(String domain, String path) {
		domain = domain.startsWith("www.") ? domain : ("www." + domain);
		return String.format("http://%s/%s", domain, path);
	}

	public static List<String> buildPurgeURL(String domain, String path) {
		domain = domain.startsWith("www.") ? domain : ("www." + domain);
		List<String> URLs = new ArrayList<>();
		URLs.add(String.format("http://%s/%s", domain, path));
		URLs.add(String.format("https://%s/%s", domain, path));
		return URLs;
	}

	public static String buildPurgeCacheEndpoint(String zoneId) {
		return String.format("%s/%s/%s", CFConstants.API_ENDPOINT, zoneId, CFConstants.ACTION_PURGE_CACHE);
	}

	public static String buildGetZoneEndpoint(String domain) {
		return String.format("%s", CFConstants.API_ENDPOINT);
	}

	public static void setCFHeader(HttpRequestBase req, CFAuth cloudflareAuth) {
		req.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		req.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		req.setHeader("Content-Type", "application/json");
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
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}

		JsonObject objResult = new JsonParser().parse(responseString.toString()).getAsJsonObject();
		return objResult;
	}
}
