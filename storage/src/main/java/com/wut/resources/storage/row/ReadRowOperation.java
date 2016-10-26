package com.wut.resources.storage.row;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.row.RowProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.operations.ReadOperation;

public class ReadRowOperation extends ReadOperation {
	private TableResourceProvider provider = RowProvider.getRowProvider();
	private TableResourceProvider cacheProvider = RowProvider.getRowCache();

	@Override
	public Data perform(WutRequest request) throws Exception {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		String idString = request.getOptionalParameterAsString("id");
		IdData rowId = idString != null ? new IdData(idString) : null;
		MappedData filter = request.getParameter("filter", true);
		
		Data result = provider.read(application, customer, user, tableId, rowId, filter);
		return result;
	}
	
	@Override
	public Data cacheGet(WutRequest request) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		String idString = request.getOptionalParameterAsString("id");
		IdData rowId = idString != null ? new IdData(idString) : null;
		MappedData filter = request.getParameter("filter", true);
		
		Data result = cacheProvider.read(application, customer, user, tableId, rowId, filter);
		return result;
	}
	
	@Override
	public boolean cachePut(WutRequest request, Data data) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		String idString = request.getStringParameter("id");
		IdData rowId = new IdData(idString);
		
		MappedData mapData = (MappedData) data;
		//MappedData resultData = (MappedData) mapData.get("result");
		Data result = cacheProvider.crupdate(application, customer, user, tableId, rowId, mapData);
		return result != null;
	}

}
