package com.wut.resources.dns;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.dns.DNSProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.support.settings.SettingsManager;

@ResourceGroupAnnotation(name = "zone", group = "dns", desc = "add zone")
public class ZoneResource extends CrudResource {
	private static final long serialVersionUID = 1210271770140843757L;
	private static DNSProvider provider = new DNSProvider(new CFSource());

	public ZoneResource() {
		super("zone", null);
	}

	@Override
	public String getName() {
		return "zone";
	}

	@Override
	public Data create(WutRequest ri) {
		String customerDomain = SettingsManager.getCustomerSettings(ri.getCustomer(), "domain");
		return provider.createZone(customerDomain);
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
		return MessageData.NOT_IMPLEMENTED;
	}

}
