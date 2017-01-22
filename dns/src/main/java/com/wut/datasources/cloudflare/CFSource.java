package com.wut.datasources.cloudflare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
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

	public Data createZone(String customerDomain) throws UnsupportedOperationException {
		// Send request
		JsonArray nsArray = new JsonArray();
		HttpPost postReq = new HttpPost(CDNUtils.createZoneEndpoint());
		CDNUtils.setCFHeader(postReq, cloudflareAuth);
		JsonObject postData = CDNUtils.setCreateZoneData(DomainUtils.getTopLevelDomain(customerDomain));
		postReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = client.execute(postReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (cfReturn.get("success").getAsString().equals("true")) {
				JsonObject cfResult = (JsonObject) cfReturn.get("result");
				nsArray = (JsonArray) cfResult.get("name_servers");
			} else {
				JsonArray cfError = (JsonArray) cfReturn.get("errors");
				JsonObject cfErrordetail = cfError.get(0).getAsJsonObject();
				int errorCode = cfErrordetail.get("code").getAsInt();
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
				return MessageData.error(cfReturn.get("errors").toString());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}
	}

	public JsonArray getZoneNameServer(String customerDomain) {
		// Send request
		HttpGet getReq = new HttpGet(CDNUtils.listZoneEndpoint());
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (!cfReturn.has("success") || !cfReturn.get("success").getAsString().equals("true")) {
				System.out.println("getZoneNameServer error" + cfReturn.get("errors").toString());
				return null;
			}
			JsonArray cfResult = (JsonArray) cfReturn.get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					if (DomainUtils.getTopLevelDomain(customerDomain).equals(item.get("name").getAsString())) {
						return (JsonArray) item.get("name_servers");
					}
				}
			}
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		}
	}

	public String getZoneId(String customerDomain) {
		// Send request
		HttpGet getReq = new HttpGet(CDNUtils.listZoneEndpoint());
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (!cfReturn.has("success") || !cfReturn.get("success").getAsString().equals("true")) {
				System.out.println("getZoneID error" + cfReturn.get("errors").toString());
				return null;
			}
			JsonArray cfResult = (JsonArray) cfReturn.get("result");
			for (Object zone : cfResult) {
				if (zone instanceof JsonObject) {
					JsonObject item = (JsonObject) zone;
					if (DomainUtils.getTopLevelDomain(customerDomain).equals(item.get("name").getAsString())) {
						return item.get("id").getAsString();
					}
				}
			}
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		}
	}

	public String getRecordId(String zoneID, String customerDomain, String recordName) {
		// Send request
		HttpGet getReq = new HttpGet(CDNUtils.listRecordEndpoint(zoneID));
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (!cfReturn.has("success") || !cfReturn.get("success").getAsString().equals("true")) {
				System.out.println("getRecordId error" + cfReturn.get("errors").toString());
				return null;
			}
			JsonArray cfResult = (JsonArray) cfReturn.get("result");
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		}
	}

	public Data getRecordDetails(String customerDomain, String recordName) {
		// Send request
		String zoneId = getZoneId(customerDomain);
		String recordId = getRecordId(zoneId, customerDomain, recordName);
		HttpGet getReq = new HttpGet(CDNUtils.detailRecordEndpoint(zoneId, recordId, recordName));
		CDNUtils.setCFHeader(getReq, cloudflareAuth);
		CloseableHttpClient client = HttpClients.createDefault();
		MappedData ret = new MappedData();
		try {
			HttpResponse response = client.execute(getReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			System.out.println(getReq.getURI());
			if (!cfReturn.has("success") || !cfReturn.get("success").getAsString().equals("true")) {
				System.out.println("getRecordDetails error" + cfReturn.get("errors").toString());
				return null;
			}
			System.out.println(cfReturn.get("result"));
			JsonObject cfResult = (JsonObject) cfReturn.get("result");
			for (Entry<String, JsonElement> entry : cfResult.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					ret.put(entry.getKey().toString(), entry.getValue().getAsString());
				}
			}
			return ret;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			return null;
		}
	}

	public Data createRecord(String customerDomain, String recordName, String content)
			throws UnsupportedOperationException {
		// Send request
		String zoneId = getZoneId(customerDomain);
		HttpPost postReq = new HttpPost(CDNUtils.createRecordEndpoint(zoneId));
		CDNUtils.setCFHeader(postReq, cloudflareAuth);
		JsonObject postData = CDNUtils.setRecordData(recordName, content);
		postReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = client.execute(postReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (cfReturn.get("success").getAsString().equals("true")) {
				return MessageData.success();
			} else {
				return MessageData.error(cfReturn.get("errors").toString());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}
	}

	public Data updateRecord(String customerDomain, String recordName, String content)
			throws UnsupportedOperationException {
		// Send request
		String zoneId = getZoneId(customerDomain);
		String recordId = getRecordId(zoneId, customerDomain, recordName);
		HttpPutWithBody postReq = new HttpPutWithBody(CDNUtils.updateRecordEndpoint(zoneId, recordId));
		CDNUtils.setCFHeader(postReq, cloudflareAuth);
		JsonObject postData = CDNUtils.setRecordData(recordName, content);
		postReq.setEntity(new StringEntity(postData.toString(), "UTF-8"));
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = client.execute(postReq);
			JsonObject cfReturn = CDNUtils.parseCFResponse(response);
			if (cfReturn.get("success").getAsString().equals("true")) {
				return MessageData.success();
			} else {
				return MessageData.error(cfReturn.get("errors").toString());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return MessageData.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			return MessageData.error(e);
		}
	}
}