package com.wut.pipeline;

import java.util.Map;

import com.wut.support.settings.SystemSettings;

public class User {
	private String customer;
	private String id;
	private String password;
	private String firstName;
	private String lastName;
	private String token;
	private String username;
	

	public static User getPublicUser(String customer) {
		User publicUser = new User();
		publicUser.setFirstName("Public");
		publicUser.setLastName("Public");
		publicUser.setUsername("public");
		publicUser.setId("public");
		publicUser.setPassword("public");
		publicUser.setToken("public");
		publicUser.setCustomer(customer);
		return publicUser;
	}
	
	public static User getRetailKitAdminUser(String customer) {
		User publicUser = new User();
		publicUser.setFirstName(SystemSettings.getInstance().getSetting("defaultUserFirstName"));
		publicUser.setLastName(SystemSettings.getInstance().getSetting("defaultUserLastName"));
		publicUser.setUsername(SystemSettings.getInstance().getSetting("defaultUserUsername"));
		publicUser.setId(SystemSettings.getInstance().getSetting("defaultUserUsername"));
		publicUser.setPassword(SystemSettings.getInstance().getSetting("defaultUserPassword"));
		publicUser.setToken("");
		publicUser.setCustomer(customer);
		return publicUser;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getId() {
		return id;
	}
	public void setId(String username) {
		this.id = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public String toString() {
		return "User [customer=" + customer + ", id=" + id + ", token=" + token
				+ ", username=" + username + "]";
	}
}
