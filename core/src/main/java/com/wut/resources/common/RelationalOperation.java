package com.wut.resources.common;

import java.util.List;
import java.util.Map;

import com.wut.datasources.tablestore.RelationalStore;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.URLArguments;

// RelationalBasesOperation or RelationalBackedOperation
//public class RelationalOperation extends AbstractOperation {
//	private RelationalStore store;
//
//	public RelationalOperation(String name, RelationalStore store) {
//		super(name);
//		this.store = store;
//	}
//	
//	public static String getTableNameParam(WutRequest request) throws MissingParameterException {
//		String table = request.getStringParameter("table");
//		String scope = request.getScope();
//		String tableName = scope + "_" + table;
//		return tableName;
//	}
//	
//	public static String getTableIdParam(WutRequest request) throws MissingParameterException {
//		return getTableIdParam(request, false);
//	}
//	
//	public static String getTableIdParam(WutRequest request,  boolean isOptional) throws MissingParameterException {
//		String id;
//		if (isOptional) {
//			id = request.getOptionalParameter("id");
//		} else {
//			id = request.getStringParameter("id");
//		}
//		return id;
//	}
//	
//	public static Map<String, String> getDataParam(WutRequest request) throws MissingParameterException {
//		String dataStr = request.getStringParameter("data");
//		Map<String, String> data = URLArguments.parseMap(dataStr);
//		return data;
//	}
//	
//	public static Map<String, String> getSchemaParam(WutRequest request) throws MissingParameterException {
//		String dataStr = request.getStringParameter("data");
//		Map<String, String> data = URLArguments.parseMap(dataStr);
//		return data;
//	}
//	
//	
//	protected Data create(String table, Map<String, String> data) {
//		final String id = store.insertRow(table, data);
//		return new IdData(id);
//	}
//	
//	protected Data read(String table, String id) {
//		if (id == null) {
//			List<Map<String,String>> data = store.getAllRows(table);
//			ListData listD = ListData.convert(data);
//			return listD;
//		} else {
//			Map<String,String> map = store.getRow(table, id);
//			MappedData dataMap = MappedData.convert(map);
//			return dataMap;
//		}
//	}
//	
//	protected Data update(String table, String id, Map<String, String> data) {
//		boolean wasUpdated = store.updateRow(table, id, data);
//		return wasUpdated ? MessageData.SUCCESS : MessageData.FAILURE;
//	}
//	
//	protected Data delete(String table, String id) {
//		boolean wasDeleted = store.deleteRow(table, id);
//		return wasDeleted ? MessageData.SUCCESS : MessageData.FAILURE;
//	}
//
//	@Override
//	public Data perform(WutRequest request) throws Exception {
//		if (name.equalsIgnoreCase("CREATE")) {
//			String table = getTableNameParam(request);
//			Map<String, String> data = getDataParam(request);
//			return create(table, data);
//		} else if (name.equalsIgnoreCase("READ")) {
//			String table = getTableNameParam(request);
//			String id = getTableIdParam(request, true);
//			return read(table, id);
//		} else if (name.equalsIgnoreCase("UPDATE")) {
//			String table = getTableNameParam(request);
//			String id = getTableIdParam(request);
//			Map<String, String> data = getDataParam(request);
//			return update(table, id, data);
//		} else if (name.equalsIgnoreCase("DELETE")) {
//			String table = getTableNameParam(request);
//			String id = getTableIdParam(request);
//			return delete(table, id);
//		} else {
//			return ErrorData.OP_NOT_IMPLIMENTED;
//		}
//	}
//	
//
//}
