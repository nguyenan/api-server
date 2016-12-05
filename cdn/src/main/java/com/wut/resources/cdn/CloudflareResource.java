package com.wut.resources.cdn;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.datasources.cloudflare.CFConstants;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.cdn.CDNProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;
import com.wut.model.scalar.*;

@ResourceGroupAnnotation(name = "file", group = "cdn", desc = "clean cache")
public class CloudflareResource extends CrudResource {
	private static final long serialVersionUID = 3301682262046459168L;
	private static CDNProvider provider = new CDNProvider(new CFSource());

	public CloudflareResource() {
		super("file", null);
	}

	@Override
	public String getName() {
		return "file";
	}

	@Override
	public Data create(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data read(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data update(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;

	}

	@Override
	public Data delete(WutRequest ri) {
		String id = ri.getId();
		String customerDomain = SettingsManager.getCustomerSettings(ri.getCustomer(), "domain");
		BooleanData purgeSucceeded = provider.purge(customerDomain, id);
		return purgeSucceeded;
	}

}
