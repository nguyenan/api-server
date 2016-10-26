package com.wut.search.resource;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.search.provider.RestProvider;

public class WebsiteResource extends CrudResource {
	private static final long serialVersionUID = -299234832472580648L;
	private RestProvider provider = new RestProvider();
	
	public WebsiteResource() {
		super("website", null);
	}

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		StringData url = request.getParameter("url");
		MappedData headers = request.getParameter("headers", true);
		
		Data response = provider.sendRequest(url, new StringData("GET"), headers, null);
		
		return response;
	}
	
	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		StringData url = request.getParameter("url");
		MappedData headers = request.getParameter("headers", true);
		StringData body = request.getParameter("body");
		
		Data response = provider.sendRequest(url, new StringData("POST"), headers, body);
		
		return response;
	}
	
}
