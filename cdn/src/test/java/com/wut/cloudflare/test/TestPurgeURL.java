package com.wut.cloudflare.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.cloudflare.CFSource;
import com.wut.model.map.MessageData;

public class TestPurgeURL {

	@Test
	public void testPurge() {
		String id = "index.html";
		CFSource cfRequest = new CFSource();
		MessageData purgeCache = cfRequest.purgeCache("mapii.com", id);
		
	}

	// @Test
	public void testZoneID() {
		String id = "index.html";
		CFSource cfRequest = new CFSource();
		cfRequest.getZoneID("mapii.com");
		// assertTrue("purgeCache", purgeCache);
	}

}