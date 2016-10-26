package com.wut.support.domain;

public class DomainUtils {
	public static String getRealDomain(String client) {
		if (client.indexOf('.') == client.lastIndexOf('.')) {
			return "www." + client;
		} else {
			return client;
		}
	}
	
	public static String getTopLevelDomain(String domain) {
		if (domain.indexOf('.') == domain.lastIndexOf('.')) {
			return domain;
		} else {
			int lastDot = domain.lastIndexOf('.');
			int secondToLastDot =  domain.substring(0, lastDot).lastIndexOf('.');
			int domainLength = domain.length();
			String topLevelDomain = domain.substring(secondToLastDot+1, domainLength);
			return topLevelDomain;
		}
	}
}
