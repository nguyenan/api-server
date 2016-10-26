package com.wut.datasources.rest;

// TODO bring this back

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class RestSource {
	
	public static void get(String[] args) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://identity.api.rackspacecloud.com/v2.0/tokens");

		// https://identity.api.rackspacecloud.com/v2.0/tokens
		// {"auth":{"RAX-KSKEY:apiKeyCredentials":{"username":"your rackspace cloud user","apiKey":"your rackspace cloud api key"}}}

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}
	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		//curl -X POST -d @auth.xml -H "Content-Type: application/xml" -H "Accept: application/xml" https://auth.api.rackspacecloud.com/v1.1/auth
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://auth.api.rackspacecloud.com/v1.1/auth");
		StringEntity input = new StringEntity("<?xml version=\"1.0\" encoding=\"UTF-8\"?><credentials xmlns=\"http://docs.rackspacecloud.com/auth/api/v1.1\" username=\"russellpalmiter\" key=\"REPLACE_ME\" />\n");
		post.setEntity(input);
		post.setHeader("Content-Type", "application/xml");
		post.setHeader("Accept", "application/xml");
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder result = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			result.append(line);
		}
		
	}

}
*/