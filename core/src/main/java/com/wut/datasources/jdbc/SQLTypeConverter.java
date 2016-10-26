package com.wut.datasources.jdbc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.scalar.TimeData;

@SuppressWarnings("rawtypes")
public class SQLTypeConverter {
	private static Map<String, Integer> typeMap = new HashMap<String, Integer>();
	private static Map<Integer, String> nameMap = new HashMap<Integer, String>();

	static {
		Class c = java.sql.Types.class;
		Field[] publicFields = c.getFields();
		for (int i = 0; i < publicFields.length; i++) {
			String fieldName = publicFields[i].getName();
			try {
				Integer val = (Integer) publicFields[i].get(null);
				typeMap.put(fieldName, val);
				nameMap.put(val, fieldName);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getName(int type) {
		return nameMap.get(type);
	}

	public static int getType(String name) {
		return typeMap.get(name);
	}
	
	
	public static Class<? extends ScalarData> getPropertyType(int type) {
		Map<String, Class<? extends ScalarData>> map = new HashMap<String, Class<? extends ScalarData>>();
		map.put("INTEGER", IntegerData.class);
		map.put("BINARY", IntegerData.class);
		map.put("BIGINT", IntegerData.class);
		map.put("TINYINT", IntegerData.class);
		map.put("SMALLINT", IntegerData.class);
		map.put("DECIMAL", IntegerData.class);
		map.put("BIT", IntegerData.class);
		map.put("BLOB", StringData.class);
		map.put("REF", IntegerData.class);
		map.put("TIME", TimeData.class);
		map.put("BOOLEAN", BooleanData.class);
		map.put("DATE", DateData.class);
		map.put("NULL", StringData.class); // TODO null?
		map.put("NUMERIC", IntegerData.class);
		map.put("REAL", IntegerData.class);
		map.put("VARCHAR", StringData.class);
		
		String name = getName(type);
		Class<? extends ScalarData> propType = map.get(name);
		return propType;
	}
	
//	
//	public String fromTypeToSQLType() {
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("boolean", ");
//		map.put("date", DateData.class);
//		map.put("email", EmailData.class);
//		map.put("integer", IntegerData.class);
//		map.put("string", StringData.class);
//		map.put("time", TimeData.class);
//		map.put("url", UrlData.class);
//		map.put("list", ListData.class);
//		map.put("map", MappedData.class);
//		map.put("matrix", MatrixData.class);
//		
//	}
}
