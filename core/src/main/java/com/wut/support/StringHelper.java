package com.wut.support;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StringHelper {
	// TODO move general string functions here
	
	public static String addPrefix(String prefix, String str) {
		return prefix + str;
	}
	
	public static String removePrefix(String prefix, String str) {
		return str.substring(prefix.length(), str.length());
	}
	
	public static String removePrefixIfPresent(String prefix, String str) {
		String newString = null;
		if (str.startsWith(prefix)) {
			newString = str.replaceFirst(prefix, "");
		}
		return newString;
	}

	public static String capitalize(String str) {
		StringBuilder sb = new StringBuilder(str);
		String firstChar = sb.substring(0, 1);
		String upperFirstChar = firstChar.toUpperCase();
		sb.delete(0, 1);
		sb.insert(0, upperFirstChar);
		return sb.toString();
	}
	
	public static String combine(String[] strs, String delimeter) {
		StringBuilder sb = new StringBuilder(strs.length*(20+delimeter.length()));
		for (String str : strs) {
			sb.append(str);
			sb.append(delimeter);
		}
		return sb.toString();
	}
	
	public static InputStream asInputStream(
			String str) {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream( str.getBytes( "UTF-8" ) );
		} catch (UnsupportedEncodingException e) {
			ErrorHandler.systemError(e, "ERROR converting to string");
		}
		return is;
	}
	
	// sentenceCase()
	// etc
}
