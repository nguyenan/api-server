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
import java.util.Iterator;
import java.util.List;
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
			if (recordName.equals("www") || recordName.equals("www." + customerDomain)) {
				List<PageRule> defaultPagerules = defaultPagerules(customerDomain);
				for (PageRule rule : defaultPagerules) {
					createPagerule(customerDomain, rule);
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
			if (recordName.equals("www") || recordName.equals("www." + customerDomain)) {
				List<PageRule> defaultPagerules = defaultPagerules(customerDomain);
				for (PageRule rule : defaultPagerules) {
					createPagerule(customerDomain, rule);
				}
			}
			return MessageData.success();
		} else {
			JsonObject cfError = cfResponse.getError();
			return CFUtils.returnCFMessage(cfError);
		}
	}

	public String createPagerule(String customerDomain, PageRule rule) {
		String zoneId = getZoneId(customerDomain);
		HttpPost postReq = new HttpPost(CFUtils.createPageRuleEndpoint(zoneId));
		CFUtils.setCFHeader(postReq, cloudflareAuth);

		JsonObject postData = CFUtils.setPageRulesData(rule);
		System.out.println(postData.toString());
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
	}

	private List<PageRule> defaultPagerules(String customerDomain) {
		List<PageRule> pagerules = new ArrayList<PageRule>();
		Action cacheAction1 = new Action("browser_cache_ttl", new IntegerData(86400));// 1 day
		Action cacheAction2 = new Action("cache_level", new StringData("cache_everything"));
		String cacheURLPattern = String.format("https://www.%s/*", normalizeDomain(customerDomain));

		pagerules.add(new PageRule(cacheURLPattern, new Action[] { cacheAction1, cacheAction2 }));

		MappedData fwData = new MappedData();
		fwData.put("url", String.format("https://www.%s/$1", normalizeDomain(customerDomain)));
		fwData.put("status_code", new IntegerData(301));
		Action fwAction = new Action("forwarding_url", fwData);
		String fwURLPattern = String.format("https://%s/*", normalizeDomain(customerDomain));

		pagerules.add(new PageRule(fwURLPattern, new Action[] { fwAction }));

		// The "Always Use HTTPS" option will only appear if your zone has an active SSL certificate associated with it on Cloudflare
		// Action httpsAction = new Action("always_use_https", new StringData("on"));
		// String httpsURLPattern = String.format("https://www.%s/*", normalizeDomain(customerDomain));
		MappedData httpsData = new MappedData();
		httpsData.put("url", String.format("https://www.%s/$1", normalizeDomain(customerDomain)));
		httpsData.put("status_code", new IntegerData(301));
		Action httpsAction = new Action("forwarding_url", httpsData);
		String httpsURLPattern = String.format("http://www.%s/*", normalizeDomain(customerDomain));

		pagerules.add(new PageRule(httpsURLPattern, new Action[] { httpsAction }));
		return pagerules;
	}

	public String normalizeDomain(String domainName) {
		return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
	}

}