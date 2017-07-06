package com.wut.provider.table;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
//import com.wut.support.UniqueIdGenerator;

public class FlatTableProvider implements TableProvider {
	private TableProvider provider;
	private IdData systemTable;
	private boolean filterOnGetRow; // filtering not needed if using unique row provider

	public FlatTableProvider(TableProvider provider, String flatTableName) {
		this(provider, flatTableName, false);
	}
	
	public FlatTableProvider(TableProvider provider, String flatTableName, boolean filterOnGetRow) {
		this.provider = provider;
		this.systemTable = new IdData(flatTableName);
		this.filterOnGetRow = filterOnGetRow;
	}
	
	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return this.getRows(customer, application, tableId, new MappedData());
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		filter.put("table", tableId);
		return provider.getRows(customer, application, systemTable, filter);
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		if (filterOnGetRow) {
			MappedData filter = new MappedData();
			filter.put("table", tableId);
			filter.put("id", rowId);
			ListData rows = this.getRows(customer, application, tableId, filter);
			MappedData row1 = (MappedData) (rows.size() > 0 ? rows.get(0) : null);
			return row1;
		} else {
			return provider.getRow(customer, application, systemTable, rowId);
		}
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return provider.deleteRow(customer, application, systemTable, rowId);
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		data.put("table", tableId);
		return provider.updateRow(customer, application, systemTable, rowId, data);
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		data.put("table", tableId);
		return provider.crupdateRow(customer, application, systemTable, rowId, data);
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		data.put("table", tableId);
		return provider.insertRow(customer, application, systemTable, data);
	}

//	private IdData getRowId(IdData userTable, IdData id) {
//		return new IdData(userTable.toRawString() + ":" + id.toRawString());
//	}
	
}
