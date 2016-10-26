package com.wut.provider.list;

import com.wut.datasources.aws.DynamoDb;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.UniqueRowProvider;

// TODO THIS CLASS!!!!!!!
public class TableToListProvider implements ListProvider {
	DefaultTableProvider settingsTableProvider = new DefaultTableProvider(new DynamoDb());
	FlatTableProvider flatSettingsTableProvider = new FlatTableProvider(settingsTableProvider, "flat2");
	UniqueRowProvider uniqueRowSettingsProvider = new UniqueRowProvider(flatSettingsTableProvider);
	
	public ListData getList(IdData listId) {
		//uniqueRowSettingsProvider.getRows(tableId, filter);
		return null;

	}
	
	public MappedData getListItem(IdData listId, IdData itemId) {
		return null;
		
	}
	
	public BooleanData deleteItem(IdData listId, IdData itemId) {
		return null;

	}
	
	public BooleanData updateItem(IdData listId, IdData itemId, ScalarData data) {
		return null;

	}

	public BooleanData crupdateRow(IdData listId, IdData itemId, ScalarData data) {
		return null;

	}

}
