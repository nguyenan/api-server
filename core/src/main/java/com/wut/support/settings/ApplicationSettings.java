package com.wut.support.settings;

public class ApplicationSettings extends Settings {
	
	private int port;
	private String host;
	
	public ApplicationSettings() {
		this("notset", -1);
	}

	public ApplicationSettings(String hostname, int port) {
		this.host = hostname;
		this.port = port;
	}
	
	@Override
	public String getSetting(String name) {
		if ("port".equals(name)) {
			return String.valueOf(port);
		} else if ("host".equals(name)) {
			return host;
		}
		return null;
	}

	@Override
	public void putSetting(String name, String value) {
		throw new RuntimeException("putSetting method not supported");
	}

}
