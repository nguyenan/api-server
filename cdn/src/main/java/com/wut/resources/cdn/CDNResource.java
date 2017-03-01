package com.wut.resources.cdn;

import com.wut.datasources.cloudflare.CloudFlareSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.cdn.CDNProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.support.settings.SettingsManager;

@ResourceGroupAnnotation(name = "file", group = "cdn", desc = "clean cache")
public class CDNResource extends CrudResource {
	private static final long serialVersionUID = 3301682262046459168L;
	private static CDNProvider provider = new CDNProvider(new CloudFlareSource());

	public CDNResource() {
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
	public Data delete(WutRequest ri) throws MissingParameterException {
		// TODO give option force reload zoneId
		String id = ri.getStringParameter("id");
		String customerDomain = SettingsManager.getCustomerSettings(ri.getCustomer(), "domain");
		MessageData result = provider.purge(customerDomain, id);
		return result;
	}

}
