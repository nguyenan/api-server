package com.wut.support.settings;

//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;

import com.wut.support.fileio.WutFile;

public abstract class Settings {
	public abstract String getSetting(String name);
	public abstract void putSetting(String name, String value);
	
	protected void setDirectorySetting(String settingName, String defaultSetting) {
		String overriddenValue = System.getProperty(settingName, defaultSetting);
		putSetting(settingName, overriddenValue);
		
		WutFile.createFolderIfNeeded(overriddenValue);
		
//		File settingDir = new File(overriddenValue);
//		
//		if (!settingDir.exists()) {
//			settingDir.mkdirs();
//		} else if (!settingDir.isDirectory()) {
//		    throw new RuntimeException("setting " + settingName + " with value " + overriddenValue + " is a file and not a folder");
//		}
	}
}
