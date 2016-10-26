package com.wut.support;

import java.util.Collection;

// Rename and break into classes: CollectionsHelper, StringHelper, SystemHelper
public class Language {
	public static <T> T ifNullThenDefault(T object, T defaultValue) {
		if (object == null)
			return defaultValue;
		else
			return object;
	}
	
	public static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
	    for (T o : a) {
	        c.add(o);
	    }
	}
	
	
	// TODO use Error.reportError()
	public static void forceExit(String errorMessage) {
		System.err.println("FATAL ERROR: " + errorMessage);
		System.exit(-1);
	}
	
	public static void forceExit() {
		System.exit(-1);
	}
	
	public static void forceExit(Exception e, String errorMessage) {
		System.err.println("FATAL ERROR: " + errorMessage);
		System.err.println("ERROR MESSAGE: " + e.getMessage());
		System.err.println("TRACE: ");
		e.printStackTrace();
		System.exit(-1);
	}

	public static boolean isBlank(Object string) {
		if (string == null || "".equals(String.valueOf(string))) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotBlank(String string) {
		return !isBlank(string);
	}
	
	public static boolean isAnyBlank(String... strings) {
		for (String s : strings) {
			if (isBlank(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean areAllBlank(String... strings) {
		for (String s : strings) {
			if (!isBlank(s)) {
				return false;
			}
		}
		return true;
	}

	public static String[] splitFirst(String str, String deliminer) {
		String[] array = new String[2];
		int delimIndex = str.indexOf(":");
		array[0] = str.substring(0, delimIndex);
		array[1] = str.substring(delimIndex, str.length()-1);
		return null;
	}



}
