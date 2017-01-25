package com.wut.provider.dns;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.Data;
import com.wut.provider.Provider;

public class DNSProvider implements Provider {
	private CFSource cfSource;

	public DNSProvider(CFSource pcfSource) {
		this.cfSource = pcfSource;
	}

	public Data createZone(String customerDomain) {
		return cfSource.createZone(customerDomain);
	}

	public Data deleteZone(String customerDomain) {
		return cfSource.deleteZone(customerDomain);
	}

	public Data createRecord(String customerDomain, String name, String content) {
		return cfSource.createRecord(customerDomain, name, content);
	}

	public Data updateRecord(String customerDomain, String name, String content) {
		return cfSource.updateRecord(customerDomain, name, content);
	}

	public Data getRecordDetails(String customerDomain, String name) {
		return cfSource.getRecordDetails(customerDomain, name);
	}
}
