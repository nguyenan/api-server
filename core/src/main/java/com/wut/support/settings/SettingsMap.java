package com.wut.support.settings;

import java.util.HashMap;

public class SettingsMap extends Settings {
	private HashMap<String, String> settings = new HashMap<String, String>();

	@Override
	public String getSetting(String name) {
		return settings.get(name);
	}
	
	@Override
	public void putSetting(String name, String value) {
		settings.put(name, value);
	}
}
