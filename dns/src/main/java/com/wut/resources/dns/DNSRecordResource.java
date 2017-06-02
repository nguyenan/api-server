package com.wut.resources.dns;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.dns.DNSProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ResourceGroupAnnotation;

@ResourceGroupAnnotation(name = "record", group = "dns", desc = "add record")
public class DNSRecordResource extends CrudResource {
	private static final long serialVersionUID = 1210271770140843757L;
	private static DNSProvider provider = new DNSProvider(new CFSource());

	public DNSRecordResource() {
		super("record", null);
	}

	@Override
	public String getName() {
		return "record";
	}

	@Override
	public Data create(WutRequest ri) throws MissingParameterException {
		String customerId = ri.getCustomer();
		String name = ri.getStringParameter("name");
		String content = ri.getStringParameter("content");
		String domain = ri.getOptionalParameterAsString("domain");
		String customerDomain = (domain != null && !domain.isEmpty()) ? domain : customerId;
		return provider.createRecord(customerDomain, name, content);
	}

	@Override
	public Data read(WutRequest ri) throws MissingParameterException {
		String customerId = ri.getCustomer();
		String name = ri.getStringParameter("name");
		String domain = ri.getOptionalParameterAsString("domain");
		String customerDomain = (domain != null && !domain.isEmpty()) ? domain : customerId;
		return provider.getRecordDetails(customerDomain, name);
	}

	@Override
	public Data update(WutRequest ri) throws MissingParameterException {
		String customerId = ri.getCustomer();
		String name = ri.getStringParameter("name");
		String content = ri.getStringParameter("content");
		String domain = ri.getOptionalParameterAsString("domain");
		String customerDomain = (domain != null && !domain.isEmpty()) ? domain : customerId;
		return provider.updateRecord(customerDomain, name, content);
	}

	@Override
	public Data delete(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

}
