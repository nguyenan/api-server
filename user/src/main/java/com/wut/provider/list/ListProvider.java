package com.wut.provider.list;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;

public interface ListProvider {
	
	public ListData getList(IdData listId);
	
	public MappedData getListItem(IdData listId, IdData itemId);
	
	public BooleanData deleteItem(IdData listId, IdData itemId);
	
	public BooleanData updateItem(IdData listId, IdData itemId, ScalarData data);

	public BooleanData crupdateRow(IdData listId, IdData itemId, ScalarData data);

}
