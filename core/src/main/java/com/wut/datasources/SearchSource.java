package com.wut.datasources;

import java.util.List;
import java.util.Map;

public interface SearchSource extends DataSource {
	public List<Map<String,String>> search(String searchTerm);
}
