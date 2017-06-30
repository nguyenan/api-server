package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
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

	public Data createZone(String customerDomain) {
		JsonArray nsArray = new JsonArray();
		HttpPost postReq = new HttpPost(CFUtils.createZoneEndpoint());
		CFUtils.setCFHeader(postReq, cloudflareAuth);

		JsonObject postData = CFUtils.setCreateZoneData(DomainUtils.getTopLevelDomain(customerDomain));
		CFUtils.setBody(postReq, postData);

		CFResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(postReq);
			cfResponse = new CFResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (cfResponse.isSuccess()) {
			JsonObject cfResult = (JsonObject) cfResponse.getCFReturn().get("result");
			nsArray = (JsonArray) cfResult.get("name_servers");
			zoneIds.put(DomainUtils.getTopLevelDomain(customerDomain), cfResult.get("id").getAsString());
			updateSSL(customerDomain, "flexible");
		} else {
			JsonObject cfError = cfResponse.getError();
			int errorCode = cfError.get("code").getAsInt();
			switch (errorCode) {
			case 1049: // this domain is not a registered domain
				return ErrorData.INVALID_DOMAIN;
			case 1061: // domain already exists
			case 90: // already exists but CF mark as temporarily banned
						// (perhaps because we tried to add too many times)
				nsArray = getZoneNameServer(DomainUtils.getTopLevelDomain(customerDomain));
			default:
				break;
			}
		}
		if (nsArray != null && nsArray.size() == 2) {
			MappedData ret = new MappedData();
			ret.put("ns1", nsArray.get(0).getAsString());
			ret.put("ns2", nsArray.get(1).getAsString());
			return ret;
		} else {
			JsonObject cfError = cfResponse.getError();
			return CFUtils.returnCFMessage(cfError);
		}
	}

	public Data deleteZone(String customerDomain) {
		String zoneId = getZoneId(customerDomain);

		HttpDeleteWithBody deleteReq = new HttpDeleteWithBody(CFUtils.deleteZoneEndpoint(zoneId));
		CFUtils.setCFHeader(deleteReq, cloudflareAuth);

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
	}

	public JsonArray getZoneNameServer(String customerDomain) {
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
						return (JsonArray) item.get("name_servers");
					}
				}
			}
			return null;
		} else {
			System.out.println(cfResponse.getError().toString());
			return null;
		}
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

	public String getRecordId(String zoneID, String customerDomain, String recordName) {
		HttpGet getReq = new HttpGet(CFUtils.listRecordEndpoint(zoneID));
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
					if (recordName.equals(item.get("name").getAsString())
							|| (recordName + "." + customerDomain).equals(item.get("name").getAsString())) {
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

	public Data getRecordDetails(String customerDomain, String recordName) {
		String zoneId = getZoneId(customerDomain);
		String recordId = getRecordId(zoneId, customerDomain, recordName);

		HttpGet getReq = new HttpGet(CFUtils.detailRecordEndpoint(zoneId, recordId));
		CFUtils.setCFHeader(getReq, cloudflareAuth);

		CFResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(getReq);
			cfResponse = new CFResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}

		if (cfResponse.isSuccess()) {
			MappedData ret = new MappedData();
			JsonObject cfResult = (JsonObject) cfResponse.getCFReturn().get("result");
			for (Entry<String, JsonElement> entry : cfResult.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					ret.put(entry.getKey().toString(), entry.getValue().getAsString());
				}
			}
			return ret;
		} else {
			JsonObject cfError = cfResponse.getError();
			return CFUtils.returnCFMessage(cfError);
		}
	}

	public Data createRecord(String customerDomain, String recordName, String content) {
		String zoneId = getZoneId(customerDomain);

		HttpPost postReq = new HttpPost(CFUtils.createRecordEndpoint(zoneId));
		CFUtils.setCFHeader(postReq, cloudflareAuth);

		JsonObject postData = CFUtils.setRecordData(recordName, content);
		CFUtils.setBody(postReq, postData);

		CFResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(postReq);
			cfResponse = new CFResponse(response);
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
			return CFUtils.returnCFMessage(cfError);
		}
	}

	public Data updateRecord(String customerDomain, String recordName, String content) {
		String zoneId = getZoneId(customerDomain);
		String recordId = getRecordId(zoneId, customerDomain, recordName);

		HttpPutWithBody putReq = new HttpPutWithBody(CFUtils.updateRecordEndpoint(zoneId, recordId));
		CFUtils.setCFHeader(putReq, cloudflareAuth);

		JsonObject postData = CFUtils.setRecordData(recordName, content);
		CFUtils.setBody(putReq, postData);

		CFResponse cfResponse;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(putReq);
			cfResponse = new CFResponse(response);
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
			return CFUtils.returnCFMessage(cfError);
		}
	}

	public Data updateSSL(String customerDomain, String sslValue) {
		try {
			String zoneId = getZoneId(customerDomain);

			HttpPatchWithBody patchReq = new HttpPatchWithBody(CFUtils.updateSSL(zoneId));
			CFUtils.setCFHeader(patchReq, cloudflareAuth);

			JsonObject postData = CFUtils.setSSLData(sslValue);
			CFUtils.setBody(patchReq, postData);

			CFResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(patchReq);
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

	public String createPagerule(String customerDomain, String zoneId, PageRule rule) {
		try {
			HttpPost postReq = new HttpPost(CFUtils.createPageRuleEndpoint(zoneId));
			CFUtils.setCFHeader(postReq, cloudflareAuth);

			JsonObject postData = CFUtils.setPageRulesData(rule);
			CFUtils.setBody(postReq, postData);

			CFResponse cfResponse;
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				CloseableHttpResponse response = client.execute(postReq);
				cfResponse = new CFResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if (cfResponse.isSuccess()) {
				JsonObject cfResult = (JsonObject) cfResponse.getCFReturn().get("result");
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
		if (customerDomain.startsWith("www.")){
			String URLPatternFrom2 = String.format("https://%s/*", topLevelDomain);
			String URLPatternTo2 = String.format("https://%s/$1", customerDomain);
			
			MappedData fwTowww = new MappedData();
			fwTowww.put("url", URLPatternTo2);
			fwTowww.put("status_code", new IntegerData(301));
			
			Action fwAction = new Action("forwarding_url", fwTowww); 
			pagerules.add(new PageRule(URLPatternFrom2, new Action[] { fwAction }));
		}		

		// #3. Apply cache
		Action cacheAction1 = new Action("browser_cache_ttl", new IntegerData(60 * 60 * 4));// 4 hours
		Action cacheAction2 = new Action("cache_level", new StringData("cache_everything"));
		String cacheURLPattern = String.format("https://*%s/*", topLevelDomain);
		pagerules.add(new PageRule(cacheURLPattern, new Action[] { cacheAction1, cacheAction2 }));

		return pagerules;
	}

	public String normalizeDomain(String domainName) {
		return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
	}

}