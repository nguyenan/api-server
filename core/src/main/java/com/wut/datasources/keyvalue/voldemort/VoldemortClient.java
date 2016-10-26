package com.wut.datasources.keyvalue.voldemort;
/*
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

import com.wut.datasources.keyvalue.KeyValueStore;
import com.wut.model.Data;

public class VoldemortClient implements KeyValueStore {

	String bootstrapUrl = "tcp://localhost:6666";
	StoreClientFactory factory = new SocketStoreClientFactory(
			bootstrapUrl);

	// create a client that executes operations on a single store
	StoreClient client = factory.getStoreClient("wut_store");

	public VoldemortClient() {
		// do some random pointless operations
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean put(String key, Data data) {
		Versioned<Data> value = client.get(key);
		if (value != null) {
			value.setObject(data);
		} else {
			value = new Versioned<Data>(data);			
		}
		client.put(key, value);
		return true;
	}

	@Override
	public Data get(String key) {
		Object o = client.getValue(key);
		return (Data) o;
	}

	public boolean remove(String key) {
		return client.delete(key);
	}
}
*/