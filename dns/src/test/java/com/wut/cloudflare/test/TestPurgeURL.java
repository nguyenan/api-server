//package com.wut.cloudflare.test;
//
//import static org.junit.Assert.*;
//
//import java.util.Arrays;
//import java.util.Collection;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
//
//import com.wut.datasources.cloudflare.CDNUtils;
//import com.wut.datasources.cloudflare.CFSource;
//import com.wut.model.map.MessageData;
//
//@RunWith(Parameterized.class)
//public class TestPurgeURL {
//
//	private String id;
//	private String customerDomain;
//
//	@Parameters
//	public static Collection<Object[]> configs() {
//		return Arrays.asList(new Object[][] { 
//				// {"index.html", "mapiii.com" }
//				 {"page/about.html", "www.oldhousefarm.net" }
//		});
//	}
//
//	public TestPurgeURL(String id, String customerDomain) {
//		this.id = id;
//		this.customerDomain = customerDomain;
//	}
//
//	@Before
//	public void initObjects() {
//		CFSource cfRequest = new CFSource();
//		cfRequest.resetZoneMap();
//	}
//
//	// PURGE CACHE
//	@Test
//	public void purgeCache() throws InterruptedException {
//		CFSource cfRequest = new CFSource();
//		MessageData purgeCache = cfRequest.purgeCache(customerDomain, id);
//		assertEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
//	}
//
//	@Test
//	public void purgeCacheWrongCredential() {
//		CFSource cfRequest = new CFSource();
//		cfRequest.getZoneID(customerDomain);
//		CFSource cfRequest2 = new CFSource("123456abc", "annguyen.qh@gmail.com");
//		MessageData purgeCache = cfRequest2.purgeCache(customerDomain, id);
//		assertNotEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
//	}
//
//	@Test
//	public void purgeCacheAndValidate() throws InterruptedException, UnsupportedOperationException {
//		CDNUtils.getCFCacheStatus(CDNUtils.buildHttpPurgeURL(customerDomain, id));
//		String statusBefore = CDNUtils.getCFCacheStatus(CDNUtils.buildHttpPurgeURL(customerDomain, id));
//		assertEquals("cache not HIT ", "HIT", statusBefore);
//
//		CFSource cfRequest = new CFSource();
//		MessageData purgeCache = cfRequest.purgeCache(customerDomain, id);
//		assertEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
//		Thread.sleep(3000);
//		String statusAfter = CDNUtils.getCFCacheStatus(CDNUtils.buildHttpPurgeURL(customerDomain, id));
//		assertEquals("validate cache fail " + CDNUtils.buildHttpPurgeURL(customerDomain, id), "MISS", statusAfter);
//	}
//
//	// ZONE ID
//	@Test
//	public void getZoneID() {
//		CFSource cfRequest = new CFSource();
//		String zoneID = cfRequest.getZoneID(customerDomain);
//		assertNotNull(zoneID);
//		assertNotEquals(zoneID, "");
//	}
//
//	@Test
//	public void getZoneIdNotExist() {
//		CFSource cfRequest = new CFSource();
//		String zoneID = cfRequest.getZoneID("map.com");
//		assertNotNull(zoneID);
//		assertEquals(zoneID, "");
//	}
//
//	@Test
//	public void getZoneIdWrongCredential() {
//		CFSource cfRequest = new CFSource("123456", "annguyen.qh@gmail.com");
//		String zoneID = cfRequest.getZoneID(customerDomain);
//		assertNull(zoneID);
//	}
//
//}