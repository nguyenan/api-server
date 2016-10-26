package com.wut.pipeline;

//import com.wut.datasources.cache.SimpleMemoryCache;
////import com.wut.datasources.cache.WutEhCache;
//import com.wut.model.Data;

// TODO this class is not longer used!!!
public class CacheStorageProcessor /*implements Processor*/ {
//	private SimpleMemoryCache cache = new SimpleMemoryCache();
//
//	@Override
//	public boolean process(WutRequest request, WutResponse response) {
//		String requestId = request.getRequestIdentifier();
//		// TODO remove this hardcoded strings from throughout the actions in the system
//		if (request.getAction().equalsIgnoreCase("read")) {
//			Data dataToSave = response.getData();
//			cache.put(requestId, dataToSave);
//			System.out.println("Data saved to cache");
//		} else if (request.getAction().equalsIgnoreCase("create") || request.getAction().equalsIgnoreCase("update") || request.getAction().equalsIgnoreCase("delete")) {
//			cache.put(requestId, null);
//			
//			// TODO fix this hack
//			if (request.getResource().equals("table")) {
//				String requestIdNoParams = request.getRequestIdentifierNoParameters();
//				cache.put(requestIdNoParams, null);
//			}
//		}
//		
//		return true;
//	}
}
