package com.wut.provider.row;

import com.wut.datasources.TableSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class DefaultTableRowProviderAdapter implements TableRowProvider {
	private TableSource source;
	
	public DefaultTableRowProviderAdapter(TableSource source) {
		this.source = source;
	}

	@Override
	public ListData get(IdData customer, IdData application, IdData tableId) {
		return source.getAllRows(customer, application, tableId);
	}

	@Override
	public ListData filter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return source.getRowsWithFilter(customer, application, tableId, filter);
	}

	@Override
	public MappedData get(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return source.getRow(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData delete(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return source.deleteRow(customer, application, tableId, rowId);
	}

	@Override
	public BooleanData update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return source.updateRow(customer, application, tableId, rowId, data);
	}

	@Override
	public BooleanData crupdate(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return source.crupdateRow(customer, application, tableId, rowId, data);
	}

	@Override
	public IdData insert(IdData customer, IdData application, IdData tableId, MappedData data) {
		return source.insertRow(customer, application, tableId, data);
	}
	
}
