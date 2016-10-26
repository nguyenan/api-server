package com.wut.datasources.cache;
/*
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.TerracottaConfiguration;

import com.wut.datasources.keyvalue.KeyValueStore;
import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.support.ErrorHandler;

public class WutEhCache implements KeyValueStore {
    private static final String CACHE_NAME = "myCache";

	//private static CacheManager manager;// = new CacheManager();
	private static Cache internalCache;// = new Cache("session_cache", 5000, false, false, 5, 2);
	// TODO change timetolive and timetillidle to zero!!! since they shouldn't
	// matter
	// since eternal mode is on
	//private static Cache diskCache; // = new Cache("wutcache", 500, true, true,10000, 1000);

	public WutEhCache() {
		
		try {
//			Configuration configuration = new Configuration();
//			CacheConfiguration defaultCache = new CacheConfiguration("default", 1000).eternal(false);
//			configuration.addDefaultCache(defaultCache);
//			
//			CacheManager mgr = new CacheManager(configuration);
//	        internalCache = mgr.getCache("default");
//	        assert (internalCache != null);
	        
	        
	        Configuration configuration = new Configuration()
	        //.terracotta(new TerracottaClientConfiguration().url("localhost:9510"))
	        .defaultCache(new CacheConfiguration("defaultCache", 100))
	        .cache(new CacheConfiguration("example", 100)
	        .timeToIdleSeconds(5)
	        .timeToLiveSeconds(120)
	        .terracotta(new TerracottaConfiguration()));
	        CacheManager manager = new CacheManager(configuration);
	        
	        internalCache = manager.getCache("defaultCache");
			
//			CacheConfiguration config1 = new CacheConfiguration("test2", 1000).eternal(true).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO);
//
//			net.sf.ehcache.config.Configuration cacheManagerConfig = new Configuration();
//			cacheManagerConfig.setName("wutCacheConfig");
//			
//			CacheManager singletonManager = CacheManager.create(cacheManagerConfig);
//			

			//Cache cache = singletonManager.getCache("wutCache");
			
//			CacheConfiguration config = cache.getCacheConfiguration();
//			config.setTimeToIdleSeconds(60);
//			config.setTimeToLiveSeconds(120);
			
//			CacheConfiguration config = new CacheConfiguration("test2", 1000).eternal(true).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO);
//			config.setMsetMaxEntriesLocalHeap(10000);
//			config.setMaxEntriesLocalDisk(1000000);
			
			//diskCache = new Cache(config);
		
			//Cache memoryOnlyCache = new Cache("wutCache", 5000, false, false, 5000, 2000);
//			Cache memoryOnlyCache = new Cache(config);
//			singletonManager.addCache(memoryOnlyCache);
//			
//			internalCache = singletonManager.getCache("wutCache");
			
		} catch (Exception e) {
			ErrorHandler.systemError("error loading wutehcache", e);
		}
		
		//diskCache = manager.getCache("wutcache");
		//manager.addCache(diskCache);
		
	}
	
	public void shutdown() {
		//manager.shutdown();
		CacheManager.getInstance().shutdown();
	}
	
	@Override
	public boolean put(String key, Data data) {
		Element element = new Element(new StringData(key), data);
		try {
			internalCache.put(element);
			return true;
		} catch (Exception e) {
			ErrorHandler.dataLossError("Error saving '" + key + "'.", e);
			return false;
		}
	}
	
	@Override
	public Data get(String key) {
		//List keys = internalCache.getKeys(); // TODO testing only
		Element element = internalCache.get(new StringData(key));
		if (element != null) {
			Data value = (Data) element.getObjectValue();
			return value;
		}
		return null;
	}
	
	@Override
	public boolean remove(String key) {
		Element element = new Element(key, "");
		try {
			internalCache.remove(element);
			return true;
		} catch (Exception e) {
			ErrorHandler.dataLossError("Error deleting '" + key + "'", e);
			return false;
		}
	}

}
*/