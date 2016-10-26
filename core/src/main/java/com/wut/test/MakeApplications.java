package com.wut.test;
/*
import com.wut.resources.common.WutOperation.TYPE;


public class MakeApplications {

	private static TestingHelper server = new TestingHelper();
	private static String format = "json";
	private static int loc = 1;
	
	public static void main(String[] args) {
		// Create Application
		String createApp = server.getResource(TYPE.UPDATE, format, "application", "{\"id\":\"test\"}");
		System.out.println("Creation");
		System.out.println(createApp);
		createComponent("list", "{}");
		createComponent("list", "{}");
		
		// Get Application
		String components = server.getResource(TYPE.UPDATE, format, "application", "{\"id\":\"test\"}");
		System.out.println("Components");
		System.out.println(components);
	}
	
	private static void createComponent(String component, String config) {
		server.getResource(TYPE.CREATE, format, "application.component", getComponentParams(component, config));
	}
	
	private static String getComponentParams(String component, String config) {
		String parameters = "{\"application\":\"test\",\"location\":\"" + loc++ + "\",\"component\":\"" + component + "\",\"configuration\":\"" + config + "\"}";
		return parameters;
	}
}
*/