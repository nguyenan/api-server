package com.wut.pipeline;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.provider.row.TableRowResourceProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;

public class CacheResource extends CrudResource {
	
	private static final long serialVersionUID = 1L;
	private TableRowResourceProvider provider;

	public CacheResource(TableRowResourceProvider provider) {
		super("cache", null);
		this.provider = provider;
	}
	
	// TODO change resource name to table and attribute table to type (or schema)
	
	public CacheResource() {
		super("cache", null);
	}
	
	@Override
	public String getName() {
		return "table";
	}
	
	// TODO not using scope properly (USERS SHARE DATA!!!!)

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request, true);
		MappedData filter = request.getParameter("filter", true);
		Data result = provider.read(customer, application, tableId, rowId, filter);
		return result;
	}
	
	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request);
		Data result = provider.delete(customer, application, tableId, rowId);
		return result;
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request);
		MappedData data = getDataParam(request);
		Data result = provider.update(customer, application, tableId, rowId, data);
		return result;
	}
	
	@Override
	public Data create(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		IdData tableId = getTableScope(request);
		MappedData data = getDataParam(request);
		IdData newRowId = provider.create(customer, application, tableId, data);
		return newRowId;
	}
	
	// PARAMETER GETTING OPERATIONS
	
	public static IdData getRowIdParam(WutRequest request) throws MissingParameterException {
		return getRowIdParam(request, false);
	}
	
	public static IdData getRowIdParam(WutRequest request,  boolean isOptional) throws MissingParameterException {
		String id;
		if (isOptional) {
			id = request.getOptionalParameterAsString("row");
		} else {
			id = request.getStringParameter("row");
		}
		return new IdData(id);
	}
	
	public static MappedData getDataParam(WutRequest request) throws MissingParameterException {
		MappedData data = request.getParameter("data");
		if (data == null) {
			throw new MissingParameterException("data");
		}
		return data;
	}
	
	public static IdData getTableScope(WutRequest request) throws MissingParameterException {
		StringData table = request.getParameter("id");
		String scope = request.scopify(table.toRawString());
		return new IdData(scope);
	}
	
	public static IdData getCustomer(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		return new IdData(customer);
	}
	
	public static IdData getApplication(WutRequest request) throws MissingParameterException {
		String application = request.getApplication();
		return new IdData(application);
	}
}

