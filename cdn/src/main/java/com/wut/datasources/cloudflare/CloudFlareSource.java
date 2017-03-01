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
import com.wut.support.ErrorHandler;
import com.wut.support.domain.DomainUtils;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class CloudFlareSource {
	private static CloudFlareAuth cloudflareAuth;

	private static final String CF_KEY = SettingsManager.getSystemSetting("cloudflare.api.secretkey");
	private static final String CF_EMAIL = SettingsManager.getSystemSetting("cloudflare.api.email");
	private Map<String, String> zoneIds = new HashMap<String, String>();
	private static WutLogger logger = WutLogger.create(CloudFlareSource.class);

	public CloudFlareSource() {
		cloudflareAuth = new CloudFlareAuth(CF_KEY, CF_EMAIL);
	}

	public CloudFlareSource(String key, String email) {
		cloudflareAuth = new CloudFlareAuth(key, email);
	}

	public String getZoneId(String customerDomain) {
		String topLevelDomain = DomainUtils.getTopLevelDomain(customerDomain);
		if (zoneIds.containsKey(topLevelDomain)) { //existed
			logger.info("ZoneId " + customerDomain + " existed...");
			return zoneIds.get(topLevelDomain);
		}
		HttpGet getReq = new HttpGet(CloudFlareUtils.listZoneEndpoint());
		CloudFlareUtils.setCFHeader(getReq, cloudflareAuth);

		CloudFlareResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(getReq);
			cfResponse = new CloudFlareResponse(response);
		} catch (IOException e) {
			ErrorHandler.userError("Unable to get CloudFlare response", e); 
			return null;
		}

		if (cfResponse.isSuccess()) {
			JsonArray cfResult = (JsonArray) cfResponse.getResult().get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					if (topLevelDomain.equals(item.get("name").getAsString())) {
						zoneIds.put(topLevelDomain, item.get("id").getAsString());
						return item.get("id").getAsString();
					}
				}
			}
			return null;
		} else {
			logger.info(cfResponse.getError().toString()); 
			return null;
		}
	}

	public MessageData purgeCache(String customerDomain, String id) {
		try {
			String zoneId = getZoneId(customerDomain);
			// Send request
			HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CloudFlareUtils.purgeCacheEndpoint(zoneId));
			CloudFlareUtils.setCFHeader(deleteReq, cloudflareAuth);
			JsonObject postData = CloudFlareUtils.getPurgeCachedDataObject(CloudFlareUtils.buildPurgeURL(customerDomain, id));
			CloudFlareUtils.setBody(deleteReq, postData);
			CloudFlareResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(deleteReq);
				cfResponse = new CloudFlareResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return MessageData.error(e);
			}

			if (cfResponse.isSuccess()) {
				return MessageData.success();
			} else {
				JsonObject cfError = cfResponse.getError();
				return CloudFlareUtils.returnMessage(cfError);
			}
		} catch (Exception e) {
			return MessageData.error(e);
		}
	}
}