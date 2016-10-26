package com.wut.model.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wut.model.AbstractData;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;

// TODO rename MapData
// TODO convert IdData uses to ScalarData
public class MappedData extends AbstractData {
	private Map<ScalarData, Data> data = new HashMap<ScalarData, Data>();
	
	public MappedData() {

	}
	
	public MappedData(Map<String, String> map) {
		for (String key : map.keySet()) {
			put(key, map.get(key));
		}
	}

	public Map<ScalarData, Data> getMap() {
		// TODO this defensive copy is going to suck for performance
		return new HashMap<ScalarData,Data>(data);
	}
	
	public Map<String, String> getMapAsPojo() {
		Map<String,String> pojoMap = new HashMap<String, String>();
		for (ScalarData key : data.keySet()) {
			Data value = this.data.get(key);
			pojoMap.put(key.toRawString(), String.valueOf(value));
		}
		return pojoMap;
	}

	public void put(String key, String property) {
		if (property != null) {
			data.put(new StringData(key), new StringData(property));
		} else {
			data.put(new StringData(key), null);
		}
	}
	
	public void put(String key, Data property) {
		data.put(new StringData(key), property);
	}
	
	public void put(ScalarData key, Data property) {
		data.put(key, property);
	}

	public Data get(String key) {
		return data.get(new StringData(key));
	}
	
	public Data get(ScalarData key) {
		return data.get(key);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (ScalarData key : data.keySet()) {
			Data value = data.get(key);
			sb.append("\"");
			sb.append(key);
			sb.append("\"");
			sb.append(":");
			sb.append("\"");
			sb.append(String.valueOf(value));
			sb.append("\"");
			sb.append(",");
		}
		if (sb.charAt(sb.length()-1) == ',') {
			sb.setLength(sb.length()-1);
		}
		sb.append("}");
		return sb.toString();
	}

	// TODO call this create()
	// TODO deprecate. use constructor instead. make this use constructor!!!
	public static MappedData convert(Map<String, String> map) {
		if (map == null)
			return null;
			
		MappedData dataMap = new MappedData();
		for (String key : map.keySet()) {
			dataMap.put(key, map.get(key));
		}
		return dataMap;
	}

	/**
	 * Convert to plain old java objects and java util map
	 * @return
	 */
	public Map<String, String> pojoize() {
		return null;
	}

	public void remove(StringData item) {
		data.remove(item);
	}

	public Set<ScalarData> keys() {
		return data.keySet();
	}

	public int size() {
		return data.size();
	}

	public String toUrlParameters() {
		StringBuilder sb = new StringBuilder();
		for (ScalarData key : data.keySet()) {
			Data value = data.get(key);
			sb.append(key);
			sb.append("=");
			//sb.append("\"");
			sb.append(value.toString());
			//sb.append("\"");
			sb.append("&");
		}
		if (sb.length() > 0 && sb.charAt(sb.length()-1) == '&') {
			sb.setLength(sb.length()-1);
		}
		return sb.toString();
	}

	@Override
	public Model<MappedData> getModel() {
		return MapModel.create();
	}

	public <T,K> Map<T, K> toMap() {
		return (Map<T,K>) (this.data);
	}
	
	
}
