package com.wut.provider.table;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class TableResourceProvider {
	private TableProvider provider;
	
	public TableResourceProvider(TableProvider provider) {
		this.provider = provider;
	}
	
	public IdData create(IdData customer, IdData application, IdData tableId, MappedData data) {
		IdData newRowId = provider.insertRow(customer, application, tableId, data);
		return newRowId;
	}
	
	public Data read(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData filter) {
		if (filter != null && rowId == null) {
			ListData data = provider.getRows(customer, application, tableId, filter);
			return data;
		} else if (rowId == null) {
			ListData data = provider.getRows(customer, application, tableId);
			return data;
		} else {
			MappedData row = provider.getRow(customer, application, tableId, rowId);
			if (row == null) {
				return MessageData.NO_DATA_FOUND;
			}
			return row;
		}
	}
	
	public Data delete(IdData customer, IdData application, IdData tableId, IdData rowId) {
		BooleanData result = provider.deleteRow(customer, application, tableId, rowId);
		return MessageData.successOrFailure(result);
	}
	
	public Data update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		BooleanData result = provider.updateRow(customer, application, tableId, rowId, data);
		return MessageData.successOrFailure(result);
	}

	
	
}
