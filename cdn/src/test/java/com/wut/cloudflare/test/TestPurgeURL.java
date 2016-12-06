package com.wut.cloudflare.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.map.MessageData;

public class TestPurgeURL {

	@Test
	public void testPurge() {
		String id = "index.html"; // file to purge
		CFSource cfRequest = new CFSource();
		MessageData purgeCache = cfRequest.purgeCache("mapiii.com", id);
		assertEquals("purge file fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
	}

	@Test
	public void testPurgeWrongCredential() {
		CFSource cfRequest = new CFSource();
		cfRequest.getZoneID("mapiii.com");
		String id = "index.html"; // file to purge
		CFSource cfRequest2 = new CFSource("123456abc", "annguyen.qh@gmail.com");
		MessageData purgeCache = cfRequest2.purgeCache("mapiii.com", id);
		assertNotEquals("purge file fail, MessageData: " + purgeCache.toString(), MessageData.SUCCESS, purgeCache);
	}

	@Test
	public void getZoneID() {
		CFSource cfRequest = new CFSource();
		String zoneID = cfRequest.getZoneID("mapiii.com");
		assertNotNull(zoneID);
		assertNotEquals(zoneID, "");
	}

	@Test
	public void getZoneIdNotExist() {
		CFSource cfRequest = new CFSource();
		String zoneID = cfRequest.getZoneID("map.com");
		assertNotNull(zoneID);
		assertEquals(zoneID, "");
	}

	@Test
	public void getZoneIdWrongCredential() {
		CFSource cfRequest = new CFSource("123456abc", "annguyen.qh@gmail.com");
		String zoneID = cfRequest.getZoneID("mapiii.com");
		assertNull(zoneID);
	}
}