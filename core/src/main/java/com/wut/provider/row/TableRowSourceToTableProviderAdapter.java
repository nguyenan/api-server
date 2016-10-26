package com.wut.provider.row;

import com.wut.datasources.TableRowSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.TableProvider;

public class TableRowSourceToTableProviderAdapter implements TableProvider {
	private TableRowSource source;
	
	public TableRowSourceToTableProviderAdapter(TableRowSource source) {
		this.source = source;
	}

//	@Override
//	public ListData get(IdData tableId) {
//		return source.getAllRows(tableId);
//	}
//
//	@Override
//	public ListData filter(IdData tableId, MappedData filter) {
//		return source.getRowsWithFilter(tableId, filter);
//	}
//
//	@Override
//	public MappedData get(IdData tableId, IdData rowId) {
//		return source.getRow(tableId, rowId);
//	}
//
//	@Override
//	public BooleanData delete(IdData tableId, IdData rowId) {
//		return source.deleteRow(tableId, rowId);
//	}
//
//	@Override
//	public BooleanData update(IdData tableId, IdData rowId, MappedData data) {
//		return source.updateRow(tableId, rowId, data);
//	}
//
//	@Override
//	public BooleanData crupdate(IdData tableId, IdData rowId, MappedData data) {
//		return source.crupdateRow(tableId, rowId, data);
//	}
//
//	@Override
//	public IdData insert(IdData tableId, MappedData data) {
//		return source.insertRow(tableId, data);
//	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId) {
		return ListData.convert(source.get(customer.toRawString(), application.toRawString(), tableId.toRawString()));
	}

	@Override
	public ListData getRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		return ListData.convert(source.filter(customer.toRawString(), application.toRawString(), tableId.toRawString(), filter.getMapAsPojo()));
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return MappedData.convert(source.get(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString()));
	}

	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		return BooleanData.create(source.remove(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString()));
	}

	@Override
	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return BooleanData.create(source.update(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString(), data.getMapAsPojo()));
	}

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		return BooleanData.create(source.crupdate(customer.toRawString(), application.toRawString(), tableId.toRawString(), rowId.toRawString(), data.getMapAsPojo()));
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		return IdData.create(source.insert(customer.toRawString(), application.toRawString(), tableId.toRawString(), data.getMapAsPojo()));
	}
	
}
