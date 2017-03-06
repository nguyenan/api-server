package com.wut.resources.cdn;

import com.wut.datasources.cloudflare.CloudFlareSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.cdn.CDNProvider;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.settings.SettingsManager;

public class PurgeAllOperation extends ParameteredOperation {
	private static CDNProvider provider = new CDNProvider(new CloudFlareSource());

	public PurgeAllOperation() {
	}

	@Override
	public String getName() {
		return "purge";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customerDomain = SettingsManager.getCustomerSettings(ri.getCustomer(), "domain");
		MessageData result = provider.purgeAll(customerDomain);
		return result;
	}
}