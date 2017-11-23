package com.wut.provider.table;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public interface TableProvider {
	
	public ListData getRows(IdData customer, IdData application, IdData tableId);
	
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter);
	
	public MappedData getRow(IdData customer, IdData application, IdData table, IdData rowId);
	
	public BooleanData deleteRows(IdData customer, IdData application, IdData tableId, MappedData filter);
	
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId);
	
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data);

	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data);

	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data);
}
