package com.wut.pipeline;

import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.provider.table.TableProvider;
import com.wut.resources.storage.TableResource;

/**
 * 
 * @author annguyen
 *
 * UserStore: manage admin's authenticate Information: { "id", "username", "name", "password", "token" }
 */
public class UserStore implements CrudSource {
	private static TableProvider table = TableResource.getTableProvider();
	private IdData usersTable = new IdData("users");
	
	private static final String[] fieldNames = new String[] { "id", "username", "name", "password", "token" };
	public FieldSet fields = new FieldSet(fieldNames);
	
	@Override
	public Data create(String customer, String application, Map<String, String> data) {
		return MessageData.NOT_IMPLEMENTED;
	}
	
	/**
	 * Read User Information (without secure data: password and token)
	 * @param customer
	 * @param application
	 * @param id UserId
	 */
	@Override
	public Data read(String customer, String application, String id) {
		Data d = table.getRow(IdData.create(customer), IdData.create(application), usersTable, new IdData(id));
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		} else {
			MappedData mappedData = (MappedData) d;
			mappedData.remove(new StringData("password"));
			mappedData.remove(new StringData("token"));
			return mappedData;
		}
	}
	
	/**
	 * Read User Secure Information
	 * @param customer
	 * @param application
	 * @param id UserId
	 */
	public Data readSecureInformation(String customer, String application, String id) {
		Data d = table.getRow(IdData.create(customer), IdData.create(application), usersTable, new IdData(id));
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		} else {
			MappedData mappedData = (MappedData) d;
			return mappedData;
		}
	}
	
	/**
	 * Update User's Secure Information
	 * @param customer
	 * @param application
	 * @param id UserId
	 * @param data Map: "id", "username", "name", "password", "token"
	 */
	@Override
	public Data update(String customer, String application, String id, Map<String, String> data) {
		MappedData mappedData = MappedData.convert(data);
		Data d = table.updateRow(IdData.create(customer), IdData.create(application), usersTable, new IdData(id), mappedData); // TODO this needs to be update and not override .... 
		return d;
	}

	/**
	 * Delete User
	 * @param customer
	 * @param application
	 * @param id UserId
	 */
	@Override
	public Data delete(String customer, String application, String id) {
		Data d = table.deleteRow(IdData.create(customer), IdData.create(application), usersTable, new IdData(id));
		return d;
	}
}
