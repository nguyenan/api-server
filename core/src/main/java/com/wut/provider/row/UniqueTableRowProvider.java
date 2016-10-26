package com.wut.provider.row;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.ErrorHandler;
import com.wut.support.UniqueIdGenerator;

// This makes all row "ids" also contain the table name
public class UniqueTableRowProvider implements TableRowProvider {
	private TableRowProvider provider;

	public UniqueTableRowProvider(TableRowProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public ListData get(IdData customer, IdData application, IdData tableId) {
		ListData result = provider.get(customer, application, tableId);
		result = undecorateIds(customer, application, tableId, result);
		return result;
	}

	@Override
	public ListData filter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		ListData result = provider.filter(customer, application, tableId, filter);
		result = undecorateIds(customer, application, tableId, result);
		return result;
	}
	
	private ListData undecorateIds(IdData customer, IdData application, IdData tableId, ListData result) {
		for (int i=0; i<result.size(); i++) {
			MappedData row = (MappedData) result.get(i);
			stripTableFromIdInRow(customer, application, tableId, row);
		}
		return result;
	}
	
	private void stripTableFromIdInRow(IdData customer, IdData application, IdData tableId, MappedData row) {
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
	
	// TODO name this decordateId
	private IdData getNewRowId(IdData customer, IdData application, IdData tableId, IdData rowId) {
		IdData newRowId = new IdData(tableId.toRawString() + ":" + rowId.toRawString());
		return newRowId;
	}

	@Override
	public MappedData get(IdData customer, IdData application, IdData tableId, IdData rowId) {
		MappedData result = this.provider.get(customer, application, tableId, getNewRowId(customer, application, tableId, rowId));
		stripTableFromIdInRow(customer, application, tableId, result);
		return result;
	}

	@Override
	public BooleanData delete(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return provider.delete(customer, application, tableId, getNewRowId(customer, application, tableId, rowId));
	}

	@Override
	public BooleanData update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return provider.update(customer, application, tableId, getNewRowId(customer, application, tableId, rowId), data);
	}

	@Override
	public BooleanData crupdate(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return provider.crupdate(customer, application,tableId, getNewRowId(customer, application, tableId, rowId), data);
	}

	// TODO is crupdate really the best solution here. it should fail if row already exists
	@Override
	public IdData insert(IdData customer, IdData application, IdData tableId, MappedData data) {
		IdData generatedRowId = new IdData(UniqueIdGenerator.getId());
		IdData newRowId = getNewRowId(customer, application, tableId, generatedRowId);
		provider.crupdate(customer, application, tableId, newRowId, data);
		return generatedRowId;
	}

//	@Override
//	public IdData insertRow(IdData tableId, MappedData data) {
//		IdData newId = getNewRowId(tableId, (IdData)data.get("id"));
//		data.put("id", newId);
//		return provider.insertRow(tableId, data);
//	}
}



