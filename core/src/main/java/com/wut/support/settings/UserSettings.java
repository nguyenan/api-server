package com.wut.support.settings;

public class UserSettings extends Settings {
	public UserSettings(String client, String username) {
		
	}
	
	@Override
	public String getSetting(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putSetting(String name, String value) {
		throw new RuntimeException("putSetting method not supported");
	}
}
