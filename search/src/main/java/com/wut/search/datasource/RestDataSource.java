package com.wut.search.datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.wut.datasources.DataSource;
import com.wut.model.map.MessageData;
import com.wut.support.exception.WutException;

public class RestDataSource implements DataSource {
	private String url;
	private Map<String,String> headers = new HashMap<String, String>();
	private String body;
	private String method = "GET";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void addHeaders(String headerKey, String headerValue) {
		this.headers.put(headerKey, headerValue);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String sendRequest() {
		StringBuilder response = new StringBuilder();

		try {
			URL url = new URL(this.url);

			//make connection
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			
			// headers
			for (String headerKey : headers.keySet()) {
				String headerValue = headers.get(headerKey);
				httpConnection.setRequestProperty(headerKey, headerValue);
			}
			
			if (method.equalsIgnoreCase("get")) {
				httpConnection.setRequestMethod("GET");
			} else if (method.equalsIgnoreCase("post")) {
				httpConnection.setRequestMethod("POST");
				
				//use post mode
				httpConnection.setDoOutput(true);
				httpConnection.setAllowUserInteraction(false);
				
				//send body
				PrintStream ps = new PrintStream(httpConnection.getOutputStream());
				ps.print(this.body);
				ps.close();
			}
			
			//get result
			BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection
			    .getInputStream()));
			String l = null;
			while ((l=br.readLine())!=null) {
				response.append(l);
			}
			br.close();
			
			httpConnection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			throw new WutException(MessageData.error(e));
		}
        
		return response.toString();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getCurlRequest() {
		StringBuilder curl = new StringBuilder();
		
		// curl url
		curl.append("curl '");
		curl.append(this.url);
		curl.append("'");
		
		// headers
		for (String headerName : headers.keySet()) {
			String headerValue = headers.get(headerName);
			curl.append(" -H '" + headerName + ": " + headerValue + "'"); // Origin: http://localhost'');
		}
		
		if (method.equalsIgnoreCase("POST")) {
			curl.append(" --data-binary '");
			curl.append(body);
			curl.append("'");
			curl.append("  --compressed");
		}
		
		return curl.toString();
	}

}
