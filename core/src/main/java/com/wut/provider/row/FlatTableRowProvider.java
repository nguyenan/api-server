package com.wut.provider.row;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
//import com.wut.support.UniqueIdGenerator;

public class FlatTableRowProvider implements TableRowProvider {
	private TableRowProvider provider;
	private IdData systemTable;

	public FlatTableRowProvider(TableRowProvider provider, String flatTableName) {
		this.provider = provider;
		this.systemTable = new IdData(flatTableName);
	}
	
	@Override
	public ListData get(IdData customer, IdData application, IdData tableId) {
		IdData newTableId = getNewId(tableId);
		return this.filter(customer, application, newTableId, new MappedData());
	}

	@Override
	public ListData filter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		IdData newTableId = getNewId(tableId);
		filter.put("table", newTableId);
		return provider.filter(customer, application, systemTable, filter);
	}

	@Override
	public MappedData get(IdData customer, IdData application, IdData tableId, IdData rowId) {
		IdData newTableId = getNewId(tableId); // ???? uggg this sucks
		return provider.get(customer, application, systemTable, rowId);
	}

	@Override
	public BooleanData delete(IdData customer, IdData application, IdData tableId, IdData rowId) {
		IdData newTableId = getNewId(tableId);
		return provider.delete(customer, application, systemTable, rowId); // TODO ummm nooo table!???? wtf
	}

	@Override
	public BooleanData update(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		IdData newTableId = getNewId(tableId);
		data.put("table", newTableId);
		return provider.update(customer, application, systemTable, rowId, data);
	}

	@Override
	public BooleanData crupdate(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		IdData newTableId = getNewId(tableId);
		data.put("table", newTableId);
		return provider.crupdate(customer, application, systemTable, rowId, data);
	}

	@Override
	public IdData insert(IdData customer, IdData application, IdData tableId, MappedData data) {
		IdData newTableId = getNewId(tableId);
		data.put("table", newTableId);
		return provider.insert(customer, application, systemTable, data);
	}

	// TODO why is this not needed!!!! we should be converting the id to
	// include the table on every request through the flat table row provider!!!

//	private IdData getRowId(IdData userTable, IdData id) {
//		return new IdData(userTable.toRawString() + ":" + id.toRawString());
//	}
	
	
	// TODO remove this function and all references
	private IdData getNewId(IdData tableId) {
		// TODO Auto-generated method stub
		return tableId;
	}
	
	public static String makeScope(String application, String customer, String username, String variable) {
		StringBuilder scope = new StringBuilder();
		scope.append(application);
		scope.append(":");
		scope.append(customer);
		scope.append(":");
		scope.append(escapeUsername(username));
		if (variable != null) { // TODO NOT NEEDED SHOULD NOT PASS NULL IN HERE
			scope.append(":");
			scope.append(variable);
		}
		String scopeStr = scope.toString();
		return scopeStr;
	}
	
	public static String escapeUsername(String username) {
		// replace with userid (so users can change usernames)
		// return URLEncoder.encode(this.user);
		return username.replaceAll("@", "_").replaceAll("\\.", "_");
	}
	
}
