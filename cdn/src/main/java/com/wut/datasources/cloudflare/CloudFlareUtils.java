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
import com.wut.support.ErrorHandler;

public class CloudFlareUtils {
	public static String API_ENDPOINT = "https://api.cloudflare.com/client/v4/zones";

	public static final String getCacheStatus(String url) {
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
			ErrorHandler.userError("Unable to get CloudFlare response", e);
		}
		return null;
	}

	public static final JsonObject getPurgeCachedDataObject(List<String> URLs) {
		JsonArray listPurge = new JsonArray();
		for (String url : URLs) {
			listPurge.add(url);
		}
		JsonObject postData = new JsonObject();
		postData.add("files", listPurge);
		return postData;
	}

	public static final String purgeCacheEndpoint(String zoneId) {
		return String.format("%s/%s/%s", API_ENDPOINT, zoneId, "purge_cache");
	}

	public static final String listZoneEndpoint() {
		// TODO: implement read list by page
		return String.format("%s?per_page=200", API_ENDPOINT);
	}

	public static final void setCFHeader(HttpRequestBase req, CloudFlareAuth cloudflareAuth) {
		req.setHeader("X-Auth-Email", cloudflareAuth.getEmail());
		req.setHeader("X-Auth-Key", cloudflareAuth.getKey());
		req.setHeader("Content-Type", "application/json");
	}

	public static final void setBody(HttpDeleteWithBody req, JsonObject postData) {
		req.setEntity(new StringEntity(postData.toString(), "UTF-8"));
	}

	public static final JsonObject parseCFResponse(HttpResponse response) {
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

	public static final MessageData returnMessage(JsonObject errDetail) {
		return MessageData.error(errDetail.toString().replaceAll("\"", "\\\\\""));
	}

	public static final List<String> buildPurgeURL(String domain, String path) {
		List<String> URLs = new ArrayList<String>();
		URLs.add(String.format("http://%s/%s", domain, path));
		URLs.add(String.format("https://%s/%s", domain, path));
		return URLs;
	}

	public static final String buildHttpsPurgeURL(String domain, String path) {
		return String.format("https://%s/%s", domain, path);
	}

}
