package com.wut.support;

public class Defaults {
	
	public static boolean isDefaultApplication(String application) {
		return application.equals("core") || application.equals("storefront");
	}

}
