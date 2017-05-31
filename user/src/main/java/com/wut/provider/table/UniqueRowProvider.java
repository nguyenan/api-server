package com.wut.provider.table;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.ErrorHandler;
import com.wut.support.UniqueIdGenerator;

// This makes all row "ids" also contain the table name
public class UniqueRowProvider implements TableProvider {
	private TableProvider provider;

	public UniqueRowProvider(TableProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		ListData result = provider.getRows(customer, application, tableId);
		result = undecorateIds(tableId, result);
		return result;
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		ListData result = provider.getRows(customer, application, tableId, filter);
		result = undecorateIds(tableId, result);
		return result;
	}
	
	private ListData undecorateIds(IdData tableId, ListData result) {
		for (int i=0; i<result.size(); i++) {
			MappedData row = (MappedData) result.get(i);
			stripTableFromIdInRow(tableId, row);
		}
		return result;
	}
	
	private void stripTableFromIdInRow(IdData tableId, MappedData row) {
		if (row != null) {
			ScalarData rowId = (ScalarData) row.get("id");
			if (rowId != null && rowId.toRawString().contains(tableId.toRawString())) {
				String strippedDownId = rowId.toRawString().substring(tableId.toRawString().length()+1);
				ScalarData newId = new IdData(strippedDownId);
				row.put("id", newId);
			} else {
				ErrorHandler.systemError("Invalid id encountered in UniqueRowProviver");
			}
		}
	}
	
	private IdData getNewRowId(IdData tableId, IdData rowId) {
		IdData newRowId = new IdData(tableId.toRawString() + ":" + rowId.toRawString());
		return newRowId;
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		MappedData result = this.provider.getRow(customer, application, tableId, getNewRowId(tableId, rowId));
		stripTableFromIdInRow(tableId, result);
		return result;
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return provider.deleteRow(customer, application, tableId, getNewRowId(tableId, rowId));
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return provider.updateRow(customer, application, tableId, getNewRowId(tableId, rowId), data);
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return provider.crupdateRow(customer, application, tableId, getNewRowId(tableId, rowId), data);
	}

	// TODO is crupdate really the best solution here. it should fail if row already exists
	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		IdData generatedRowId = new IdData(UniqueIdGenerator.getId());
		IdData newRowId = getNewRowId(tableId, generatedRowId);
		provider.crupdateRow(customer, application, tableId, newRowId, data);
		return generatedRowId;
	}

//	@Override
//	public IdData insertRow(IdData tableId, MappedData data) {
//		IdData newId = getNewRowId(tableId, (IdData)data.get("id"));
//		data.put("id", newId);
//		return provider.insertRow(tableId, data);
//	}
}



