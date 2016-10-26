package com.wut.provider.table;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class SoftDeleteProvider implements TableProvider {
	private TableProvider provider;

	public SoftDeleteProvider(TableProvider provider) {
		this.provider = provider;
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return this.getRows(customer, application, tableId, new MappedData());
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		ListData newResults = new ListData();
		ListData oldResults = provider.getRows(customer, application, tableId, filter);
		for (int i = 0; i < oldResults.size(); i++) {
			MappedData item = (MappedData) oldResults.get(0);
			Data isDeleted = item.get("_deleted");
			if (!String.valueOf(isDeleted).equals("true")) {
				newResults.add(item);
			}
		}
		return newResults;
	}

//	private boolean isRowDeleted(MappedData row) {
//		// TODO implement is row deleted
//		// Data isDeleted = item.get("_deleted");
//		// if (!String.valueOf(isDeleted).equals("true")) {
//		// return true;
//		// }
//		return false;
//	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		//MappedData data = provider.getRow(tableId, rowId);
		// if ()

		return null;
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		// get
		// return provider.updateRow(tableId, rowId); // TODO no tableId here

		// TODO implement
		return null;
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		data.put("table", tableId);
		// return provider.updateRow(systemTable, rowId, data);
		// TODO implement
		return null;
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		data.put("table", tableId);
		// return provider.crupdateRow(systemTable, rowId, data);

		// TODO implement
		return null;
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		data.put("table", tableId);
		return provider.insertRow(customer, application, tableId, data);
	}

}
