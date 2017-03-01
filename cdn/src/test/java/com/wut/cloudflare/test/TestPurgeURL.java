package com.wut.cloudflare.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.wut.datasources.cloudflare.CloudFlareSource;
import com.wut.datasources.cloudflare.CloudFlareUtils;
import com.wut.model.map.MessageData;

@RunWith(Parameterized.class)
public class TestPurgeURL {

	private String id;
	private String customerDomain;

	@Parameters
	public static Collection<Object[]> configs() {
		return Arrays.asList(new Object[][] {
				{ "blog.html", "www.farmer.events" } });
	}

	public TestPurgeURL(String id, String customerDomain) {
		this.id = id;
		this.customerDomain = customerDomain;
	}

	@Before
	public void initObjects() {

	}

	// PURGE CACHE
	@Test
	public void purgeCache() throws InterruptedException {
		CloudFlareSource cfRequest = new CloudFlareSource();
		MessageData purgeCache = cfRequest.purgeCache(customerDomain, id);
		assertEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
	}

	@Test
	public void purgeCacheWrongCredential() {
		CloudFlareSource cfRequest = new CloudFlareSource();
		cfRequest.getZoneId(customerDomain);
		CloudFlareSource cfRequest2 = new CloudFlareSource("123456", "test@tend.ag");
		MessageData purgeCache = cfRequest2.purgeCache(customerDomain, id);
		assertNotEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
	}

	@Test
	public void purgeCacheAndValidate() throws InterruptedException, UnsupportedOperationException {
		CloudFlareUtils.getCacheStatus(CloudFlareUtils.buildHttpsPurgeURL(customerDomain, id));
		String statusBefore = CloudFlareUtils.getCacheStatus(CloudFlareUtils.buildHttpsPurgeURL(customerDomain, id));
		assertEquals("cache not HIT ", "HIT", statusBefore);

		CloudFlareSource cfRequest = new CloudFlareSource();
		MessageData purgeCache = cfRequest.purgeCache(customerDomain, id);
		assertEquals("purge cache fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
		Thread.sleep(3000);
		String statusAfter = CloudFlareUtils.getCacheStatus(CloudFlareUtils.buildHttpsPurgeURL(customerDomain, id));
		assertEquals("validate cache fail " + CloudFlareUtils.buildPurgeURL(customerDomain, id), "MISS", statusAfter);
	}

	// ZONE ID
	@Test
	public void getZoneId() {
		CloudFlareSource cfRequest = new CloudFlareSource();
		String zoneID = cfRequest.getZoneId(customerDomain);
		assertNotNull(zoneID);
		assertNotEquals(zoneID, "");
	}

	@Test
	public void getZoneIdNotExist() {
		CloudFlareSource cfRequest = new CloudFlareSource();
		String zoneID = cfRequest.getZoneId("example.com");
		assertNull(zoneID);
	}

	@Test
	public void getZoneIdWrongCredential() {
		CloudFlareSource cfRequest = new CloudFlareSource("123456", "test@tend.ag");
		String zoneID = cfRequest.getZoneId(customerDomain);
		assertNull(zoneID);
	}

}