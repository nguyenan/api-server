package com.wut.datasources.cloudflare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CDNUtils {
	public static String API_ENDPOINT = "https://api.cloudflare.com/client/v4/zones";

	public static JsonObject setCreateZoneData(String domain) {
		JsonObject postData = new JsonObject();
		postData.addProperty("name", domain);
		postData.addProperty("jump_start", true);
		return postData;
	}

	public static String buildCreateZoneEndpoint() {
		return String.format("%s", API_ENDPOINT);
	}

	public static String buildGetZoneEndpoint() {
		return String.format("%s", API_ENDPOINT);
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
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonObject objResult = new JsonParser().parse(responseString.toString()).getAsJsonObject();
		return objResult;
	}
}
