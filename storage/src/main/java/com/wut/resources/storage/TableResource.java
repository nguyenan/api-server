package com.wut.resources.storage;

import com.wut.datasources.aws.DynamoDb;
import com.wut.datasources.cassandra.CassandraSource;
import com.wut.datasources.jdbc.derby.Derby;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.row.TableRowSourceToTableProviderAdapter;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;

public class TableResource extends CrudResource {
	private static final long serialVersionUID = -9114702464429163887L;
	private static TableResourceProvider provider = getProvider(); // = ProviderManager.create().getTableProvider();
	
	public static TableProvider getTableProvider() {
		//DynamoDb dynamoDbSource = new DynamoDb();
		CassandraSource dynamoDbSource = new CassandraSource();
		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(dynamoDbSource);
		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
		TableProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
		return uniqueRowProvider;
	}
	
	public static TableResourceProvider getProvider() {
		TableProvider provider = getTableProvider();
		TableResourceProvider tableResourceProvider = new TableResourceProvider(provider);
		return tableResourceProvider;
	}
	
	// TODO not used
//	public static TableProvider getCacheProvider() {
//		Derby derbySource = new Derby();
//		TableRowToTableProviderAdapter adapter = new TableRowToTableProviderAdapter(derbySource);
//		FlatTableProvider flatTableProvider = new FlatTableProvider(adapter, "webutilitykit");
//		TableProvider provider = new UniqueRowProvider(flatTableProvider);
//		return provider;
//	}
	
	// TODO change resource name to table and attribute table to type (or schema)
	
	public TableResource() {
		super("table", null);
	}
	
	@Override
	public String getName() {
		return "table";
	}
	
	// TODO not using scope properly (USERS SHARE DATA!!!!)

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request, true);
		MappedData filter = request.getParameter("filter", true);
		
		Data result = provider.read(application, customer, user, tableId, rowId, filter);
		return result;
	}
	
	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request);
		
		Data result = provider.delete(application, customer, user, tableId, rowId);
		return result;
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		IdData tableId = getTableScope(request);
		IdData rowId = getRowIdParam(request);
		MappedData data = getDataParam(request);
		
		Data result = provider.update(application, customer, user, tableId, rowId, data);
		return result;
	}
	
	@Override
	public Data create(WutRequest request) throws MissingParameterException {
		IdData application = new IdData(request.getApplication());
		IdData customer = new IdData(request.getCustomer());
		IdData user = new IdData(request.getUser().getId());
		
		IdData tableId = getTableScope(request);
		MappedData data = getDataParam(request);
		
		IdData newRowId = provider.create(application, customer, user, tableId, data);
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
		if (id != null) {
			return new IdData(id);
		}
		return null;
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
		//String scope = request.scopify(table.toRawString());
		return new IdData(table.toRawString());
	}
}

