package com.wut.datasources.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.CreateTableRequest;
import com.amazonaws.services.dynamodb.model.DeleteItemRequest;
import com.amazonaws.services.dynamodb.model.DeleteItemResult;
import com.amazonaws.services.dynamodb.model.DeleteTableRequest;
//import com.amazonaws.services.dynamodb.model.DeleteTableResult;
import com.amazonaws.services.dynamodb.model.DescribeTableRequest;
import com.amazonaws.services.dynamodb.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.KeySchema;
import com.amazonaws.services.dynamodb.model.KeySchemaElement;
import com.amazonaws.services.dynamodb.model.ListTablesRequest;
import com.amazonaws.services.dynamodb.model.ListTablesResult;
import com.amazonaws.services.dynamodb.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodb.model.ProvisionedThroughputExceededException;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.services.dynamodb.model.ReturnValue;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.dynamodb.model.TableDescription;
import com.amazonaws.services.dynamodb.model.TableStatus;
import com.wut.datasources.MultiSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.Language;
import com.wut.support.StringHelper;
import com.wut.support.settings.SettingsManager;

@SuppressWarnings("deprecation")
public class DynamoDb extends MultiSource {
	
	private static final String AMAZON_SECRET_KEY = SettingsManager.getSystemSetting("amazon.dynamodb.accesskey");
	private static final String AMAZON_ACCESS_KEY = SettingsManager.getSystemSetting("amazon.dynamodb.secretkey");

	static {
		// CHECKING CERTIFICATES OCCASIONALLY CAUSES PROBLEMS WITH DYNAMO DB
		System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
	}
	
	// TODO make this static
	private AmazonDynamoDBClient dynamoDB;
	
	public DynamoDb() {
		AWSCredentials myCredentials = new BasicAWSCredentials(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);
		dynamoDB = new AmazonDynamoDBClient(myCredentials);
	}
	
	public DynamoDb(String accessKey, String secretKey) {
		AWSCredentials myCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		dynamoDB = new AmazonDynamoDBClient(myCredentials);
	}
	
	public List<String> getTables() {
		ArrayList<String> tableNames = new ArrayList<String>();
		String lastEvaluatedTableName = null;
        do {
            
            ListTablesRequest listTablesRequest = new ListTablesRequest()
            .withLimit(10)
            .withExclusiveStartTableName(lastEvaluatedTableName);
            
            ListTablesResult result = dynamoDB.listTables(listTablesRequest);
            lastEvaluatedTableName = result.getLastEvaluatedTableName();
            
            for (String name : result.getTableNames()) {
                tableNames.add(name);
            }
            
        } while (lastEvaluatedTableName != null);
        
        return tableNames;
	}
	
	public void createTable(String tableName) {
		try {
			//String tableName = "my-favorite-movies-table";

			// Create a table with a primary key named 'name', which holds a
			// string
			CreateTableRequest createTableRequest = new CreateTableRequest()
					.withTableName(tableName)
					.withKeySchema(
							new KeySchema(new KeySchemaElement()
									.withAttributeName("id")
									.withAttributeType("S")))
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(
									5L).withWriteCapacityUnits(5L));
			TableDescription createdTableDescription = dynamoDB.createTable(
					createTableRequest).getTableDescription();
			System.out.println("Created Table: " + createdTableDescription);

			// Wait for it to become active
			waitForTableToBecomeAvailable(tableName);

			// Describe our new table
			DescribeTableRequest describeTableRequest = new DescribeTableRequest()
					.withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(
					describeTableRequest).getTable();
			System.out.println("Table Description: " + tableDescription);

		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with AWS, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
	public void deleteTable(String tableName) {
		try {
			DeleteTableRequest deleteTableRequest = new DeleteTableRequest()
			  .withTableName(tableName);
			//DeleteTableResult result = dynamoDB.deleteTable(deleteTableRequest);
			dynamoDB.deleteTable(deleteTableRequest);
			System.out.println("Deleted Table: " + tableName);
			
			// Describe our new table
			DescribeTableRequest describeTableRequest = new DescribeTableRequest()
					.withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(
					describeTableRequest).getTable();
			System.out.println("Table description for deleted table: " + tableDescription);
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with AWS, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	
//	private static String getAttributeValueAsString(AttributeValue attr) {
//		AttributeValue updated = row.get("updated");
//		attr.get
//		String updatedString = updated.getS();
//		if (updatedString != null) {
//			updated.setN(updatedString);
//			updated.setS(null);
//		}
//	}
	
	public int copyScan(ScanResult scanResult, String toTableName) {
		int copyCount = 0;

		for (Map<String, AttributeValue> row : scanResult.getItems()) {
			// TODO this should be done in a provider
			// if not soft deleted, add to results
			String softDelete = String.valueOf(row.get("_deleted_"));
			if (!softDelete.equals("true")) {
				//AttributeValue attr =  new AttributeValue();
				AttributeValue updated = row.get("updated");
				String updatedString = updated.getS();
				if (updatedString != null) {
					updated.setN(updatedString);
					updated.setS(null);
				}
				PutItemRequest putItemRequest = new PutItemRequest(toTableName, row);
				PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
				// TODO CHECK FOR SUCCESS
				copyCount++;
				System.out.println("> copied:" + row);
			}
		}
		
		return copyCount;
	}
	

	
	private ScanResult exponentialBackoffScan(ScanRequest scanRequest) {
		int currentRetry = 0;
		int maxRetries = 30;
		boolean retry = false;
		
		// backoff algorithm from http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIRetries
		
		do {
			retry = false;
			
			try {
				ScanResult scanResult = dynamoDB.scan(scanRequest);
				return scanResult;
			} catch (ProvisionedThroughputExceededException e) {
				e.printStackTrace();
				retry = true;
				System.out.println("RETRYING ATTEMPT " + currentRetry);
			} catch (AmazonServiceException e) {
				e.printStackTrace();
			} catch (AmazonClientException e) {
				e.printStackTrace();
			}
			
			if (retry) {
				try {
					Thread.sleep(2^currentRetry * 50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} while (retry && currentRetry < maxRetries);
		
		return null;
	}
	
	public int copyTable(String fromTableName, String toTableName) {
		return copyTable(this, fromTableName, toTableName);
	}
	
	public int copyTable(DynamoDb fromDb, String fromTableName, String toTableName) {
		int copyCount = 0;
		
		ScanRequest scanRequest = new ScanRequest(fromTableName).withLimit(REQUEST_BATCH_SIZE);

		ScanResult scanResult = fromDb.exponentialBackoffScan(scanRequest);
		
		//addScanResults(scanResult, results);
		
		copyCount = copyScan(scanResult, toTableName);

		int scannedCount = scanResult.getScannedCount();

		while (scannedCount == REQUEST_BATCH_SIZE) {
			Key lastKeyScanned = scanResult.getLastEvaluatedKey();
			scanRequest = new ScanRequest(fromTableName).withLimit(REQUEST_BATCH_SIZE).withExclusiveStartKey(lastKeyScanned);
			scanResult = fromDb.exponentialBackoffScan(scanRequest);
			
			copyCount += copyScan(scanResult, toTableName);
			
			scannedCount = scanResult.getScannedCount();
			
			System.out.println("Copied:" + copyCount + " Scanned " + scannedCount);
		}
		
		return copyCount;
	}
	
	public static void main(String[] args) throws IOException {
		//DynamoDb db = new DynamoDb();
		DynamoDb fromDb = new DynamoDb(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);
		DynamoDb toDb = new DynamoDb();
		
		System.out.println("fromDb="+fromDb.getTables());
		System.out.println("toDb="+toDb.getTables());
		
		//IdData fromTable = new IdData("webutilitykittest2");
		IdData fromTable = new IdData("webutilitykittest1");
		IdData toTable = new IdData("test3");
	
		int copyCount = toDb.copyTable(fromDb, fromTable.toRawString(), toTable.toRawString());
		
		System.out.println("Copy finished. Copied:" + copyCount);
	}
	
	public static IdData customer = new IdData("www.test.com");
	public static IdData application = new IdData("test");
	
	public static void main4(String[] args) throws IOException {
		//DynamoDb db = new DynamoDb();
		DynamoDb db = new DynamoDb(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);

		System.out.println("table="+db.getTables());
		
		//IdData fromTable = new IdData("webutilitykittest2");
		IdData fromTable = new IdData("webutilitykittest1");
		//IdData fromTable = new IdData("testonetwothree");
		
		ListData allFromRows = db.getAllRows(customer, application, fromTable);
		Iterator<? extends Data> iterator = allFromRows.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			Data row = iterator.next();
			System.out.println("ROW " + i + ":" + row);
		}
	}

	public static void main1(String[] args) throws IOException {
		DynamoDb fromDb = new DynamoDb();
		
		IdData fromTable = new IdData("flat2");
		
//		ListData allFromRows = db.getAllRows(fromTable);
//		Iterator<? extends Data> iterator = allFromRows.iterator();
//		int i = 0;
//		while (iterator.hasNext()) {
//			i++;
//			Data row = iterator.next();
//			System.out.println("ROW " + i + ":" + row);
//		}
		//System.out.println("From Table=" + allFromRows);

		IdData toTable = new IdData("webutilitykittest1");

		//db.createTable(toTable.toRawString());
		
		//ListData toTableBeforeCopy = db.getAllRows(toTable);
		//System.out.println("To Table=" + toTableBeforeCopy);
		
		int copyCount = fromDb.copyTable(fromTable.toRawString(), toTable.toRawString());
		
		System.out.println("Copy finished. Copied:" + copyCount);
		//ListData toTableAfterCopy = db.getAllRows(toTable);
		//System.out.println("To Table=" + toTableAfterCopy);
		
	}

	public static void main2(String[] args) throws IOException {
		Random rand = new Random();
		DynamoDb db = new DynamoDb();
		
		// create a test table
		IdData table = new IdData("test");
		db.createTable(table.toRawString());
		
		// No rows yet
		ListData d = db.getAllRows(customer, application, table);
		System.out.println("T1=" + d);
		
		// make a new row
		MappedData newRow = new MappedData();
		newRow.put("name", "HotProduct");
		newRow.put("description", "Solid!");
		newRow.put("brand", "Hipster Inc");
		newRow.put("price", "100");
		newRow.put("msrp", "500");
		IdData rowId = db.insertRow(customer, application, table, newRow);

		// one new row added
		ListData d2 = db.getAllRows(customer, application, table);
		System.out.println("T2=" + d2);
		
		// update the msrp
		String newMSRP = String.valueOf(rand.nextInt(1000));
		newRow.put("msrp", newMSRP);
		db.updateRow(customer, application, table, rowId, newRow);
		
		// new msrp
		ListData d3 = db.getAllRows(customer, application, table);
		System.out.println("T3=" + d3);
		
		// just the updated product
		MappedData r1 = db.getRow(customer, application, table, rowId);
		System.out.println("R1=" + r1);
		
		// all products with a price of 100
		MappedData filter = new MappedData();
		filter.put("price", "100");
		ListData d4 = db.getRowsWithFilter(customer, application, table, filter);
		System.out.println("T4=" + d4);
		
		// delete the added product
		//db.deleteRow(table, rowId);
		
		// back to an empy table
		ListData d5 = db.getAllRows(customer, application, table);
		System.out.println("T5=" + d5);
		
//		ListData d6 = db.getRowsWithFilter(table, filter);
//		System.out.println("T6=" + d5);
		
		// clean up the test table
		//db.deleteTable("test");
		
		System.out.println("Done.");
	}

//	private static Map<String, AttributeValue> newItem(String name, int year,
//			String rating, String... fans) {
//		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//		item.put("name", new AttributeValue(name));
//		item.put("year", new AttributeValue().withN(Integer.toString(year)));
//		item.put("rating", new AttributeValue(rating));
//		item.put("fans", new AttributeValue().withSS(fans));
//
//		return item;
//	}

	private void waitForTableToBecomeAvailable(String tableName) {
		System.out.println("Waiting for " + tableName + " to become ACTIVE...");

		long startTime = System.currentTimeMillis();
		long endTime = startTime + (10 * 60 * 1000);
		while (System.currentTimeMillis() < endTime) {
			try {
				Thread.sleep(1000 * 20);
			} catch (Exception e) {
			}
			try {
				DescribeTableRequest request = new DescribeTableRequest()
						.withTableName(tableName);
				TableDescription tableDescription = dynamoDB.describeTable(
						request).getTable();
				String tableStatus = tableDescription.getTableStatus();
				System.out.println("  - current state: " + tableStatus);
				if (tableStatus.equals(TableStatus.ACTIVE.toString()))
					return;
			} catch (AmazonServiceException ase) {
				if (ase.getErrorCode().equalsIgnoreCase(
						"ResourceNotFoundException") == false)
					throw ase;
			}
		}

		throw new RuntimeException("Table " + tableName + " never went active");
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		String tableName = tableId.toRawString();
		
		GetItemRequest getItemRequest = new GetItemRequest()
		  .withTableName(tableName)
		  .withKey(new Key()
		  .withHashKeyElement(new AttributeValue().withS(rowId.toRawString())));

		GetItemResult result = dynamoDB.getItem(getItemRequest); // TODO wrap with try/catch to catch provisioning problems
		Map<String, AttributeValue> row = result.getItem();
		
		if (row != null) {
			MappedData rowResult = convertDynamoRowToWutModel(row);
			return rowResult;
		} else {
			return null;
		}
	}

	@Override
	public ListData getAllRows(IdData customer, IdData application, IdData tableId) {
		String tableName = tableId.toRawString();
		
		// Scan items for movies with a year attribute greater than 1985
		//HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//		Condition condition = new Condition().withComparisonOperator(
//				ComparisonOperator.GT.toString()).withAttributeValueList(
//				new AttributeValue().withN("1985"));
//		scanFilter.put("year", condition);
		
		// full table scan
		ScanRequest scanRequest = new ScanRequest(tableName);
		//.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		
		System.out.println("Result: " + scanResult);

		ListData results = new ListData();
		
		addScanResults(scanResult, results);
		
		return results;
	}
	
	private static int REQUEST_BATCH_SIZE = 100;
	
	@Override
	public ListData getRowsWithFilter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		String tableName = tableId.toRawString();

		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		if (filter.size() > 0) {
			// build filter conditions
			for (ScalarData key : filter.getMap().keySet()) {
				String value = String.valueOf(filter.get(key));
				
				String comparisonOperator = ComparisonOperator.EQ.toString();
				if (value.startsWith("<")) {
					comparisonOperator = ComparisonOperator.LE.toString();
					//value = StringUtils.trimLeadingCharacter(value, '<');
					value = StringHelper.removePrefix("<", value);
				} else if (value.startsWith(">")) {
					comparisonOperator = ComparisonOperator.GE.toString();
					//value = StringUtils.trimLeadingCharacter(value, '>');
					value = StringHelper.removePrefix(">", value);
				}
				Condition condition = new Condition().withComparisonOperator(
						comparisonOperator).withAttributeValueList(
						new AttributeValue().withS(value));
				String keyStr = key.toRawString();
				scanFilter.put(keyStr, condition);
			}
		}
		
		// full table scan
		ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter).withLimit(REQUEST_BATCH_SIZE);
		//scanRequest.withLimit(REQUEST_BATCH_SIZE);
		
		ListData results = new ListData();
		
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		
		System.out.println("Result: " + scanResult);

		addScanResults(scanResult, results);

		int scannedCount = scanResult.getScannedCount();

		while (scannedCount == REQUEST_BATCH_SIZE) {
			Key lastKeyScanned = scanResult.getLastEvaluatedKey();
			scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter).withLimit(REQUEST_BATCH_SIZE).withExclusiveStartKey(lastKeyScanned);
			scanResult = dynamoDB.scan(scanRequest);
			addScanResults(scanResult, results);
			scannedCount = scanResult.getScannedCount();
		}
		
		return results;
	}

	private void addScanResults(ScanResult scanResult, ListData results) {
		for (Map<String, AttributeValue> row : scanResult.getItems()) {
			// convert dynamo map to wut model map
			MappedData newRow = convertDynamoRowToWutModel(row);

			// TODO this should be done in a provider
			// if not soft deleted, add to results
			String softDelete = String.valueOf(newRow.get("_deleted_"));
			if (!softDelete.equals("true")) {
				results.add(newRow);
			}
		}
	}
	
	private MappedData convertDynamoRowToWutModel(Map<String, AttributeValue> row) {
		MappedData newRow = new MappedData();
		for (String key : row.keySet()) {
			AttributeValue value = row.get(key);
			String valueString = value.getS();
			
			if (key.equals("updated") && valueString == null) {
				valueString = value.getN();
			}
			
			newRow.put(key, valueString);
		}
		return newRow;
	}
	
	// getRowsWithFilter()
	// Scan items for movies with a year attribute greater than 1985
//			HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//			Condition condition = new Condition().withComparisonOperator(
//					ComparisonOperator.GT.toString()).withAttributeValueList(
//					new AttributeValue().withN("1985"));
//			scanFilter.put("year", condition);
//			ScanRequest scanRequest = new ScanRequest(tableName)
//					.withScanFilter(scanFilter);
//			ScanResult scanResult = dynamoDB.scan(scanRequest);

	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		String tableName = tableId.toRawString();
		
		Map<String, AttributeValue> newItem = new HashMap<String, AttributeValue>();
		for (ScalarData key : data.keys()) {
			String keyString = key.toRawString();
			Data keyValue = data.get(key);
			String keyValueString = keyValue.toString();
			if (Language.isNotBlank(keyValueString)) {
				AttributeValue attrValue = new AttributeValue(keyValueString);
				newItem.put(keyString, attrValue);
			}
			
//			item.put("year", new AttributeValue().withN(Integer.toString(year)));
//			item.put("rating", new AttributeValue(rating));
//			item.put("fans", new AttributeValue().withSS(fans));
		}
//		String idScope = tableId + ":" + rowId.toRawString();
//		newItem.put("id", new AttributeValue(idScope));
//		long curDateTime = new Date().getTime();
//		newItem.put("updated", new AttributeValue(String.valueOf(curDateTime)));
		newItem.put("id", new AttributeValue(rowId.toRawString()));
		
		long curDateTime = new Date().getTime();
		AttributeValue updatedAttr = new AttributeValue();
		updatedAttr.setN(String.valueOf(curDateTime));
		newItem.put("updated", updatedAttr);
		
		PutItemRequest putItemRequest = new PutItemRequest(tableName, newItem);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		System.out.println("Result: " + putItemResult);

		// TODO fix this!!!
		return BooleanData.TRUE;
	}
	
	
	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		String tableName = tableId.toRawString();

		Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();

		// HashMap<String, AttributeValue> key = new HashMap<String,
		// AttributeValue>();
		// key.put("id", new AttributeValue().withS(rowId.toRawString()));

		Key key = new Key();
		key.setHashKeyElement(new AttributeValue().withS(rowId.toRawString()));

		// expectedValues.put("InPublication",
		// new ExpectedAttributeValue()
		// .withValue(new AttributeValue().withN("0"))); // Boolean stored as 0
		// or 1.

		ReturnValue returnValues = ReturnValue.ALL_OLD;

		DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
				.withTableName(tableName).withKey(key)
				.withExpected(expectedValues).withReturnValues(returnValues);

		@SuppressWarnings("unused")
		DeleteItemResult result = dynamoDB.deleteItem(deleteItemRequest);

		// Check the response.
		// System.out.println("Printing item that was deleted...");
		// //printItem(result.getAttributes());
		//
		// DeleteItemRequest deleteItemRequest = new
		// DeleteItemRequest(tableName, "asdfasd");
		// PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		// System.out.println("Result: " + putItemResult);

		return BooleanData.TRUE;

	}

	@Override
	public BooleanData deleteRows(IdData customer, IdData application, IdData tableId, MappedData filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
