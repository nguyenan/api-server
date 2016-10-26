package com.wut.cache;

import com.wut.datasources.cache.SimpleMemoryCache;
import com.wut.model.Data;
import com.wut.pipeline.AbstractProcessor;
import com.wut.pipeline.WutRequest;
import com.wut.pipeline.WutResponse;

public class SimpleCacheProcessor extends AbstractProcessor {
	private SimpleMemoryCache cache = new SimpleMemoryCache();
	private boolean isReadFromCache;

	public SimpleCacheProcessor(boolean isReadFromCache) {
		this.isReadFromCache = isReadFromCache;
	}
	
	@Override
	public boolean process(WutRequest request, WutResponse response) {
		// TODO fix cache so it's not a hacky bunch of bullshit
		String resource = request.getResource();
		String requestIdNoRow = resource + request.getScoppedID();
		String requestId = resource + request.getScoppedID();
		if (resource.equals("table")) {
			Data row = request.getOptionalNonStringParameter("row");
			if (row != null) {
				requestId += ":" + row;
			}
		} else if (resource.equals("user")) {
			Data username = request.getOptionalNonStringParameter("username");
			if (username != null) {
				requestId += ":" + username;
			}
		} else if (resource.equals("payment")) {
			Data start = request.getOptionalNonStringParameter("start");
			if (start != null) {
				requestId += ":" + start;
			}
			Data end = request.getOptionalNonStringParameter("end");
			if (end != null) {
				requestId += ":" + end;
			}
		}
		boolean hasFilter = request.getOptionalNonStringParameter("filter") != null;
		boolean actionIsRead = request.getAction().equalsIgnoreCase("read");
		
		String noCacheSetting = request.getSetting("noCache");
		boolean noCache = noCacheSetting != null && noCacheSetting.equals("true");

		if (isReadFromCache && !response.isFinalized() && actionIsRead && !hasFilter && !noCache) {
			Data savedResponse = cache.get(requestId);
			if (savedResponse != null) {
				System.out.println("Data from cache!!!");
				response.setData(savedResponse);
				return true;
			}
		} else if (!isReadFromCache && !hasFilter) {
			// TODO remove this hardcoded strings from throughout the actions in the system
			if (actionIsRead) {
				Data dataToSave = response.getData();
				cache.put(requestId, dataToSave);
				System.out.println("Data saved to cache");
			} else if (request.getAction().equalsIgnoreCase("create") || request.getAction().equalsIgnoreCase("update") || request.getAction().equalsIgnoreCase("delete")) {
				cache.put(requestId, null);
				
				// TODO fix this hack
				if (request.getResource().equals("table")) {
					cache.put(requestIdNoRow, null);
				}
			}
		}
		
		return true;
	}

}
