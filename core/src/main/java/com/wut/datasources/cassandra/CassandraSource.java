package com.wut.datasources.cassandra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.Policies;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.wut.datasources.MultiSource;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.support.StringHelper;

public class CassandraSource extends MultiSource {
	//private static final String KEYSPACE = "mykeyspace";
	private static final String KEYSPACE = "webutilitykit";
	private Cluster cluster;
	private Session session;
	//private static int REQUEST_BATCH_SIZE = 100;
	private static List<String> SPECIAL_COLUMNS =  Arrays.asList("id"); //Arrays.asList("updated", "created", "updator", "creator", "id");
 
	public CassandraSource() {
		cluster = Cluster.builder().addContactPoint("db3.tend.co")
				.withReconnectionPolicy(new ConstantReconnectionPolicy(5000L))
				.withRetryPolicy(Policies.defaultRetryPolicy())
				.build();
		doConnectionStuff();
	}
	
	public void doConnectionStuff() {		
		try {
			session = cluster.connect(KEYSPACE);
		} catch (InvalidQueryException e) {
			// create keyspace here
			
			// CREATE KEYSPACE webutilitykit WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };
			try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
            	System.out.println("sleep exception" + e1);
            }
			session = cluster.connect(KEYSPACE);
		}
	}

	public List<String> getTables() {
		ArrayList<String> tableNames = new ArrayList<String>();

		// TODO list table
		
		/*
		 * If you want to do this outside of the cqlsh tool you can query the schema_keyspaces table in the system keyspace. There's also a table called schema_columnfamilies which contains information about all tables.

The DESCRIBE and SHOW commands only work in cqlsh and cassandra-cli.
		 */
		
		// cqlsh> select columnfamily_name from system.schema_columnfamilies where keyspace_name = 'test';


		return tableNames;
	}

	/*public void createTable(String tableName) {
		StringBuilder createTableStatement = new StringBuilder();
		createTableStatement.append("CREATE TABLE IF NOT EXISTS ");
		createTableStatement.append(tableName);
		createTableStatement.append(" (");
		createTableStatement.append("rowId text, ");
		createTableStatement.append("tablename text, ");
		createTableStatement.append("creator text, ");
		createTableStatement.append("customer text, ");
		createTableStatement.append("application text, ");
		createTableStatement.append("updator text,");
		createTableStatement.append("permission text,");
		createTableStatement.append("updated bigint,");
		createTableStatement.append("data MAP<text,text>,");
		createTableStatement.append("tags LIST<text>,");
		//createTableStatement.append("PRIMARY KEY (customer, tablename, id)");
		//createTableStatement.append("PRIMARY KEY (tablename, id)");
		createTableStatement.append("PRIMARY KEY (customer, application, tablename, rowId)");
		createTableStatement.append(" );");
		// PRIMARY KEY (id, title, album, artist)

		String createTableStatementString = createTableStatement.toString();

		session.execute(createTableStatementString);

		// TODO check if succeeded

		// TODO create indexes (tags, updated, updator, creator, created)
		//session.execute("CREATE INDEX ON " + tableName + " (table);");
		try {
			session.execute("DROP INDEX tableIndex;");
		} catch (Exception e) {
			System.out.println("no index tableIndex to drop");
		}
		session.execute("CREATE INDEX tableIndex ON " + tableName + " (tablename);");
		
		
		try {
			session.execute("DROP INDEX updatedIndex;");
		} catch (Exception e) {
			System.out.println("no index tableIndex to drop");
		}
		session.execute("CREATE INDEX updatedIndex ON " + tableName + " (updated);"); // get tablename in here
		
		//session.execute("CREATE INDEX tablenameIndex ON " + tableName + " (tablename);");

		//session.execute("CREATE INDEX ON " + tableName + " (data);");
		
		
//		 CREATE INDEX user_state
//   		   ON myschema.users (state);
//   		   CREATE INDEX ON myschema.users (zip);
		 
	}*/

	public void deleteTable(String tableName) {
		StringBuilder dropTableStatement = new StringBuilder();
		dropTableStatement.append("DROP TABLE IF EXISTS ");
		dropTableStatement.append(KEYSPACE);
		dropTableStatement.append(".");
		dropTableStatement.append(tableName);
		dropTableStatement.append(";");
		
		String dropTableStatementString = dropTableStatement.toString();

		session.execute(dropTableStatementString);
	}

	// private static String getAttributeValueAsString(AttributeValue attr) {
	// AttributeValue updated = row.get("updated");
	// attr.get
	// String updatedString = updated.getS();
	// if (updatedString != null) {
	// updated.setN(updatedString);
	// updated.setS(null);
	// }
	// }

//	public static void main4(String[] args) throws IOException {
//		// DynamoDb db = new DynamoDb();
//		CassandraSource db = new CassandraSource();
//
//		System.out.println("table=" + db.getTables());
//
//		// IdData fromTable = new IdData("webutilitykittest2");
//		IdData fromTable = new IdData("webutilitykittest1");
//		// IdData fromTable = new IdData("testonetwothree");
//
//		ListData allFromRows = db.getAllRows(fromTable);
//		Iterator<? extends Data> iterator = allFromRows.iterator();
//		int i = 0;
//		while (iterator.hasNext()) {
//			i++;
//			Data row = iterator.next();
//			System.out.println("ROW " + i + ":" + row);
//		}
//	}

//	public static void main(String[] args) throws IOException {
//		IdData customer = new IdData("test");
//		IdData application = new IdData("test");
//		
//		Random rand = new Random();
//		CassandraSource db = new CassandraSource();
//
//		// delete the test table
//		IdData table = new IdData("flat2");
//		db.deleteTable(table.toRawString()); // WTF RUSSELL
//		
//		// create a test table
//		db.createTable(table.toRawString());
//
//		// No rows yet
//		ListData d = db.getAllRows(customer, application, table);
//		System.out.println("T1=" + d);
//
//		// make a new row
//		MappedData newRow = new MappedData();
//		//newRow.put("customer", "www.example.com");
//		newRow.put("name", "HotProduct");
//		newRow.put("description", "Solid!");
//		newRow.put("brand", "Hipster Inc");
//		newRow.put("price", "100");
//		newRow.put("msrp", "500");
//		IdData rowId = db.insertRow(customer, application, table, newRow);
//
//		// one new row added
//		ListData d2 = db.getAllRows(customer, application, table);
//		System.out.println("T2=" + d2);
//
//		// update the msrp
//		String newMSRP = String.valueOf(rand.nextInt(1000));
//		newRow.put("msrp", newMSRP);
//		db.updateRow(customer, application, table, rowId, newRow);
//
//		// new msrp
//		ListData d3 = db.getAllRows(customer, application, table);
//		System.out.println("T3=" + d3);
//
//		// just the updated product
//		MappedData r1 = db.getRow(customer, application, table, rowId);
//		System.out.println("R1=" + r1);
//
//		// all products with a price of 100
//		MappedData filter = new MappedData();
//		filter.put("price", "100");
//		ListData d4 = db.getRowsWithFilter(customer, application, table, filter);
//		System.out.println("T4=" + d4);
//
//		// delete the added product
//		// db.deleteRow(table, rowId);
//
//		// back to an empy table
//		ListData d5 = db.getAllRows(customer, application, table);
//		System.out.println("T5=" + d5);
//
//		// ListData d6 = db.getRowsWithFilter(table, filter);
//		// System.out.println("T6=" + d5);
//
//		// clean up the test table
//		// db.deleteTable("test");
//
//		System.out.println("Done.");
//	}

	// private static Map<String, AttributeValue> newItem(String name, int year,
	// String rating, String... fans) {
	// Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
	// item.put("name", new AttributeValue(name));
	// item.put("year", new AttributeValue().withN(Integer.toString(year)));
	// item.put("rating", new AttributeValue(rating));
	// item.put("fans", new AttributeValue().withSS(fans));
	//
	// return item;
	// }

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData table, IdData rowId) {
		Select select = prepareStatement(table);
		
		makeStatementMultitennant(select, customer, application, table, rowId);
		
		ResultSet results = executeStatement(select);

		ListData resultData = convertResult(results);
		if (resultData.size() < 1) {
			return null;
		} else {
			MappedData result1 = (MappedData) resultData.get(0);
			return result1;
		}
	}

	@Override
	public ListData getAllRows(IdData customer, IdData application, IdData table) {
		Select selectAll = prepareStatement(table);

		makeStatementMultitennant(selectAll, customer, application, table);

		ResultSet results = executeStatement(selectAll);

		ListData resultData = convertResult(results);

		return resultData;
	}

	@Override
	public ListData getRowsWithFilter(IdData customer, IdData application, IdData table, MappedData filter) {
		
		Select select = prepareStatement(table);
		
		makeStatementMultitennant(select, customer, application, table);

		
		// if filter not empty
		if (filter.size() > 0) {
			// build filter conditions
			for (ScalarData keyData : filter.getMap().keySet()) {
				String key = keyData.toRawString();
				String value = String.valueOf(filter.get(keyData));
				
//				if (key.equals("table")) {
//					select.where(QueryBuilder.eq("tablename", value));
//				}
				
				if (SPECIAL_COLUMNS.contains(key)) {

					// TODO: are < & > necessary on special columns???
					if (value.startsWith("<")) {
						value = StringHelper.removePrefix("<", value);
						select.where(QueryBuilder.lt(key, value));
					} else if (value.startsWith(">")) {
						value = StringHelper.removePrefix(">", value);
						select.where(QueryBuilder.gt(key, value));
					} else {
						// TODO implement
						//select.where(QueryBuilder.eq(key, value));
					}
				} else {
					//select.where(QueryBuilder.)
					// TODO filter this on mid tier
					// https://datastax-oss.atlassian.net/browse/JAVA-110
					//select.where(QueryBuilder.in("data", value));
				}
			}
		}
		
		//select.allowFiltering(); // TODO remove later
		
		ResultSet results = executeStatement(select);

		ListData resultData = convertResult(results);
		
		List<Data> toRemove = new ArrayList<Data>();
		for (int i=0; i<resultData.size(); i++) {
			MappedData currentResult = (MappedData) resultData.get(i);
			
			for (ScalarData filterKeyData : filter.getMap().keySet()) {
				String filterKey = filterKeyData.toRawString();
				Data filterValueData = filter.get(filterKeyData);
				String filterValue = String.valueOf(filterValueData);
				
				Data currentResultValueData = currentResult.get(filterKeyData);
				String currentResultValue = String.valueOf(currentResultValueData);
				
				if (currentResultValueData == null || !currentResultValue.equals(filterValue)) {
					toRemove.add(currentResult);
				}
			}
		}
		
		for (Data itemToRemove : toRemove) {
			resultData.remove(itemToRemove);
		}
		
		// add "ALLOW FILTERING ;" to filtered CQL queries
		// http://docs.datastax.com/en/cql/3.1/cql/ddl/ddl_using_multiple_indexes.html
		
		return resultData;
	}


	@Override
	public BooleanData crupdateRow(IdData customer, IdData application, IdData table, IdData rowId, MappedData data) {
		Update update = QueryBuilder.update(KEYSPACE, table.toRawString());
		
		for (ScalarData keyData : data.getMap().keySet()) {
			String key = keyData.toRawString().replaceAll("_", ""); // TODO this replace all is dangerous

			if (SPECIAL_COLUMNS.contains(key)) {
				String value = String.valueOf(data.get(keyData));
				//update.with(QueryBuilder.set(key, value));
				data.remove((StringData)keyData);
			}
		}
		update.with(QueryBuilder.putAll("data", data.getMapAsPojo()));
		
		//update.with(QueryBuilder.put("tablename", tablename));
	
		makeStatementMultitennant(update, customer, application, table, rowId);

		
		ResultSet results = executeStatement(update);
		
		// TODO fix this
		return BooleanData.create(results != null);
		
	}
		
	@Override
	public BooleanData deleteRow(IdData customer, IdData application, IdData table, IdData rowId) {
		Delete delete = QueryBuilder.delete().from(table.toRawString());
		
		makeStatementMultitennant(delete, customer, application, table, rowId);
		
		ResultSet results = executeStatement(delete);
		
		// TODO fix this
		return BooleanData.create(results != null);
	}


	@Override
	public BooleanData deleteRows(IdData customer, IdData application, IdData table, MappedData filter) {
		ListData rowsWithFilter = getRowsWithFilter(customer, application, table, filter);
		List<MappedData> list = rowsWithFilter.toList();
		for (MappedData item : list) {
			IdData rowId = new IdData(item.get("id").toString());
			if (BooleanData.FALSE.equals(deleteRow(customer, application, table, rowId)))
				return BooleanData.FALSE;
		}
		return BooleanData.TRUE;
	}

	// ///////////// HELPER METHODS ///////////////

	private Select prepareStatement(IdData table) {
		String tableName = table.toRawString();
		Select select = QueryBuilder.select().all().from(KEYSPACE, tableName);
		// TODO To disable paging of the results, set the fetch size to Integer.MAX_VALUE. If the fetch size is set to a value less than or equal to 0, the default fetch size will be used.
		return select;
	}
	
	private void makeStatementMultitennant(Select statement, IdData customer, IdData application, IdData tablename) {
		statement.where(QueryBuilder.eq("customer", customer.toRawString()));
		statement.where(QueryBuilder.eq("application", application.toRawString()));
		statement.where(QueryBuilder.eq("tablename", tablename.toRawString()));
	}
	
	private void makeStatementMultitennant(Select statement, IdData customer, IdData application, IdData tablename, IdData rowId) {
		statement.where(QueryBuilder.eq("customer", customer.toRawString()));
		statement.where(QueryBuilder.eq("application", application.toRawString()));
		statement.where(QueryBuilder.eq("tablename", tablename.toRawString()));
		statement.where(QueryBuilder.eq("rowId", rowId.toRawString()));
	}
	
	private void makeStatementMultitennant(Delete statement, IdData customer, IdData application, IdData tablename, IdData rowId) {
		statement.where(QueryBuilder.eq("customer", customer.toRawString()));
		statement.where(QueryBuilder.eq("application", application.toRawString()));
		statement.where(QueryBuilder.eq("tablename", tablename.toRawString()));
		statement.where(QueryBuilder.eq("rowId", rowId.toRawString()));
	}
	
	private void makeStatementMultitennant(Update statement, IdData customer, IdData application, IdData tablename, IdData rowId) {
		statement.where(QueryBuilder.eq("customer", customer.toRawString()));
		statement.where(QueryBuilder.eq("application", application.toRawString()));
		statement.where(QueryBuilder.eq("tablename", tablename.toRawString()));
		statement.where(QueryBuilder.eq("rowId", rowId.toRawString()));
	}

	private ResultSet executeStatement(Statement statement) {
		int retries = 0;
        while (retries < 3) {
            try {
                ResultSet results = session.execute(statement);
                return results;
            } catch (NoHostAvailableException e) {
                System.out.println("NoHostAvailableException Could not connect to DB");
                e.printStackTrace();
                doConnectionStuff();
                ++retries;
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } 
        return null; 
	}

	private ListData convertResult(ResultSet results) {
		ListData resultData = new ListData();
		if (results == null) {
			System.out.println("ResultSet null");
			return resultData;
		}
		for (Row row : results) {
			Map<String,String> data = row.getMap("data", String.class, String.class);

			MappedData map = new MappedData(data);

			String id = row.getString("rowId");
			map.put("id", id);

			long updated = row.getLong("updated");
			map.put("_updated", String.valueOf(updated));
			
			
			System.out.format("%s %d \n", id, updated);

			// row.getList("data", null);

			resultData.add(map);
		}

		return resultData;
	}
}
