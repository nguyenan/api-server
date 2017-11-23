package com.wut.provider.table;

import com.wut.datasources.TableSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class DefaultTableProvider implements TableProvider {
	private TableSource source;
	
	public DefaultTableProvider(TableSource source) {
		this.source = source;
	}
	
	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return source.getAllRows(customer, application, tableId);
	}
	
	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return source.getRowsWithFilter(customer, application, tableId, filter);
	}
	
	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return source.getRow(customer, application, tableId, rowId);
	}
	
	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return source.deleteRow(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData deleteRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return source.deleteRows(customer, application, tableId, filter);
	}
	
	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return source.updateRow(customer, application, tableId, rowId, data);
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return source.crupdateRow(customer, application, tableId, rowId, data);
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		return source.insertRow(customer, application, tableId, data);
	}
}
