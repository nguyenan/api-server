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
}
