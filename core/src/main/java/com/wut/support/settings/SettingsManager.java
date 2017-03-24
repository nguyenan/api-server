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
import com.wut.support.domain.DomainUtils;

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

	public static Boolean initClientSettings(String domain) {
		try {
			// payment
			setClientSettings(domain, "payment-processor", "braintree");

			// email
			setClientSettings(domain, "top-level-domain", DomainUtils.getTopLevelDomain(domain));
			setClientSettings(domain, "email-domain", domain);
			setClientSettings(domain, "email-from-address", "support@" + DomainUtils.getTopLevelDomain(domain));
			setClientSettings(domain, "email-smtp-host", getSystemSetting("default.email-smtp-host"));
			setClientSettings(domain, "email-smtp-port", getSystemSetting("default.email-smtp-port"));
			setClientSettings(domain, "email-username", getSystemSetting("default.email-username"));
			setClientSettings(domain, "email-password", getSystemSetting("default.email-password"));
			setClientSettings(domain, "password-reset-link",
					String.format("https://%s/account.html?", DomainUtils.getRealDomain(domain)));

			// template
			setClientSettings(domain, "git.branch", domain);
			if (ADMIN_DOMAIN.contains(domain)) {
				setClientSettings(domain, "git.repository", getSystemSetting("default.git.repository.admin"));
			} else {
				setClientSettings(domain, "git.repository", getSystemSetting("default.git.repository.theme"));
			}
			setClientSettings(domain, "client.code.dir", getSystemSetting("code.dir") + domain);
			setClientSettings(domain, "client.site.dir", getSystemSetting("site.dir") + domain);

			// analytic
			setClientSettings(domain, "google-analytics-view", getSystemSetting("default.google-analytics-view"));

			// cdn
			setClientSettings(domain, "domain", domain);
		} catch (Exception e) {
			ErrorHandler.systemError("init Clientsetting fail", e);
			return false;
		}
		return true;
	}

	public static String getClientSettings(String customer, String settingName) {
		return getClientSettings(customer, settingName, false);
	}

	/*
	 * get from local storage only when setting field existed and client doesn't
	 * require refreshSettings
	 */

	public static String getClientSettings(String customer, String settingName, boolean refreshSettings) {
		try {		
			MappedData clientSettings = clientSettingsMap.get(customer);
			if (clientSettings != null && !refreshSettings) {
				Data settingsData = clientSettings.get(settingName);
				if (settingsData != null)
					return ((StringData) settingsData).toString();
			}
	
			Data settingsData = settingsStore.read(customer, APPLICATION, settingName);
			if (settingsData.equals(MessageData.NO_DATA_FOUND))
				return "";
	
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
			MappedData userData = new MappedData();
			userData.put("value", new StringData(settingValue));
			Map<String, String> settingDataPojo = userData.getMapAsPojo();
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

	// /*
	// * client = customer = domain
	// */
	// public static boolean isClientLoaded(String client) {
	// return customers.contains(client);
	// }
}
