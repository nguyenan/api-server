package com.wut.provider.row;

import com.wut.datasources.TableRowSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;

public class DefaultTableRowProvider implements TableRowProvider {
	private TableRowSource source;
	
	public DefaultTableRowProvider(TableRowSource source) {
		this.source = source;
	}

	@Override
	public ListData get(IdData customer, IdData application, IdData tableId) {
		return ListData.convert(source.get(customer.toRawString(), application.toRawString(), tableId.toRawString()));
	}

	@Override
	public ListData filter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return ListData.convert(source.filter(customer.toRawString(), application.toRawString(), tableId.toRawString(), filter.getMapAsPojo()));
	}

	@Override
	public MappedData get(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return MappedData.convert(source.get(customer.toRawString(), application.toRawString(),tableId.toRawString(), rowId.toRawString()));
	}

	@Override
	public BooleanData delete(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return BooleanData.create(source.remove(customer.toRawString(), application.toRawString(),tableId.toRawString(), rowId.toRawString()));
	}

	@Override
	public BooleanData update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return BooleanData.create(source.update(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString(), data.getMapAsPojo()));
	}

	@Override
	public BooleanData crupdate(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return BooleanData.create(source.crupdate(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString(), data.getMapAsPojo()));
	}

	@Override
	public IdData insert(IdData customer, IdData application, IdData tableId, MappedData data) {
		return IdData.create(source.insert(customer.toRawString(), application.toRawString(), tableId.toRawString(), data.getMapAsPojo()));
	}
	
}
