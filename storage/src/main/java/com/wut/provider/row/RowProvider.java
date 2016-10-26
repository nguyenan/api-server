package com.wut.provider.row;

import com.wut.datasources.aws.DynamoDb;
import com.wut.datasources.jdbc.derby.Derby;
import com.wut.provider.row.DefaultTableRowProvider;
import com.wut.provider.row.FlatTableRowProvider;
import com.wut.provider.row.TableRowProvider;
//import com.wut.provider.row.TableRowProviderToTableProviderAdapter;
import com.wut.provider.row.UniqueTableRowProvider;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;

public class RowProvider {

	public static TableResourceProvider getRowProvider() {
		return getProvider();
	}

	private static TableProvider getTableProvider() {
		DynamoDb dynamoDbSource = new DynamoDb();
		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(dynamoDbSource);
		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
		TableProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
		return uniqueRowProvider;
	}
	
	private static TableResourceProvider getProvider() {
		TableProvider provider = getTableProvider();
		TableResourceProvider tableResourceProvider = new TableResourceProvider(provider);
		return tableResourceProvider;
	}
	
	
	public static TableResourceProvider getRowCache() {
		Derby derbySource = new Derby();
		DefaultTableRowProvider defaultTableProvider = new DefaultTableRowProvider(derbySource);
		FlatTableRowProvider flatTableProvider = new FlatTableRowProvider(defaultTableProvider, "webutilitykit");
		TableRowProvider uniqueProvider = new UniqueTableRowProvider(flatTableProvider);
		
		TableRowProviderToTableProviderAdapter adapterProvider = new TableRowProviderToTableProviderAdapter(uniqueProvider);
		
		TableResourceProvider tableResourceProvider = new TableResourceProvider(adapterProvider);
		
		return tableResourceProvider;
	}
}
