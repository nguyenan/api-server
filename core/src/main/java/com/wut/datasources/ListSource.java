package com.wut.datasources;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;

public interface ListSource extends DataSource {
	public ListData getList(IdData listId);
	
	public MappedData getListItem(IdData listId, IdData rowId);
	
	public void deleteRow(IdData tableId, IdData rowId);
	
	public void deleteList(IdData tableId);
	
	public void updateListItem(IdData tableId, IdData rowId, MappedData data);

	public IdData insertListItem(IdData tableId, MappedData data);
}
