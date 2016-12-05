package com.wut.datasources.cloudflare;

import java.util.HashMap;
import java.util.Map;

public class CDNUtils {
	private static Map<String, String> zoneIdMap = new HashMap<>();

	public static String buildPurgeURL(String domain, String path) {
		return String.format("https://%s/%s", domain, path);
	}

	public static String buildPurgeCacheEndpoint(String zoneId) {
		return String.format("%s/%s/%s", CFConstants.API_ENDPOINT, zoneId, CFConstants.ACTION_PURGE_CACHE);
	}
	public static String buildGetZoneEndpoint(String domain) {
		return String.format("%s", CFConstants.API_ENDPOINT);
	}

//	public static String getZoneID(String customerDomain) {
//		if (zoneIdMap.containsKey(customerDomain))
//			return zoneIdMap.get(customerDomain);
//		return "";
//		}
}
