package com.wut.datasources;

import java.util.List;
import java.util.Map;

public interface TableRowSource extends DataSource {
	
	// TODO put into a TableSource class
	public List<Map<String,String>> get(String customer, String application, String tableId);
	
	// TODO put into a TableSource class
	public List<Map<String,String>> filter(String customer, String application, String tableId, Map<String,String> filter);
	
	public Map<String,String> get(String customer, String application, String tableId, String rowId);
	
	public boolean remove(String customer, String application, String tableId, String rowId);
	
	public boolean update(String customer, String application, String tableId, String rowId, Map<String,String> data);

	public boolean crupdate(String customer, String application, String tableId, String rowId, Map<String,String> data);

	public String insert(String customer, String application, String tableId, Map<String,String> data);

}
