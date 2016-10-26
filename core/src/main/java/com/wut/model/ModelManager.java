package com.wut.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.matrix.MatrixData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.EmailData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StateData;
import com.wut.model.scalar.StringData;
import com.wut.model.scalar.TimeData;
import com.wut.model.scalar.UrlData;
import com.wut.model.scalar.ZipCodeData;

public class ModelManager {
	private Map<String, Class<? extends Data>> map = new HashMap<String, Class<? extends Data>>();
	
	public ModelManager() {
		map.put("boolean", BooleanData.class);
		map.put("date", DateData.class);
		map.put("email", EmailData.class);
		map.put("integer", IntegerData.class);
		map.put("string", StringData.class);
		map.put("time", TimeData.class);
		map.put("url", UrlData.class);
		map.put("list", ListData.class);
		map.put("map", MappedData.class);
		map.put("matrix", MatrixData.class);
		map.put("zipcode", ZipCodeData.class);
		map.put("state", StateData.class);
		// TODO add phone
	}
	
	public Class<? extends Data> getType(String name) {
		return map.get(name);
	}
	
	public Set<String> getTypes() {
		return map.keySet();
	}

}
