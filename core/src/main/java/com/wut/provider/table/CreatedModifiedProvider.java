package com.wut.provider.table;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.ErrorHandler;
import com.wut.support.UniqueIdGenerator;

// This makes all row "ids" also contain the table name
public class CreatedModifiedProvider implements TableProvider {
	private TableProvider provider;

	public CreatedModifiedProvider(TableProvider provider) {
		this.provider = provider;
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		data.put("_updated", String.valueOf(System.currentTimeMillis()));
		return provider.updateRow(customer, application, tableId, rowId, data);
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		// TODO created????
		data.put("_updated", String.valueOf(System.currentTimeMillis()));
		return provider.crupdateRow(customer, application, tableId, rowId, data);
	}
 
	// TODO is crupdate really the best solution here. it should fail if row already exists
	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		data.put("_created", String.valueOf(System.currentTimeMillis()));
		data.put("_updated", String.valueOf(System.currentTimeMillis()));
		return provider.insertRow(customer, application, tableId, data);
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return provider.getRows(customer, application, tableId);
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return provider.getRows(customer, application, tableId, filter);
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData table, IdData rowId) {
		return provider.getRow(customer, application, table, rowId);
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return provider.deleteRow(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData deleteRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		// TODO Auto-generated method stub
		return null;
	}
}