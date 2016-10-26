package com.wut.resources.schema;
/*
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wut.datasources.DataSourceManager;
import com.wut.datasources.RudSource;
import com.wut.datasources.TableSource;
import com.wut.datasources.tablestore.SingleTableStore;
import com.wut.datasources.tablestore.bigtable.BigTable;
import com.wut.model.Data;
import com.wut.model.ModelManager;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.provider.ProviderManager;
import com.wut.support.ErrorHandler;
import com.wut.support.TextHelper;

public class SchemaStore implements RudSource {
	private static final IdData SCHEMA_TABLE_ID = new IdData("schema"); 
	private ModelManager masterModel = new ModelManager();
	private TableProvider provider = ProviderManager.create().getTableProvider();

	@Override
	public Data read(String name) {
		if (name == null) {
			return provider.getRows(SCHEMA_TABLE_ID);
		} else {
			MappedData row = provider.getRow(SCHEMA_TABLE_ID, new IdData(name));
			if (row == null) {
				return MessageData.NO_DATA_FOUND;
			}
			return row;
		}
	}
	
	@Override
	public Data update(String name, Map<String, String> data) {
		List<String> errors = verifySchema(data);
		if (errors.size() != 0) {
			String errMsg = "invalid update of schema. ";
			errMsg += TextHelper.collectionToString(errors);
			ErrorHandler.userError(errMsg);
			MessageData err = new MessageData(1000, "invalid schema for " + name, errMsg);
			return err;
		} else {
			//data.put("id", name);
			MappedData newSchema = new MappedData(data);
			provider.updateRow(SCHEMA_TABLE_ID, new IdData(name), newSchema);
			return MessageData.SUCCESS;
		}
	}
	
	@Override
	public Data delete(String name) {
		provider.deleteRow(SCHEMA_TABLE_ID, new IdData(name));
		return MessageData.SUCCESS;
	}
	
	
	private List<String> verifySchema(Map<String, String> data) {
		Set<String> validTypes = masterModel.getTypes();
		
		List<String> errors = new ArrayList<String>();
		
		for (String param : data.keySet()) {
			String paramType = data.get(param);
			if (!validTypes.contains(paramType)) {
				errors.add("parameter " + param + " has an invalid type of " + paramType);
			}
		}
		
		// TODO re-enable validation by returning errors
		//return errors;
		return new ArrayList<String>();
	}
	
}
*/
