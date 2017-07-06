package com.wut.support.settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.support.ErrorHandler;

public class SettingsManager {
	private static final String APPLICATION = "core";
	private static SystemSettings sysSettings = new SystemSettings();
	private static SettingsStore settingsStore = new SettingsStore();
	private static Map<String, MappedData> clientSettingsMap = new HashMap<String, MappedData>();
	private static final List<String> ADMIN_DOMAIN = Arrays
			.asList(new String[] { "betaadmin.tend.ag", "admin.tend.ag", "polymer.tend.ag" });

	public static String getUserSetting(String userId, String settingName) {
		throw new SettingNotFoundException("setting " + settingName + " for user " + userId + " was no found");
	}

	public static String getSystemSetting(String settingName) {
		String setting = sysSettings.getSetting(settingName);
		return setting;
	}

	public static Boolean initClientSettings(String customerId) {
		try {
			// template
			if (ADMIN_DOMAIN.contains(customerId)) {
				setClientSettings(customerId, "template.git.repository", getSystemSetting("default.git.repository.admin"));
			} else {
				setClientSettings(customerId, "template.git.repository", getSystemSetting("default.git.repository.theme"));
			}
			
			// payment
			setClientSettings(customerId, "payment.payment-processor", "braintree");

			// email
			setClientSettings(customerId, "email.email-smtp-host", getSystemSetting("default.email-smtp-host"));
			setClientSettings(customerId, "email.email-smtp-port", getSystemSetting("default.email-smtp-port"));
			setClientSettings(customerId, "email.email-username", getSystemSetting("default.email-username"));
			setClientSettings(customerId, "email.email-password", getSystemSetting("default.email-password"));
			
			// user
			setClientSettings(customerId, "user.email-smtp-host", getSystemSetting("default.email-smtp-host"));
			setClientSettings(customerId, "user.email-smtp-port", getSystemSetting("default.email-smtp-port"));
			setClientSettings(customerId, "user.email-username", getSystemSetting("default.email-username"));
			setClientSettings(customerId, "user.email-password", getSystemSetting("default.email-password"));
			
			// init domain setting
			setClientSettings(customerId, "template.domain", customerId);
			setClientSettings(customerId, "email.domain", customerId);
			setClientSettings(customerId, "user.domain", customerId);
			setClientSettings(customerId, "dns.domain", customerId);
			setClientSettings(customerId, "cdn.domain", customerId);
			setClientSettings(customerId, "file.domain", customerId);
		} catch (Exception e) {
			ErrorHandler.systemError("init Clientsetting fail", e);
			return false;
		}
		return true;
	}

	/**
	 * get from local storage only when setting field existed and client doesn't require refreshSettings
	 * @param customer
	 * @param settingName
	 * @return
	 */
	public static String getClientSettings(String customer, String settingName) {
		try {	
			MappedData clientSettings = clientSettingsMap.get(customer);
			if ((clientSettings != null) && (clientSettings.get(settingName)!=null) ) {
					return ((StringData) clientSettings.get(settingName)).toString();
			}
	
			Data settingsData = settingsStore.read(customer, APPLICATION, settingName);
			if (settingsData.equals(MessageData.NO_DATA_FOUND))
				return "";
	
			// Update Map
			if (clientSettings == null)
				clientSettings = new MappedData();
			clientSettings.put(settingName, settingsData);
			clientSettingsMap.put(customer, clientSettings);
			return ((StringData) settingsData).toString();			
		} catch (Exception e) {
			ErrorHandler.userError("get Clientsetting fail", e);
			return "";
		}
	}


	public static synchronized Boolean setClientSettings(String customer, String settingName, String settingValue) {
		try {
			MappedData settingData = new MappedData();
			settingData.put("value", new StringData(settingValue));
			Map<String, String> settingDataPojo = settingData.getMapAsPojo();
			Data result = settingsStore.update(customer, APPLICATION, settingName, settingDataPojo);
			if (MessageData.SUCCESS.equals(result)) {
				MappedData clientSettings = clientSettingsMap.get(customer);
				if (clientSettings == null)
					clientSettings = new MappedData();
				clientSettings.put(settingName, new StringData(settingValue));
				clientSettingsMap.put(customer, clientSettings);
				return true;
			}
			return false;
		} catch (Exception e) {
			ErrorHandler.userError("set Clientsetting fail", e);
			return false;
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

		toolLocation.append(getToolDirectory(basePath, toolName)); // tool
																	// folder
		toolLocation.append("/");
		toolLocation.append(getToolExecutable(toolName)); // tool executable

		return toolLocation.toString();
	}

	private static boolean onMacOperatingSystem() {
		return (os.indexOf("mac") >= 0);
	}

	private static boolean onUnixOperatingSystem() {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
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
			// toolLocation.append("/mac/osx64/");
			toolLocation.append("/");
		} else if (isLinux && is64bit) {
			// toolLocation.append("/linux/ubuntu64/");
			toolLocation.append("/");
		} else {
			// throw new RuntimeException("running unsupported operating system
			// / architecture: " + os + (is64bit ? " 64 bit" : " 32 bit"));
		}

		toolLocation.append(toolName); // tool folder

		return toolLocation.toString();
	}

	public static String getToolExecutable(String toolName) {
		StringBuilder toolExe = new StringBuilder();
		toolExe.append(toolName); // tool executable
		return toolExe.toString();
	}
}
