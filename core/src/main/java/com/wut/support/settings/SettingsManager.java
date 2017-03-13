package com.wut.support.settings;

import java.util.Map;

public class SettingsManager {
	private static SystemSettings sysSettings = new SystemSettings();
	private static long lastUpdated = System.currentTimeMillis();
	private static Map<String,ClientSettings> clientSettings = ClientSettings.loadFromConfig(true);	
	
	public static String getUserSetting(String userId, String settingName) {
		throw new SettingNotFoundException("setting " + settingName + " for user " + userId + " was no found");
	}
	
//	// TODO eliminate application settings for system settings instead
//	public static String getApplicationSetting(String settingName) {
//		String setting = appSettings.getSetting(settingName);
//		return setting;
//	}
	
	public static String getSystemSetting(String settingName) {
		String setting = sysSettings.getSetting(settingName);
		return setting;
	}
	
	// TODO rename getClientSetting()
	public static String getCustomerSettings(String customer, String settingName, boolean forceReload) {
		if (forceReload){
			System.out.println("forceRefresh Settings");
			clientSettings = ClientSettings.loadFromConfig(true);
		}
		return getCustomerSettings(customer, settingName);
	}
	public static String getCustomerSettings(String customer, String settingName) {		
		if ((System.currentTimeMillis() - lastUpdated) > (60*60*1000)){
			System.out.println("refresh Settings");
			lastUpdated = System.currentTimeMillis();
			clientSettings = ClientSettings.loadFromConfig(false);
		}
		ClientSettings customerSettings = clientSettings.get(customer);
		if (customerSettings == null) {
			throw new SettingNotFoundException("no settings for customer " + customer + " were found");
		}
		String settingValue = customerSettings.getSetting(settingName);
		if (settingValue == null) {
			throw new SettingNotFoundException("setting " + settingName + " for customer " + customer + " was no found");
		}
		return settingValue;
	}
	

	public static synchronized Boolean updateCustomerSettings(String customer, String setting, String value) {
		clientSettings = ClientSettings.loadFromConfig(false);
		ClientSettings customerSettings = clientSettings.get(customer);
		if (customerSettings == null) {
			// this version will auto initCustomerSettings if not exist
			createCustomerSettings(customer);
		}
		customerSettings = clientSettings.get(customer);
		customerSettings.putSetting(setting, value);
		boolean wasSucessful = ClientSettings.updateToConfig(customer, ClientSettings.toConfigString(customerSettings));
		
		return wasSucessful;
	}
	
	public static synchronized Boolean createCustomerSettings(String customerDomain) { 
		clientSettings = ClientSettings.loadFromConfig(false);
		ClientSettings customerSettings = clientSettings.get(customerDomain);
		if (customerSettings != null) {
			return true;
		} else {
			boolean wasSucessful = ClientSettings.addToConfig(customerDomain);
			clientSettings = ClientSettings.loadFromConfig(false);
			return wasSucessful;
		}			
	}
		
	//////////////////////////////////////////////////////
	/////////////////////////////////////////////////////
	/////////////// JUNK METHODS ////////////////////////
	/////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	
	private static String os = System.getProperty("os.name").toLowerCase();

	public static String getToolUtility(String basepath, String toolName, String utilityName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(basepath, toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(utilityName)); // tool executable
		
		return toolLocation.toString();
	}
	
	
	public static String getToolUtility(String toolName, String utilityName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(utilityName)); // tool executable
		
		return toolLocation.toString();
	}
	
	public static String getToolLocation(String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(toolName)); // tool executable
		
		return toolLocation.toString();
	}
	
	public static String getToolLocation(String basePath, String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(getToolDirectory(basePath, toolName)); // tool folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(toolName)); // tool executable
		
		return toolLocation.toString();
	}
	
	private static boolean onMacOperatingSystem() {
		return (os.indexOf("mac") >= 0);
	}
	
	private static boolean onUnixOperatingSystem() {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );
	}
	private static boolean onWindowsOperatingSystem() {
		return (os.indexOf("win") >= 0);
	}

	public static String getToolDirectory(String toolName) {
		String toolsBaseDir = sysSettings.getSetting("tools.base.directory");
		return getToolDirectory(toolsBaseDir, toolName);
	}
	
	public static String getToolDirectory(String basePath, String toolName) {
		StringBuilder toolLocation = new StringBuilder();
		
		toolLocation.append(basePath);
		
		boolean isWindows = onWindowsOperatingSystem();
		boolean isMac = onMacOperatingSystem();
		boolean isUnix = onUnixOperatingSystem();
		boolean isLinux = isUnix;
		boolean isSolaris = (os.indexOf("sunos") >= 0);
		
		boolean jvmIs64bit = System.getProperty("sun.arch.data.model").contains("64");
		boolean osIs64bit = (System.getProperty("os.arch").indexOf("64") != -1);
		boolean is64bit = jvmIs64bit || osIs64bit;

		if (isMac && is64bit) {
			//toolLocation.append("/mac/osx64/");
			toolLocation.append("/");
		} else if (isLinux && is64bit) {
			//toolLocation.append("/linux/ubuntu64/");
			toolLocation.append("/");
		} else {
			//throw new RuntimeException("running unsupported operating system / architecture: " + os + (is64bit ? " 64 bit" : " 32 bit"));
		}
		
		toolLocation.append(toolName); // tool folder
		
		return toolLocation.toString();
	}

	public static String getToolExecutable(String toolName) {
		StringBuilder toolExe = new StringBuilder();
		toolExe.append(toolName); // tool executable
		return toolExe.toString();
	}
	

	
//	/*
//	 * client = customer = domain
//	 */
//	public static boolean isClientLoaded(String client) {
//		return customers.contains(client);
//	}
}
