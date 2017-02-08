package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.wut.model.map.MessageData;
import com.wut.support.domain.DomainUtils;
import com.wut.support.settings.SettingsManager;

public class CFSource {
	private static CFAuth cloudflareAuth;

	private static final String CF_KEY = SettingsManager.getSystemSetting("cloudflare.api.secretkey");
	private static final String CF_EMAIL = SettingsManager.getSystemSetting("cloudflare.api.email");
	private Map<String, String> zoneIds = new HashMap<String, String>();

	public CFSource() {
		cloudflareAuth = new CFAuth(CF_KEY, CF_EMAIL);
	}

	public CFSource(String key, String email) {
		cloudflareAuth = new CFAuth(key, email);
	}

	public String getZoneId(String customerDomain) {
		if (zoneIds.containsKey(DomainUtils.getTopLevelDomain(customerDomain))) {
			System.out.println("existed");
			return zoneIds.get(DomainUtils.getTopLevelDomain(customerDomain));
		}
		HttpGet getReq = new HttpGet(CFUtils.listZoneEndpoint());
		CFUtils.setCFHeader(getReq, cloudflareAuth);

		CFResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(getReq);
			cfResponse = new CFResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (cfResponse.isSuccess()) {
			JsonArray cfResult = (JsonArray) cfResponse.getCFReturn().get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					if (DomainUtils.getTopLevelDomain(customerDomain).equals(item.get("name").getAsString())) {
						zoneIds.put(DomainUtils.getTopLevelDomain(customerDomain), item.get("id").getAsString());
						return item.get("id").getAsString();
					}
				}
			}
			return null;
		} else {
			System.out.println(cfResponse.getError().toString());
			return null;
		}
	}

	public MessageData purgeCache(String customerDomain, String id) throws UnsupportedOperationException {
		try {
			String zoneId = getZoneId(customerDomain);
			// Send request
			HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CFUtils.purgeCacheEndpoint(zoneId));
			CFUtils.setCFHeader(deleteReq, cloudflareAuth);
			JsonObject postData = CFUtils.setPurgeCacheData(CFUtils.buildPurgeURL(customerDomain, id));
			CFUtils.setBody(deleteReq, postData);
			CFResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(deleteReq);
				cfResponse = new CFResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return MessageData.error(e);
			}

			if (cfResponse.isSuccess()) {
				return MessageData.success();
			} else {
				JsonObject cfError = cfResponse.getError();
				return CFUtils.returnCFMessage(cfError);
			}
		} catch (Exception e) {
			return null;
		}
	}

	public String normalizeDomain(String domainName) {
		return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
	}

}