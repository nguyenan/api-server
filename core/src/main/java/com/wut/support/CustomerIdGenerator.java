package com.wut.support;

import java.util.UUID;

public class CustomerIdGenerator {
	public static String getNewId() {
		String uniqueID =  UUID.randomUUID().toString();
		uniqueID = uniqueID.replaceAll("-", "");
		uniqueID = "l1s1" + uniqueID;
		System.out.println(uniqueID);
		return uniqueID;
	}		 
}