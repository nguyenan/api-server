package com.wut.datasources;

import java.util.Map;

import com.wut.model.Data;

public interface CrudSource extends RudSource {
	// returns a unique id for the newly created data
	public Data create(String customer, String application, Map<String,String> data);
	//public Data read(String id);
	//public Data update(String id, Map<String,String> data);
	//public Data delete(String id);
}
