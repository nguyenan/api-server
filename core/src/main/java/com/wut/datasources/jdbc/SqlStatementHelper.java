package com.wut.datasources.jdbc;

import java.util.Map;

public class SqlStatementHelper {
	
	public static String select(String table, Map<String, String> where) {
		StringBuilder statement = new StringBuilder();
		String selectTable = select(table);
		statement.append(selectTable);
		statement.append(" WHERE ");
		
		int lastKey = where.keySet().size() - 1;
		int currentKey = 0;
		
		for (String key : where.keySet()) {
			statement.append(key);
			statement.append( " = ");
			String value = where.get(key);
			statement.append("'" + value + "'");
			if (currentKey != lastKey) {
				statement.append(" AND ");
			}
			currentKey++;
		}
		return statement.toString();
	}
	
	public static String select(String table) {
		StringBuilder statement = new StringBuilder();
		statement.append("SELECT * FROM ");
		statement.append(table);
		return statement.toString();
	}

	
	// TODO map needs to return keys consistently for this to work right
	// TODO that needs to be fixed!!! can't depend on order of keys
	public static String insert(String table, Map<String, String> data) {
		StringBuilder statement = new StringBuilder();
		statement.append("INSERT INTO ");
		statement.append(table);
		
		int lastKey = data.keySet().size() - 1;
		int currentKey = 0;
		
		statement.append(" (");
		for (String key : data.keySet()) {
			statement.append(key);
			if (currentKey != lastKey) {
				statement.append(", ");
			}
			currentKey++;
		}
		statement.append(") ");

		statement.append(" VALUES (");

		//int lastKey = data.keySet().size() - 1;
		currentKey = 0;
		
		for (String key : data.keySet()) {
			String value = data.get(key);
			// TODO fix this hack. this should be higher up the stack.
			if (key.equalsIgnoreCase("updated") || key.equalsIgnoreCase("created")) {
				statement.append(value);
			} else {
				statement.append("'");
				statement.append(format(value));
				statement.append("'");
			}
			
			if (currentKey != lastKey) {
				statement.append(", ");
			}
			currentKey++;
		}
		statement.append(")");

		return statement.toString();
	}
	
	
	public static String insert(String table, String rowId,
			Map<String, String> data) {
		data.put("id", rowId);
		return insert(table, data);
	}

	public static String update(String table, Map<String, String> data) {
		// UPDATE table_name
		// SET column1=value1,column2=value2,...
		
		StringBuilder statement = new StringBuilder();
		statement.append("UPDATE ");
		statement.append(table);
		statement.append(" SET ");
		
		int lastKey = data.keySet().size() - 1;
		int currentKey = 0;
		for (String key : data.keySet()) {
			statement.append(key);
			statement.append("=");
			statement.append("'");
			String value = format(data.get(key));
			statement.append(value);
			statement.append("'");

			if (currentKey != lastKey) {
				statement.append(", ");
			}
			currentKey++;
		}
		
		return statement.toString();
	}

	/*
	 * DELETE FROM table_name
	   WHERE some_column=some_value;
	 */
	public static String remove(String table, String rowId) {
		StringBuilder statement = new StringBuilder();
		
		statement.append("DELETE FROM ");
		statement.append(table);
		statement.append(" WHERE ");
		statement.append(" id=");
		statement.append("'");
		statement.append(rowId);
		statement.append("'");
		
		return statement.toString();
	}
	
	/////////// HELPERS /////////
	
	// TODO needed????
	public static String format(Object data) {
		return String.valueOf(data);
	}

	public static Map<String,String> parse(String data) {
		
		return null;
	}


}
