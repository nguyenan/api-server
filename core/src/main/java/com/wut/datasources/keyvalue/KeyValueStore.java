package com.wut.datasources.keyvalue;

import com.wut.model.Data;

public interface KeyValueStore {
	/** returns true if successful */
	public boolean put(String key, Data data);

	/** returns data if successful, null otherwise */
	public Data get(String key);

	/** returns true if successful */
	public boolean remove(String key);
}
