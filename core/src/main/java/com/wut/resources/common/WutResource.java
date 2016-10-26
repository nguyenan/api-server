package com.wut.resources.common;

import java.util.Collection;
import java.util.List;

import com.wut.cache.Cache;

public interface WutResource {
	public Collection<WutOperation> getOperations();
	public String getName();
	
	public List<String> getExamples(); // externalize this!!!
	public String getHelp(); // externalize this in xml form!!!
	public boolean initialize();
	//public String getGroup();
	//public Collection<Resource> getSubResources();
	//public String getPath();
	//public String getGroup();
	public String getRevision(); // externalize!!!
	
	//public List<Cache> getCaches();
	
}
