package com.wut.datasources;

import java.util.Map;

import com.wut.model.Data;

public interface RudSource extends DataSource {
	//public boolean create(String name, Map<String,String> data);
	public Data read(String customer, String application, String name);
	public Data update(String customer, String application, String name, Map<String,String> data);
	public Data delete(String customer, String application, String name);
}
