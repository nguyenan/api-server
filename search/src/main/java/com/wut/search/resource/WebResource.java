package com.wut.search.resource;

import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.SearchProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.search.datasource.GoogleSearchDataSource;

public class WebResource extends CrudResource {
	private static final long serialVersionUID = -599262832472580648L;
	private GoogleSearchDataSource source = new GoogleSearchDataSource();
	private SearchProvider provider = new SearchProvider(source);
	
	public WebResource() {
		super("web", null);
	}

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		StringData searchTerm = request.getParameter("term");
		Data d = provider.search(searchTerm); 
		return d;
	}
}
