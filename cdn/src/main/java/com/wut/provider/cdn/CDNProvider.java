package com.wut.provider.cdn;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.scalar.BooleanData;
import com.wut.provider.Provider;

public class CDNProvider implements Provider {
	private CFSource cfSource;

	public CDNProvider(CFSource pcfSource) {
		this.cfSource = pcfSource;
	}

	public BooleanData purge(String customerDomain, String id) {
		boolean ret = cfSource.purgeCache(customerDomain, id);
		return BooleanData.create(ret);
	}
}
