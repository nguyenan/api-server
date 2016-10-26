package com.wut.provider.row;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.TableProvider;

public class TableRowProviderToTableProviderAdapter implements TableProvider {
	private TableRowProvider rowProvider;
	
	public TableRowProviderToTableProviderAdapter(TableRowProvider rowProvider) {
		this.rowProvider = rowProvider;
	}
	
	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return rowProvider.get(customer, application, tableId);
		//return ListData.convert(source.get(tableId.toRawString()));
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return rowProvider.filter(customer, application, tableId, filter);
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return rowProvider.get(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return rowProvider.delete(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return rowProvider.update(customer, application, tableId, rowId, data);
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return rowProvider.crupdate(customer, application, tableId, rowId, data);
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		return rowProvider.insert(customer, application, tableId, data);
	}
	
}

