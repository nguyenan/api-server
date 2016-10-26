package com.wut.datasources;

//import com.wut.datasources.aws.AmazonSimpleDatabaseSource;
//import com.wut.datasources.aws.DynamoDb;
//import com.wut.datasources.google.GoogleSpreadsheetSource;

public class DataSourceManager {
	//private static AmazonSimpleDatabaseSource simpleDb = new AmazonSimpleDatabaseSource();
	//private static DynamoDb dynamoDb = new DynamoDb();
	//private static GoogleSpreadsheetSource gSpreadsheet;
	private static DataSourceManager singleton = new DataSourceManager();
	
	public DataSourceManager() {
//		if (gSpreadsheet == null) {
//			// TODO remove generic username/password
//			gSpreadsheet = new GoogleSpreadsheetSource("fake.palmiter@gmail.com", "fakepassword");
//		}
	}
	
//	public ScalarSource getScalarSource() {
//		return simpleDb;
//	}
	
	public ListSource getListSource() {
		return null; // TODO ummmm yeah
	}
	
//	public TableSource getTableSource() {
//		return dynamoDb;
//	}

	public static DataSourceManager create() {
		return singleton;
	}
}
