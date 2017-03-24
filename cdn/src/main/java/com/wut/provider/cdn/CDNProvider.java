package com.wut.provider.cdn;

import com.wut.datasources.cloudflare.CloudFlareSource;
import com.wut.model.map.MessageData;
import com.wut.provider.Provider;

public class CDNProvider implements Provider {
	private CloudFlareSource cfSource;

	public CDNProvider(CloudFlareSource pcfSource) {
		this.cfSource = pcfSource;
	}

	public MessageData purge(String customerDomain, String id) {
		return cfSource.purgeCache(customerDomain, id);
	}
	
	public MessageData purgeAll(String customerDomain) {
		return cfSource.purgeCacheAll(customerDomain);
	}
}
