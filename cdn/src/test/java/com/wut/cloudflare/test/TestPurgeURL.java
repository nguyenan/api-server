package com.wut.cloudflare.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.cloudflare.CFSource; 

public class TestPurgeURL {

	//@Test
	public void testPurge() {
		String id = "index.html";
		CFSource cfRequest = new CFSource();
		boolean purgeCache = cfRequest.purgeCache("mapii.com", id);
		assertTrue("purgeCache", purgeCache); 
	}
	@Test
	public void testZoneID() {
		String id = "index.html"; 
		CFSource cfRequest = new CFSource();
		cfRequest.getZoneID("mapii.com");
		//assertTrue("purgeCache", purgeCache); 
	}

}