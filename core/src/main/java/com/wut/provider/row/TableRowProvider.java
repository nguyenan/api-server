package com.wut.provider.row;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public interface TableRowProvider extends Provider {
	
	public ListData get(IdData customer, IdData application, IdData tableId);
	
	public ListData filter(IdData customer, IdData application, IdData tableId, MappedData filter);
	
	public MappedData get(IdData customer, IdData application, IdData table, IdData rowId);
	
	public BooleanData delete(IdData customer, IdData application, IdData tableId, IdData rowId);
	
	public BooleanData update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data);

	public BooleanData crupdate(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data);

	public IdData insert(IdData customer, IdData application, IdData tableId, MappedData data);
}
