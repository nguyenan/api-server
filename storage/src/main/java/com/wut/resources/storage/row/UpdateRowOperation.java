package com.wut.resources.storage.row;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.Provider;
import com.wut.provider.row.RowProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.resources.common.AbstractOperation;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.operations.UpdateOperation;

public class UpdateRowOperation extends UpdateOperation {
	private TableResourceProvider provider = RowProvider.getRowProvider();
	private TableResourceProvider cacheProvider = RowProvider.getRowCache();

	@Override
	public Data perform(WutRequest request) throws Exception {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		String idString = request.getStringParameter("id");
		IdData rowId = new IdData(idString);
		MappedData data = request.getParameter("data");
		if (data == null) { // TODO: DONT THIS THIS CHECK IS NEEDED
			throw new MissingParameterException("data");
		}
		
		Data result = provider.update(application, customer, user, tableId, rowId, data);
		return result;
	}
	
	@Override
	public Data cacheGet(WutRequest request) throws MissingParameterException {
		return null;
	}
	
	@Override
	public boolean cachePut(WutRequest request, Data d) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		String idString = request.getStringParameter("id");
		IdData rowId = new IdData(idString);
		MappedData data = request.getParameter("data");
		if (data == null) { // TODO: DONT THIS THIS CHECK IS NEEDED
			throw new MissingParameterException("data");
		}
		
		Data result = cacheProvider.update(application, customer, user, tableId, rowId, data);
		return result != null;
	}
	
}
