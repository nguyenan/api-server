package com.wut.pipeline;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

// TODO rename AuthenticationToken
public class Token {
	private String token;
	private static Random rand = new Random();

	public Token(String token) {
		this.token = token;
	}
	public String getToken() {
		return this.token;
	}
	@Override
	public String toString() {
		return this.token;
	}
	
	public static Token generateNewToken(String username, String password) {
		String d1234 = "T000";
		int d5 = rand.nextInt(10);
		int d6 = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
		int d7 = rand.nextInt(10);
		char d8 = username.charAt(d6 % username.length());
		int d9 = Math.abs(password.hashCode() % 9);
		int d10 = rand.nextInt(10);
		int d11 = Math.abs(username.hashCode() % 9);
		int d12 = rand.nextInt(10);
		char d13 = username.charAt((username.length() + 2) % 3);
		int d14 = (username.length() + 3) % 9;
		int d15 = 5;
		String token = d1234 + d5 + d6 + d7 + d8 + d9 + d10 + d11 + d12 + d13 + d14 + d15;
		token = token.toLowerCase();
		Token t = new Token(token);
		return t;
	}
	
}
