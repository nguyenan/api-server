package com.wut.provider;

import java.util.List;
import java.util.Map;

import com.wut.datasources.SearchSource;
import com.wut.model.list.ListData;
import com.wut.model.scalar.StringData;

public class SearchProvider implements Provider {
	private SearchSource source;

	public SearchProvider(SearchSource source) {
		this.source = source;
	}
	
	public ListData search(StringData term) {
		List<Map<String,String>> results = source.search(term.toRawString());
		return ListData.convert(results);
	}
}
