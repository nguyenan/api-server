package com.wut.support;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UniqueIdGenerator {
	// TODO rename getNewId()
	public static String getId() {
		String uniqueID =  UUID.randomUUID().toString();
		uniqueID = uniqueID.replaceAll("-", "");
		// ADD UNIQUE SERVER COMPONENT
		uniqueID = "l1s1" + uniqueID;
		System.out.println(uniqueID);
		return uniqueID;
	}
	
	 private AtomicLong id = new AtomicLong(0L);

	 public long getNextId(int granularity) {
	   return id.getAndAdd(granularity);
	 }
	 
//	 IDGenerator id = new IDGenerator();
//		id.next();
}
