package com.wut.support.settings;

public class SettingNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -9194751332420050905L;
	public SettingNotFoundException(String string) {
		super(string);
	}
}
