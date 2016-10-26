package com.wut.datasources.tablestore.bigtable;

import java.util.List;
import java.util.Map;

import com.wut.datasources.keyvalue.KeyValueStore;
import com.wut.datasources.tablestore.RelationalStore;
import com.wut.model.Data;
//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.EntityNotFoundException;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
//import com.google.appengine.api.datastore.Query;

public class BigTable implements RelationalStore, KeyValueStore {


	@Override
	public String insertRow(String table, Map<String, String> row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteRow(String table, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRow(String table, String id, Map<String, String> row) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getRow(String table, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getAllRows(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createTable(String table, Map<String, Class<Data>> schema) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteTable(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTable(String table, Map<String, Class<Data>> schema) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Class<Data>> getTableSchema(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTables() {
		// TODO Auto-generated method stub
		return null;
	}
//	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//	
//	public void test() {	
//		{
//			Entity alice = new Entity("Person", "Alice");
//			alice.setProperty("gender", "female");
//			alice.setProperty("age", 20);
//	
//			Key bobKey = KeyFactory.createKey("Person", "Bob");
//			Entity bob = new Entity(bobKey);
//			bob.setProperty("gender", "male");
//			bob.setProperty("age", 23);
//	
//			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//			datastore.put(alice);
//			datastore.put(bob);
//		}
//		
//		
//		{
//			Key bobKey = KeyFactory.createKey("Person", "Bob");
//			Key aliceKey = KeyFactory.createKey("Person", "Alice");
//
//			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//			Entity alice, bob;
//
//			try {
//			    alice = datastore.get(aliceKey);
//			    bob = datastore.get(bobKey);
//
//			    Long aliceAge = (Long) alice.getProperty("age");
//			    Long bobAge = (Long) bob.getProperty("age");
//			    System.out.println("Alices age: " + aliceAge);
//			    System.out.println("Bobs age: " + bobAge);
//			} catch (EntityNotFoundException e) {
//			    // Alice or Bob doesn't exist!
//			}
//		}
//	}
//
//	@Override
//	public boolean createTable(String table, Map<String, Class<Data>> schema) {
//		ErrorHandler
//				.systemError("uanble to create big table entities at runtime");
//		return false;
//	}
//
//	private Key getKey(String type, String id) {
//		return KeyFactory.createKey(null, type, id);
//	}
//
//	private Entity getEntityByKey(String entityType, String key) {
//		Entity e;
//		try {
//			e = datastore.get(getKey(entityType, key));
//			return e;
//		} catch (EntityNotFoundException e1) {
//			//ErrorHandler.userError("invalid key provided");
//			// just return null if 
//		}
//		return null;
//	}
//
//	@Override
//	public boolean deleteRow(String type, String id) {
//		System.out.println("deleting " + type + " with id " + id);
//		Key k = getKey(type, id);
//		try {
//			Entity e = datastore.get(k);
//			Map<String, Object> properties = e.getProperties();
//			datastore.delete(k);
//			//datastore.delete(new Entity[] {e});
//		} catch (EntityNotFoundException e) {
//			e.printStackTrace();
//		}
//		//datastore.
//		//datastore.getCurrentTransaction().commit();
//		return true;
//	}
//
//	@Override
//	public boolean deleteTable(String table) {
//		// just removes all the data from the table
//		Query query = new Query(table);
//		
//		for (Entity e : datastore.prepare(query).asIterable()) {
//			datastore.delete(e.getKey());
//		}
//		
//		return true;
//	}
//
//	@Override
//	public List<Map<String, String>> getAllRows(String table) {
//		Query query = new Query(table);
//		//query.addFilter("dueDate", Query.FilterOperator.LESS_THAN, today);
//		
//		List<Map<String, String>> rowList = new ArrayList<Map<String,String>>();
//		
//		for (Entity e : datastore.prepare(query).asIterable()) {
//			Map<String, Object> retreivedProperties = e.getProperties();
//			Map<String, String> returnedRow = new HashMap<String, String>();
//			String key = String.valueOf(e.getKey().getName());
//			//String keyString = e.getKey().getParent().getName();
//			//System.out.println("listing " + table + " with id " + keyString + " and parent " + e.getKey().getParent().getName());
//			returnedRow.put("_key_", key);
//			for (String property : retreivedProperties.keySet()) {
//				String value = retreivedProperties.get(property).toString();
//				returnedRow.put(property, value);
//			}
//			rowList.add(returnedRow);
//		}
//		
//		return rowList;
//	}
//
//	@Override
//	public List<String> getAllTables() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Map<String, String> getRow(String type, String id) {
//		 Entity e = getEntityByKey(type, id);
//		 if (e == null) {
//			 return null;
//		 }
//		 Map<String, String> row = new HashMap<String, String>();
//		 Map<String, Object> properties = e.getProperties();
//		 for (String propKey : properties.keySet()) {
//			 String value = String.valueOf(properties.get(propKey));
//			 row.put(propKey, value);
//		 }
//		 return row;
//	}
//
//	@Override
//	public Map<String, Class<Data>> getTableSchema(String table) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String insertRow(String type, Map<String, String> properties) {
//		String newId = UniqueIdGenerator.getId();
//		System.out.println("inserting " + type + " with id " + newId);
//
//		Key key = getKey(type, newId);
//		
//		Entity newEntity = new Entity(key);
//		for (String propertyName : properties.keySet()) {
//			newEntity.setProperty(propertyName, properties.get(propertyName));
//		}
//		
//		datastore.put(newEntity);
//		return newId;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.wut.datasources.tablestore.RelationalStore#updateRow(java.lang.String, java.lang.String, java.util.Map)
//	 */
//	@Override
//	/**
//	 * Updates a row or creates the the if not present
//	 */
//	public boolean updateRow(String table, String id, Map<String, String> properties) {
//		Entity entity = getEntityByKey(table, id);
//		if (entity == null) {
//			Key key = getKey(table, id);
//			entity = new Entity(key);
//		}
//		for (String propertyName : properties.keySet()) {
//			entity.setProperty(propertyName, properties.get(propertyName));
//		}
//		datastore.put(entity);
//		return true;
//	}
//
//	@Override
//	public boolean updateTable(String table, Map<String, Class<Data>> schema) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	
//	
//	// THESE METHODS ARE ALL RELATED TO THE KeyValueStore INTERFACE
//	// TODO break the following key value method off into their own class
//	
//	private static String KEYVALUE_TABLE_NAME = "KEYVALUES"; // TODO add underscore
//	private static String VALUE_NAME = "value";
//	
//	@Override
//	public String get(String key) {
//		Entity e = getEntityByKey(KEYVALUE_TABLE_NAME, key);
//		Map<String, Object> properties = e.getProperties();
//		String value = String.valueOf(properties.get(key));
//		return value;
//		//return getRow(KEYVALUE_TABLE_NAME, key).get(VALUE_NAME);
//	}
//
//	@Override
//	public boolean put(String key, String data) {
//		Entity entity = getEntityByKey(KEYVALUE_TABLE_NAME, key);
//		entity.setProperty(VALUE_NAME, data);
//		datastore.put(entity);
//		return true;
//	}
//
//	@Override
//	public boolean remove(String key) {
//		deleteRow(KEYVALUE_TABLE_NAME, key);
//		return true; // TODO return deleteRow()
//	}
//	
//	/// SCALAR ////
//
//	@Override
//	public ScalarData readScalar(String id) {
//		Map<String, String> row = getRow("scalar", id);
//		String strData = row.get("data");
//		StringData data = new StringData(strData);
//		return data;
//	}
//
//	@Override
//	public boolean updateScalar(String id, ScalarData data) {
//		//Map<String, String> row = getRow("scalar", id);
//		Map<String, String> row = new HashMap<String, String>();
//		row.put("data", data.toRawString());
//		boolean success = updateRow("scalar", id, row);
//		//boolean success = put(id, data.toRawString());
//		return success;
//	}

	@Override
	public boolean put(String key, Data data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Data get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return false;
	}

}
