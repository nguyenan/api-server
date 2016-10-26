package com.wut.datasources.tablestore;

import java.util.List;
import java.util.Map;

import com.wut.model.Data;

// TODO rename tablestore
public interface RelationalStore {

	// Row operations (change row to record?)
	public String insertRow(String table, Map<String,String> row); // TODO return ID!!!
	public boolean deleteRow(String table, String id);
	public boolean updateRow(String table, String id, Map<String,String> row);
	public Map<String,String> getRow(String table, String id);
	public List<Map<String,String>> getAllRows(String table);
	
	// Table operations
	public boolean createTable(String table, Map<String,Class<Data>> schema);
	public boolean deleteTable(String table);
	public boolean updateTable(String table, Map<String,Class<Data>> schema);
	public Map<String,Class<Data>> getTableSchema(String table);
	public List<String> getAllTables();
	
}
