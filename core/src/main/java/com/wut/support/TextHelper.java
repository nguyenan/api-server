package com.wut.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// TODO rename TextUtil and put in text package
public class TextHelper {
	
	public static String arrayToString(Object[] array) {
		return arrayToString(array, ",");
	}
	
	public static String arrayToString(Object[] array, String delimeter, int start,
			int end) {
		StringBuilder sb = new StringBuilder();
		for (int i=start; i<end; i++) {
			Object o = array[i];
			sb.append(String.valueOf(o));
			sb.append(delimeter);
		}
		deleteLastDelimeter(sb, delimeter);
		return sb.toString();
	}
	
	public static String arrayToString(Object[] array, String delimeter) {
		return arrayToString(array, delimeter, 0, array.length);
	}
	
	public static String collectionToString(Collection<?> c) {
		return c.toString();
	}
	
	public static String collectionToString(Collection<?> c, String delimeter) {
		StringBuilder result = new StringBuilder(c.toString().replaceAll(",", delimeter));
		result.deleteCharAt(0);
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	public static String mapToString(Map<?, ?> map) { //, String valueSeperator, String delimeter) {
		return mapToString(map, "=", ",");
	}

	public static String mapToString(Map<?, ?> map, String valueSeperator, String delimeter) {
		return mapToString(map, "", "", valueSeperator, delimeter);
	}
	
	public static String mapToString(Map<?, ?> map, String keyWrap,
			String valWrap, String valueSeperator, String delimeter) {
		StringBuilder text = new StringBuilder();
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			text.append(keyWrap + key + keyWrap + valueSeperator + valWrap + value + valWrap + delimeter);
		}
		deleteLastDelimeter(text, delimeter);
		return text.toString(); 
	}

	public static String concat(String delimeter, String... strings) {
		StringBuilder builder = new StringBuilder();
		for (String s : strings) {
			builder.append(s);
			builder.append(delimeter);
		}
		deleteLastDelimeter(builder, delimeter);
		return builder.toString();
	}
	
	public static void deleteLastDelimeter(StringBuilder sb, String delimeter) {
		if (sb.length() > delimeter.length()) {
			sb.delete(sb.length()-delimeter.length(), sb.length());
		}
	}

	public static void collectionToString(List<String> cols, StringBuilder sql) {
		for (String col : cols) {
			sql.append(col + ",");
		}
		if (sql.length()>1) {
			sql.deleteCharAt(sql.length()-1);
		}
	}
	
	public static String perWordCapitalize(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<str.length(); i++) {
			String current = String.valueOf(str.charAt(i));
			String last = String.valueOf(i>0 ? str.charAt(i) : '!'); // TODO fix this magic char
			if (last.equals(last.toUpperCase())) {
				sb.append(current.toUpperCase());
			} else {
				sb.append(current);
			}
		}
		return sb.toString();
	}

	public static String trim(String s, char toTrim) {
		StringBuilder sb = new StringBuilder(s);
		return trim(sb, toTrim);
	}
	
	public static String trim(StringBuilder s, char toTrim) {
        int originalLen = s.length();
        int len = s.length();
        int st = 0;

        while ((st < len) && (s.charAt(st) == toTrim)) {
            st++;
        }
        while ((st < len) && (s.charAt(len - 1) == toTrim)) {
            len--;
        }
        return ((st > 0) || (len < originalLen)) ? s.substring(st, len) : s.toString();
    }
	


}
