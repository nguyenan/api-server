package com.wut.resources.cdn;

import com.wut.datasources.cloudflare.CloudFlareSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.cdn.CDNProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.settings.SettingsManager;

public class PurgeOperation extends ParameteredOperation {
	private static CDNProvider provider = new CDNProvider(new CloudFlareSource());

	public PurgeOperation() {
		addParameter(OperationParameter.ID);
	}

	@Override
	public String getName() {
		return "purge";
	}

	@Override
	public Data perform(WutRequest ri) throws Exception {
		String id = ri.getStringParameter("id");
		String customerDomain = SettingsManager.getCustomerSettings(ri.getCustomer(), "domain");
		MessageData result = provider.purge(customerDomain, id);
		return result;
	}
}
