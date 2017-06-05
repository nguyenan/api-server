package com.wut.provider.table;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public class TableResourceProvider implements Provider {
	private TableProvider provider;
	
	public TableResourceProvider(TableProvider provider) {
		this.provider = provider;
	}
	
	public IdData create(IdData application, IdData customer, IdData username, IdData tableId, MappedData data) {
		IdData newTableId = getFullyQualifiedTableId(application, customer, username, tableId);
		
		IdData newRowId = provider.insertRow(customer, application, newTableId, data);
		return newRowId;
	}
	
	public Data read(IdData application, IdData customer, IdData username, IdData tableId, IdData rowId, MappedData filter) {
		IdData newTableId = getFullyQualifiedTableId(application, customer, username, tableId);

		if (filter != null && rowId == null) {
			ListData data = provider.getRows(customer, application, newTableId, filter);
			return data;
		} else if (rowId == null) {
			ListData data = provider.getRows(customer, application, newTableId);
			return data;
		} else {
			MappedData row = provider.getRow(customer, application, newTableId, rowId);
			if (row == null) {
				return MessageData.NO_DATA_FOUND;
			}
			return row;
		}
	}
	
	public Data update(IdData application, IdData customer, IdData username, IdData tableId, IdData rowId, MappedData data) {
		IdData newTableId = getFullyQualifiedTableId(application, customer, username, tableId);

		BooleanData result = provider.updateRow(customer, application, newTableId, rowId, data);
		return MessageData.successOrFailure(result);
	}
	
	public Data crupdate(IdData application, IdData customer, IdData username, IdData tableId, IdData rowId, MappedData data) {
		IdData newTableId = getFullyQualifiedTableId(application, customer, username, tableId);

		BooleanData result = provider.crupdateRow(customer, application, newTableId, rowId, data);
		return MessageData.successOrFailure(result);
	}

	public Data delete(IdData application, IdData customer, IdData username, IdData tableId, IdData rowId) {
		IdData newTableId = getFullyQualifiedTableId(application, customer, username, tableId);

		BooleanData result = provider.deleteRow(customer, application, newTableId, rowId);
		return MessageData.successOrFailure(result);
	}
	
	
	private IdData getFullyQualifiedTableId(IdData application,
			IdData customer, IdData username, IdData tableId) {
		// TODO Auto-generated method stub FIXXXX!!!!
		String scope = makeScope(application.toRawString(), customer.toRawString(), username.toRawString(), null);
		String qualifiedTable = scope + ":" + tableId.toRawString();
		
		return new IdData(qualifiedTable);
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
