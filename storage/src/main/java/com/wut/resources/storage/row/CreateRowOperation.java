package com.wut.resources.storage.row;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.row.RowProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.operations.CreateOperation;

public class CreateRowOperation extends CreateOperation {
	private TableResourceProvider provider = RowProvider.getRowProvider();
	private TableResourceProvider cacheProvider = RowProvider.getRowCache();

	@Override
	public Data perform(WutRequest request) throws Exception {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		MappedData data = request.getParameter("data");
		if (data == null) { // TODO: DONT THIS THIS CHECK IS NEEDED
			throw new MissingParameterException("data");
		}
		
		IdData newRowId = provider.create(application, customer, user, tableId, data);
		return newRowId;
	}
	
	@Override
	public Data cacheGet(WutRequest request) throws MissingParameterException {
		return null;
	}
	
	@Override
	public boolean cachePut(WutRequest request, Data data) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData username = new IdData(request.getUser().getId());
		
		StringData tableStringData = request.getParameter("table");
		IdData tableId = new IdData(tableStringData.toRawString());
		
		MappedData requestData = request.getParameter("data");
		
		IdData rowId = new IdData(((ScalarData) data).toRawString());
		
		Data updateResult = cacheProvider.update(application, customer, username, tableId, rowId, requestData); //(application, customer, user, tableId, data);
		return updateResult != null;
	}
}
