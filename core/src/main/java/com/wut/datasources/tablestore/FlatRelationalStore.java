package com.wut.datasources.tablestore;

//import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wut.model.Data;
//import com.wut.model.list.ListData;

// TODO make these chainable
// TODO rename to FlattenedRelationalStore
public class FlatRelationalStore implements RelationalStore {
	private RelationalStore store;
	private String trueTableName;

	public FlatRelationalStore(RelationalStore store) {
		this(store, "Standard12");
	}

	public FlatRelationalStore(RelationalStore store, String trueTableName) {
		this.store = store;
		this.trueTableName = trueTableName;
	}

	@Override
	public boolean createTable(String table, Map<String, Class<Data>> schema) {
		return true;
	}

	@Override
	public boolean deleteRow(String table, String id) {
		return store.deleteRow(trueTableName, id);
	}

	@Override
	public boolean deleteTable(String table) {
		return false;
	}

	@Override
	public List<Map<String, String>> getAllRows(String table) {
		// TODO put filter here -- filter by given table
		return store.getAllRows(trueTableName);
	}

	@Override
	public List<String> getAllTables() {
		return null;
	}

	@Override
	public Map<String, String> getRow(String table, String id) {
		return store.getRow(trueTableName, id);
	}

	@Override
	public Map<String, Class<Data>> getTableSchema(String table) {
		return null;
	}

	@Override
	public String insertRow(String table, Map<String, String> row) {
		row.put("_table_", table);
		return store.insertRow(trueTableName, row);
	}

	@Override
	public boolean updateRow(String table, String id, Map<String, String> row) {
		row.put("_table_", table);
		return store.updateRow(trueTableName, id, row);
	}

	@Override
	public boolean updateTable(String table, Map<String, Class<Data>> schema) {
		return false;
	}
}
