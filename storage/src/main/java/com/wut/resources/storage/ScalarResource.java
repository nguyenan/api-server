package com.wut.resources.storage;

import com.wut.datasources.aws.DynamoDb;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.scalar.DefaultScalarProvider;
import com.wut.provider.scalar.ScalarProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;

public class ScalarResource extends ObsoleteCrudResource {
//	private DataSourceManager dsm = DataSourceManager.create();
//	private ScalarSource store = dsm.getScalarSource();
	//private static final String TABLE = "_scalars_";
	
	private static final long serialVersionUID = -4594122748457947394L;
	private static ScalarProvider provider; // = ProviderManager.create().getDefaultScalarProvider();
	
	static {
		DynamoDb dynamoDbSource = new DynamoDb();
		provider = new DefaultScalarProvider(dynamoDbSource);
	}
	
	// TODO change resource name to table and attribute table to type (or
	// schema)

	@Override
	public String getName() {
		return "scalar";
	}

	// TODO not using scope properly (USERS SHARE DATA!!!!)

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String id = request.getScopeWithId();
		Data d = provider.read(customer, application, new IdData(id));
		// TODO do this here or in store???
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		}
		return d;
	}

	@Override
	public Data delete(WutRequest request) throws Exception {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String id = request.getScopeWithId();
		// TODO make deleteScalar()
		BooleanData wasDeleted = provider.update(customer, application, new IdData(id), null);
		return wasDeleted;
	}

	@Override
	public Data update(WutRequest request) throws Exception {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String id = request.getScopeWithId();
		StringData data = request.getParameter("data");
		BooleanData wasUpdated = provider.update(customer, application, new IdData(id), data);
		return wasUpdated;
	}

	@Override
	public Data create(WutRequest request) throws Exception {
//		String table = getTableNameParam(request);
//		Map<String, String> data = getDataParam(request);
//		String id = store.insertRow(table, data);
//		return new IdData(id);
//		
//		String id = request.getParameter("id");
//		StringData data = request.getParameter("data");
//		boolean wasUpdated = store.updateScalar(id, data);
//		return wasUpdated ? MessageData.SUCCESS : MessageData.FAILURE;
		
		return new MessageData("This method, create, on Scalar is not supported yet -- TDB");
	}

	// PARAMETER GETTING OPERATIONS

//	public static String getTableNameParam(WutRequest request)
//			throws MissingParameterException {
//		String table = request.getStringParameter("table");
//		String scope = request.getScope();
//		String tableName = scope + "_" + table;
//		return tableName;
//	}
//
//	public static String getTableIdParam(WutRequest request)
//			throws MissingParameterException {
//		return getTableIdParam(request, false);
//	}
//
//	public static String getTableIdParam(WutRequest request, boolean isOptional)
//			throws MissingParameterException {
//		String id;
//		if (isOptional) {
//			id = request.getOptionalParameter("id");
//		} else {
//			id = request.getStringParameter("id");
//		}
//		return id;
//	}
//
//	public static Map<String, String> getDataParam(WutRequest request)
//			throws MissingParameterException {
//		MappedData map = (MappedData) request.getParameters();
//		MappedData data = (MappedData) map.get("data");
//		if (data == null) {
//			throw new MissingParameterException("data");
//		}
//		Map<String, String> hashMap = new HashMap<String, String>();
//		for (String key : data.getMap().keySet()) {
//			Data valueData = data.get(key);
//			hashMap.put(key, String.valueOf(valueData));
//		}
//		return hashMap;
//	}
//
//	public static Map<String, String> getSchemaParam(WutRequest request) throws MissingParameterException {
//		String dataStr = request.getStringParameter("data");
//		Map<String, String> data = URLArguments.parseMap(dataStr);
//		return data;
//	}

}