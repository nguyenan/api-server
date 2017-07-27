package com.wut.resources.dns;

import java.util.Arrays;
import java.util.List;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
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
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[] { "dns.domain" });
	}

	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[] { "dns.domain" });
	}

	@Override
	public Data create(WutRequest ri) {
		String customerId = ri.getCustomer();
		String customerDomain = SettingsManager.getClientSettings(customerId, "dns.domain");
		String domain = ri.getOptionalParameterAsString("domain");
		customerDomain = (domain != null && !domain.isEmpty()) ? domain : customerDomain;
		if (customerDomain.isEmpty())
			return ErrorData.DOMAIN_EMPTY;
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
		String customerId = ri.getCustomer();
		String customerDomain = SettingsManager.getClientSettings(customerId, "dns.domain");
		String domain = ri.getOptionalParameterAsString("domain");
		customerDomain = (domain != null && !domain.isEmpty()) ? domain : customerDomain;
		return provider.deleteZone(customerDomain);
	}

}
