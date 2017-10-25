package com.wut.model;

public enum PermissionRole {
	OWNER("owner"), MANAGER("manager"), ADMIN("admin"), CREWMEMBER("crewmember");

	private String value;

	PermissionRole(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static boolean contains(String value) {
		for (PermissionRole p : PermissionRole.values()) {
			if (p.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
