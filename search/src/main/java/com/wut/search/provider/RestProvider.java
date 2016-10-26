package com.wut.search.provider;

import java.net.URLDecoder;
import java.util.Map;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.model.scalar.UriEncodedData;
import com.wut.provider.Provider;
import com.wut.search.datasource.RestDataSource;

public class RestProvider implements Provider {
	
	static {
//		System.setProperty("java.protocol.handler.pkgs",
//		        "com.sun.net.ssl.internal.www.protocol");
//		   Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
		/*
	    // Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
		};

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		*/
	}

	
	public Data sendRequest(StringData url, StringData method, MappedData headers, StringData body) {
		RestDataSource call = new RestDataSource();
		call.setUrl(url.toRawString());
		call.setMethod(method.toRawString());

		String bodyToDecode;
		if (body == null) {
			bodyToDecode = "";
		} else {
			bodyToDecode = body.toRawString();
		}
		String decodedBody = URLDecoder.decode(bodyToDecode);
		call.setBody(decodedBody);
		//getCall.addHeaders("Content-Type", "application/xml");
		//getCall.addHeaders("Accept", "application/json");
		
		if (headers != null) {
			Map<String, String> headerMapAsPojo = headers.getMapAsPojo();
			for (String headerName : headerMapAsPojo.keySet()) {
				String headerValue = headerMapAsPojo.get(headerName);
				call.addHeaders(headerName, headerValue);
			}
		}
		
		System.out.println("Executing:\n" + call.getCurlRequest());
		
		String output = call.sendRequest();
		if (output != null) {
			return UriEncodedData.createFromUnencodedString(output);
		} else {
			return MessageData.FAILURE;
		}
	}
	
}
