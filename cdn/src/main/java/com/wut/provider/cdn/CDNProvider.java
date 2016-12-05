package com.wut.provider.cdn;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.map.MessageData;
import com.wut.provider.Provider;

public class CDNProvider implements Provider {
	private CFSource cfSource;

	public CDNProvider(CFSource pcfSource) {
		this.cfSource = pcfSource;
	}

	public MessageData purge(String customerDomain, String id) {
		return cfSource.purgeCache(customerDomain, id);
	}
}
