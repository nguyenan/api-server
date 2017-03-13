package com.wut.datasources.cassandra;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.wut.datasources.TableRowSource;
import com.wut.datasources.jdbc.ResultSetListHelper;
import com.wut.datasources.jdbc.SqlStatementHelper;
import com.wut.format.Decoder;
import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
import com.wut.format.json.JsonDecoder;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.support.ErrorHandler;
import com.wut.support.logging.WutLogger;


/*
public class Cassandra implements TableRowSource {
	public static final String database = "mykeyspace";
	private Cluster cluster;
	private Session session;
	
	public Cassandra() {
        cluster = Cluster.builder().addContactPoint("db1.webutilitykit.com").build();
        session = cluster.connect(database);
	}
	
	private boolean createTable(String name, boolean dropIfExists) throws SQLException {
		// TODO implement
		
//		 CREATE TABLE users (
//		  user_name varchar PRIMARY KEY,
//		  password varchar,
//		  gender varchar,
//		  session_token varchar,
//		  state varchar,
//		  birth_year bigint
//		); 
		
		return false;
	}


	public void shutdown() {
        cluster.close();
	}

	@Override
	public List<Map<String, String>> get(String tableId) {
		List<Map<String, String>> listOfResults = new ArrayList<Map<String,String>>();
		
		try {
			String statementStr = SqlStatementHelper.select(tableId);
			
			ResultSet results = executeQuery(statementStr);
			
			while (results.next()) {
				//results.getString("_updated");
				String table = results.getString("tablename");
				
				String data = results.getString("data");
				MappedData mappedData = expandDataIntoMap(data);
				Map<String, String> map = mappedData.getMapAsPojo();
				
				map.put("_table", String.valueOf(table));

				int updated = results.getInt("updated");
				map.put("_updated", String.valueOf(updated));
				
				int created = results.getInt("created");
				map.put("_created", String.valueOf(created));
				
				String customer = results.getString("customer");
				map.put("_customer", String.valueOf(customer));
				
				String creator = results.getString("creator");
				map.put("_creator", String.valueOf(creator));
				
				listOfResults.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listOfResults;
	}
	
	public MappedData expandDataIntoMap(String data) {
		try {
			JsonDecoder decoder = new JsonDecoder();
			MappedData map = (MappedData) decoder.decode(data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new MappedData();
	}
	
	@Override
	public List<Map<String, String>> filter(String tableId,
			Map<String, String> filter) {
		
		try {
			String statementStr = SqlStatementHelper.select(tableId, filter);
			ResultSet results = executeQuery(statementStr);
			while (results.next()) {
				int updated = results.getInt("_updated");
				int created = results.getInt("_created");
				String owner = results.getString("_owner");
				String data = results.getString("_data");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<String, String> get(String tableId, String rowId) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("id", rowId);
		
		String readSql = SqlStatementHelper.select(tableId, conditions);

        com.datastax.driver.core.ResultSet results = session.execute(readSql);
        for (Row row : results) {
        	System.out.format("%s %d\n", row.getString("fname"), 4 ); //  row.getInt("age")
        }
        
		try {
			List<Map<String,String>> results = ResultSetListHelper.getList(resultSet);
			if (results != null && results.size() < 1) { // < 1 == 0
				return null;
			} else if (results != null) {
				Map<String,String> result = results.get(0);
				String dataResultString = result.get("DATA");
				Map<String,String> dataMap = parseFromString(dataResultString);
				//String resultMapStr = dataMap.get("result");
				//Map<String,String> resultMap = parseFromString(resultMapStr);
				return dataMap;
			}
		} catch (SQLException e) {
			// eat it
		}
		
		return null;
	}

	@Override
	public boolean remove(String tableId, String rowId) {
		
		String deleteSql = SqlStatementHelper.remove(tableId, rowId);
		boolean resultSuccessful = executeDML(deleteSql);
		
		return resultSuccessful;
	}

	@Override
	public boolean update(String tableId, String rowId, Map<String, String> data) {
		data.put("id", rowId); // TODO hacky and weird but needed so SqlHelper generates correct sql
		Map<String, String> statementData = convertMapToString(data);
		//statementData.put("id", rowId); 
		String updateSql = SqlStatementHelper.update(tableId, statementData);
		boolean resultSuccessful = executeDML(updateSql);
		
		return resultSuccessful;
	}

	@Override
	public boolean crupdate(String tableId, String rowId, Map<String, String> data) {
		boolean updated = update(tableId, rowId, data);
		if (!updated) {
			//String insertSql = SqlStatementHelper.insert(tableId, rowId, data);
			data.put("id", rowId);
			String id = insert(tableId, data);
			return (id != null);
		}

		return updated;
	}
	
	@Override
	public String insert(String tableId, Map<String, String> data) {
		Map<String, String> statementData = new HashMap<String, String>();

		convertMapElement("id", data, "id", statementData);
		convertMapElement("_table", data, "tablename", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		convertMapElement("_created", data, "created", statementData);
		convertMapElement("_updator", data, "updator", statementData);
		convertMapElement("_customer", data, "customer", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		
		String dataAsString = condenseToString(data);
		statementData.put("data", dataAsString);

		String insertSql = SqlStatementHelper.insert(tableId, statementData);
		
        com.datastax.driver.core.ResultSet rs = session.execute(insertSql);
		
		boolean resultSuccessful = session.execute(insertSql);
		
		if (resultSuccessful) {
			return data.get("id");
		} else {
			return null;
		}
	}
	
	private Map<String, String> convertMapToString(Map<String, String> data) {
		Map<String, String> statementData = new HashMap<String, String>();

		convertMapElement("id", data, "id", statementData);
		convertMapElement("_table", data, "tablename", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		convertMapElement("_created", data, "created", statementData);
		convertMapElement("_updator", data, "updator", statementData);
		convertMapElement("_customer", data, "customer", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		
		String dataAsString = condenseToString(data);
		statementData.put("data", dataAsString);

		return statementData;
	}

}
*/