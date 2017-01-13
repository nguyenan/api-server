package com.wut.datasources.cloudflare;

public class CFAuth {
	private String key;
	private String email;

	public CFAuth(String key, String email) {
		this.key = key;
		this.email = email;
	}

	public String getKey() {
		return key;
	}

	public String getEmail() {
		return email;
	}
}