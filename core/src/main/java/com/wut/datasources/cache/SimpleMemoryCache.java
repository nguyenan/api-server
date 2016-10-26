package com.wut.datasources.cache;

import java.util.HashMap;
import java.util.Map;

import com.wut.datasources.keyvalue.KeyValueStore;
import com.wut.model.Data;

public class SimpleMemoryCache implements KeyValueStore {
	private static Map<String, Data> internalCache = new HashMap<String, Data>();
	
	@Override
	public boolean put(String key, Data data) {
		if (internalCache.size() > 1000) {
			internalCache.clear();
		}
		internalCache.put(key, data);
		return true;
	}

	@Override
	public Data get(String key) {
		return internalCache.get(key);
	}

	@Override
	public boolean remove(String key) {
		internalCache.remove(key);
		return true;
	}
	
	public static int getSize() {
		return internalCache.size();
	}

}
