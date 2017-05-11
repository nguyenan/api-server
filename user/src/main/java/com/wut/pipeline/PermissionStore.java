package com.wut.pipeline;

import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.provider.table.TableProvider;
import com.wut.resources.storage.TableResource;

public class PermissionStore implements CrudSource {
	private static TableProvider table = TableResource.getTableProvider();
	private IdData permissionsTable = new IdData("permission");
	
	private static final String[] fieldNames = new String[] { "id", "role"};
	public FieldSet fields = new FieldSet(fieldNames);
	
	@Override // TODO this needs to be synchronized across servers --- really??? bullshit!!!
	public Data create(String customer, String application, Map<String, String> data) {
		return MessageData.FAILURE;
	}
	
	@Override
	public Data read(String customer, String application, String id) {
		Data d = table.getRow(IdData.create(customer), IdData.create(application), permissionsTable, new IdData(id));
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		} else {
			MappedData mappedData = (MappedData) d;
			return mappedData;
		}
	}

	public Data read(String customer, String application, String id, String affectedCustomer) {
		Data d = table.getRow(IdData.create(customer), IdData.create(application), permissionsTable, new IdData(id));
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		} else {
			MappedData mappedData = (MappedData) d;
			Data permissionData = ((MappedData) mappedData).get(affectedCustomer);
			if (permissionData == null) {
				return MessageData.NO_DATA_FOUND;
			}
			return permissionData;
		}
	}

	@Override
	public Data update(String customer, String application, String id, Map<String, String> data) {
		MappedData mappedData = MappedData.convert(data);
		Data d = table.updateRow(IdData.create(customer), IdData.create(application), permissionsTable, new IdData(id), mappedData); 
		return d;
	}

	@Override
	public Data delete(String customer, String application, String id) {
		Data d = table.deleteRow(IdData.create(customer), IdData.create(application), permissionsTable, new IdData(id));
		return d;
	}

}
