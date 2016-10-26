package com.wut.provider;
/*
import S3FileSource;

import com.wut.datasources.aws.DynamoDb;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;
import com.wut.provider.scalar.DefaultScalarProvider;
import com.wut.provider.scalar.ScalarProvider;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.UniqueRowProvider;

public class ProviderManager {
	private static TableProvider table;
	private static Provider list;
	private static Provider map;
	private static ScalarProvider scalar;
	private static FileProvider file;
	//private static TableProvider settings;
	private static ProviderManager singleton = new ProviderManager();
	
	public ProviderManager() {
		// TODO use DataSourceManager here to get sources !!!!
		
//		DynamoDb dynamoDbSource = new DynamoDb();
//		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(dynamoDbSource);
//		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
//		UniqueRowProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
//		table = uniqueRowProvider;
//		
//		// TODO fix
//		//AmazonSimpleDatabaseSource aws = new AmazonSimpleDatabaseSource();
////		GoogleSpreadsheetSource googleSpreadsheet = 
//		scalar = new DefaultScalarProvider(dynamoDbSource);
//		
//		file = new DefaultFileProvider(new S3FileSource());
	}
	
	public TableProvider getTableProvider() {
		return table;
	}
	
	public Provider getListProvider() {
		return list;
	}
	
	public Provider getMapProvider() {
		return map;
	}
	
	public ScalarProvider getDefaultScalarProvider() {
//		GoogleSpreadsheetSource googleSpreadsheetSource = new GoogleSpreadsheetSource(username, password);
//		ScalarProvider scalarProvider = new DefaultScalarProvider(aws);
		return scalar;
	}

	public static ProviderManager create() {
		return singleton;
	}

	public FileProvider getFileProvider() {
		return file;
	}
}
*/