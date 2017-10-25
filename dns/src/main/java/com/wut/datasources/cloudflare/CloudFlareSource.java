package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StringData;
import com.wut.support.domain.DomainUtils;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class CloudFlareSource {
	private static CloudFlareAuth cloudflareAuth;

	private static final String CF_KEY = SettingsManager.getSystemSetting("cloudflare.api.secretkey");
	private static final String CF_EMAIL = SettingsManager.getSystemSetting("cloudflare.api.email");
	private static WutLogger logger = WutLogger.create(CloudFlareSource.class);
	private Map<String, String> zoneIds = new HashMap<String, String>();

	public CloudFlareSource() {
		cloudflareAuth = new CloudFlareAuth(CF_KEY, CF_EMAIL);
	}

	public CloudFlareSource(String key, String email) {
		cloudflareAuth = new CloudFlareAuth(key, email);
	}

	public Data createZone(String customerDomain) {
		JsonArray nsArray = getZoneNameServer(customerDomain);
		if (nsArray != null && nsArray.size() == 2) { // zone was created
			MappedData ret = new MappedData();
			ret.put("ns1", nsArray.get(0).getAsString());
			ret.put("ns2", nsArray.get(1).getAsString());
			return ret;
		}

		// Create new zone
		HttpPost postReq = new HttpPost(CloudFlareUtils.createZoneEndpoint());
		JsonObject postData = CloudFlareUtils.setCreateZoneData(DomainUtils.getTopLevelDomain(customerDomain));
		CloudFlareUtils.setCFHeader(postReq, cloudflareAuth);
		CloudFlareUtils.setBody(postReq, postData);

		CloudFlareResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(postReq);
			cfResponse = new CloudFlareResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (!cfResponse.isSuccess()) {
			JsonObject cfError = cfResponse.getError();
			return CloudFlareUtils.returnCFMessage(cfError);
		}
		JsonObject cfResult = (JsonObject) cfResponse.getResult().get("result");
		zoneIds.put(DomainUtils.getTopLevelDomain(customerDomain), cfResult.get("id").getAsString());
		updateSSL(customerDomain, "flexible");

		MappedData ret = new MappedData();
		nsArray = null;
		nsArray = (JsonArray) cfResult.get("name_servers");
		ret.put("ns1", nsArray.get(0).getAsString());
		ret.put("ns2", nsArray.get(1).getAsString());
		return ret;

	}

	public Data deleteZone(String customerDomain) {
		String zoneId = getZoneId(customerDomain);

		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CloudFlareUtils.deleteZoneEndpoint(zoneId));
		CloudFlareUtils.setCFHeader(deleteReq, cloudflareAuth);

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
			return CloudFlareUtils.returnCFMessage(cfError);
		}
	}

	public JsonObject getZone(String customerDomain) {
		String topLevelDomain = DomainUtils.getTopLevelDomain(customerDomain);
		int currentPage = 0;
		int totalPages = 0;
		do {
			currentPage++;
			HttpGet getReq = new HttpGet(CloudFlareUtils.listZoneEndpoint(currentPage));
			CloudFlareUtils.setCFHeader(getReq, cloudflareAuth);

			CloudFlareResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(getReq);
				cfResponse = new CloudFlareResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if (!cfResponse.isSuccess()) {
				logger.error(cfResponse.getError().toString());
				return null;
			}

			JsonArray cfResult = (JsonArray) cfResponse.getResult().get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					// System.out.println(item.get("name").getAsString());
					if (topLevelDomain.equals(item.get("name").getAsString())) {
						return item;
					}
				}
			}
			JsonObject cfResultInfo = (JsonObject) cfResponse.getResult().get("result_info");
			totalPages = getInt(cfResultInfo, "total_pages");
		} while (currentPage < totalPages);

		return null;
	}

	public Integer getInt(JsonObject obj, String key) {
		JsonElement jsonElement = obj.get("total_pages");
		if (jsonElement != null)
			return Integer.parseInt(jsonElement.toString());
		return -1;
	}

	public JsonArray getZoneNameServer(String customerDomain) {
		JsonObject zone = getZone(customerDomain);
		if (zone != null)
			return (JsonArray) zone.get("name_servers");
		return null;
	}

	public String getZoneId(String customerDomain) {
		String topLevelDomain = DomainUtils.getTopLevelDomain(customerDomain);
		if (zoneIds.containsKey(topLevelDomain)) {
			return zoneIds.get(topLevelDomain);
		}
		JsonObject zone = getZone(customerDomain);
		if (zone != null) {
			zoneIds.put(topLevelDomain, zone.get("id").getAsString());
			return zone.get("id").getAsString();
		}
		return null;
	}

	public String getRecordId(String zoneID, String customerDomain, String recordName) {
		int currentPage = 0;
		int totalPages = 0;
		do {
			currentPage++;
			HttpGet getReq = new HttpGet(CloudFlareUtils.listRecordEndpoint(zoneID, currentPage));
			CloudFlareUtils.setCFHeader(getReq, cloudflareAuth);

			CloudFlareResponse cfResponse;
			System.out.println(getReq.toString());
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(getReq);
				cfResponse = new CloudFlareResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if (!cfResponse.isSuccess()) {
				logger.error(cfResponse.getError().toString());
				return null;
			}
			JsonArray cfResult = (JsonArray) cfResponse.getResult().get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					if (recordName.equals(item.get("name").getAsString())
							|| (recordName + "." + customerDomain).equals(item.get("name").getAsString())) {
						return item.get("id").getAsString();
					}
				}
			}
			JsonObject cfResultInfo = (JsonObject) cfResponse.getResult().get("result_info");
			totalPages = getInt(cfResultInfo, "total_pages");
		} while (currentPage < totalPages);

		return null;
	}

	public Data getRecordDetails(String customerDomain, String recordName) {
		String zoneId = getZoneId(customerDomain);
		if (zoneId == null)
			return ErrorData.ZONE_NOT_FOUND;

		String recordId = getRecordId(zoneId, customerDomain, recordName);
		if (recordId == null)
			return ErrorData.RECORD_NOT_FOUND;

		HttpGet getReq = new HttpGet(CloudFlareUtils.detailRecordEndpoint(zoneId, recordId));
		CloudFlareUtils.setCFHeader(getReq, cloudflareAuth);

		CloudFlareResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(getReq);
			cfResponse = new CloudFlareResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (!cfResponse.isSuccess()) {
			JsonObject cfError = cfResponse.getError();
			return CloudFlareUtils.returnCFMessage(cfError);
		}
		MappedData ret = new MappedData();
		JsonObject cfResult = (JsonObject) cfResponse.getResult().get("result");
		for (Entry<String, JsonElement> entry : cfResult.entrySet()) {
			if (!entry.getValue().isJsonObject()) {
				ret.put(entry.getKey().toString(), entry.getValue().getAsString());
			}
		}
		return ret;
	}

	public Data createRecord(String customerDomain, String recordName, String content) {
		String zoneId = getZoneId(customerDomain);

		HttpPost postReq = new HttpPost(CloudFlareUtils.createRecordEndpoint(zoneId));
		CloudFlareUtils.setCFHeader(postReq, cloudflareAuth);

		JsonObject postData = CloudFlareUtils.setRecordData(recordName, content);
		CloudFlareUtils.setBody(postReq, postData);

		CloudFlareResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(postReq);
			cfResponse = new CloudFlareResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (cfResponse.isSuccess()) {
			if (recordName.equals(normalizeDomain(customerDomain))) {
				List<PageRule> defaultPagerules = defaultPagerules(customerDomain);
				for (PageRule rule : defaultPagerules) {
					createPagerule(customerDomain, zoneId, rule);
				}
			}
			return MessageData.success();
		}

		JsonObject cfError = cfResponse.getError();
		int errorCode = cfError.get("code").getAsInt();
		switch (errorCode) {
		case 81053: // record already exists
			// return ErrorData.RECORD_EXISTED;
			return updateRecord(customerDomain, recordName, content);
		case 1004:
			return ErrorData.RECORD_INVALID;
		default:
			return CloudFlareUtils.returnCFMessage(cfError);
		}
	}

	public Data updateRecord(String customerDomain, String recordName, String content) {
		String zoneId = getZoneId(customerDomain);
		if (zoneId == null)
			return ErrorData.ZONE_NOT_FOUND;

		String recordId = getRecordId(zoneId, customerDomain, recordName);
		if (recordId == null)
			return ErrorData.RECORD_NOT_FOUND;

		HttpPutWithBody putReq = new HttpPutWithBody(CloudFlareUtils.updateRecordEndpoint(zoneId, recordId));
		CloudFlareUtils.setCFHeader(putReq, cloudflareAuth);

		JsonObject postData = CloudFlareUtils.setRecordData(recordName, content);
		CloudFlareUtils.setBody(putReq, postData);

		CloudFlareResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(putReq);
			cfResponse = new CloudFlareResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (cfResponse.isSuccess()) {
			if (recordName.equals(normalizeDomain(customerDomain))) {
				List<PageRule> defaultPagerules = defaultPagerules(customerDomain);
				for (PageRule rule : defaultPagerules) {
					createPagerule(customerDomain, zoneId, rule);
				}
			}
			return MessageData.success();
		} else {
			JsonObject cfError = cfResponse.getError();
			return CloudFlareUtils.returnCFMessage(cfError);
		}
	}
	
	public Data deleteRecord(String customerDomain, String recordName) {
		String zoneId = getZoneId(customerDomain);
		if (zoneId == null)
			return ErrorData.ZONE_NOT_FOUND;

		String recordId = getRecordId(zoneId, customerDomain, recordName);
		if (recordId == null)
			return ErrorData.RECORD_NOT_FOUND;

		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CloudFlareUtils.deleteRecordEndpoint(zoneId, recordId));
		CloudFlareUtils.setCFHeader(deleteReq, cloudflareAuth);

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
			return CloudFlareUtils.returnCFMessage(cfError);
		}
	}

	public Data updateSSL(String customerDomain, String sslValue) {
		try {
			String zoneId = getZoneId(customerDomain);

			HttpPatchWithBody patchReq = new HttpPatchWithBody(CloudFlareUtils.updateSSL(zoneId));
			CloudFlareUtils.setCFHeader(patchReq, cloudflareAuth);

			JsonObject postData = CloudFlareUtils.setSSLData(sslValue);
			CloudFlareUtils.setBody(patchReq, postData);

			CloudFlareResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(patchReq);
				cfResponse = new CloudFlareResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return MessageData.error(e);
			}

			if (cfResponse.isSuccess()) {
				return MessageData.success();
			} else {
				JsonObject cfError = cfResponse.getError();
				return CloudFlareUtils.returnCFMessage(cfError);
			}
		} catch (Exception e) {
			return null;
		}
	}

	public String createPagerule(String customerDomain, String zoneId, PageRule rule) {
		try {
			HttpPost postReq = new HttpPost(CloudFlareUtils.createPageRuleEndpoint(zoneId));
			CloudFlareUtils.setCFHeader(postReq, cloudflareAuth);

			JsonObject postData = CloudFlareUtils.setPageRulesData(rule);
			CloudFlareUtils.setBody(postReq, postData);

			CloudFlareResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(postReq);
				cfResponse = new CloudFlareResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if (cfResponse.isSuccess()) {
				JsonObject cfResult = (JsonObject) cfResponse.getResult().get("result");
				return cfResult.get("id").toString();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private List<PageRule> defaultPagerules(String customerDomain) {
		List<PageRule> pagerules = new ArrayList<PageRule>();
		String topLevelDomain = DomainUtils.getTopLevelDomain(customerDomain);
		// #1. Apply https
		String URLPatternFrom1 = String.format("http://*%s/*", topLevelDomain);
		String URLPatternTo1 = String.format("https://$1%s/$2", topLevelDomain);

		MappedData fwToHttps = new MappedData();
		fwToHttps.put("url", URLPatternTo1);
		fwToHttps.put("status_code", new IntegerData(301));

		Action httpsAction = new Action("forwarding_url", fwToHttps);
		Integer priority = 1;
		pagerules.add(new PageRule(URLPatternFrom1, new Action[] { httpsAction }, priority));

		// #2. Add www
		if (customerDomain.startsWith("www.")) {
			String URLPatternFrom2 = String.format("https://%s/*", topLevelDomain);
			String URLPatternTo2 = String.format("https://%s/$1", customerDomain);

			MappedData fwTowww = new MappedData();
			fwTowww.put("url", URLPatternTo2);
			fwTowww.put("status_code", new IntegerData(301));

			Action fwAction = new Action("forwarding_url", fwTowww);
			pagerules.add(new PageRule(URLPatternFrom2, new Action[] { fwAction }));
		}

		// #3. Apply cache
		Action cacheAction1 = new Action("browser_cache_ttl", new IntegerData(60 * 60 * 4));// 4
																							// hours
		Action cacheAction2 = new Action("cache_level", new StringData("cache_everything"));
		String cacheURLPattern = String.format("https://*%s/*", topLevelDomain);
		pagerules.add(new PageRule(cacheURLPattern, new Action[] { cacheAction1, cacheAction2 }));

		return pagerules;
	}

	public String normalizeDomain(String domainName) {
		return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
	}

}