package com.wut.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO rename UrlUtil and put in url package
public class URLArguments {
	
	public static Map<String, String> parseArguments(String arguments) {
		//arguments = Reference.decode(arguments);
		int questMarkLoc = arguments.indexOf('?');
		//String command = arguments.substring(0, questMarkLoc+1);
		if (questMarkLoc != -1) {
			arguments = arguments.substring(questMarkLoc+1);
		} else if (arguments.indexOf('/') != -1) {
			arguments = arguments.substring(arguments.indexOf('/')+1);
		}
		return parseMap1(arguments);
	}
	
	// only used by parseArguments right now -- not sure if it will parse an
	// inner argument map
	private static Map<String, String> parseMap1(String arguments) {
		Map<String, String> argsMap = new HashMap<String, String>();
		String[] args = arguments.split("&");
		
		for (String arg : args) {
			String[] variable = arg.split("=");
			if (variable.length == 2) {
				// TODO maybe wait until it's parsed to decode
				// maybe even create a class to wrap the parameters
				// OLD BETTER WAY				
//				String name = Reference.decode(variable[0]);
//				String value = Reference.decode(variable[1]);
				
				// NEW LESS COUPLED WAY
				try {
					String name = URLDecoder.decode(variable[0], "UTF-8");
					String value = URLDecoder.decode(variable[1], "UTF-8");
					String valueWithoutQuotes = value.replaceAll("\"", "");
					argsMap.put(name, valueWithoutQuotes);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				ErrorHandler.userError("invalid url argument passed. possibly param without value");
			}
		}
		return argsMap;
	}
	
	public static Map<String, String> parseMap(String string) {
		String withoutBraces = string.replaceAll("\\{","").replaceAll("\\}", "");
		String[] args = withoutBraces.split(",");
		
		Map<String, String> map = new HashMap<String, String>();
		for (String arg : args) {
			try {
				String[] variable = arg.split(":");
				
				if (variable.length == 2) {
					// TODO maybe wait until it's parsed to decode
					// maybe even create a class to wrap the parameters
					String name = URLDecoder.decode(variable[0], "UTF-8").trim();
					String value = URLDecoder.decode(variable[1], "UTF-8").trim();
					String nameWithoutQuotes = name.replaceAll("\"", "").replaceAll("'", "").trim();
					String valueWithoutQuotes = value.replaceAll("\"", "").replaceAll("'", "").trim();
					map.put(nameWithoutQuotes, valueWithoutQuotes);
				}
			} catch (UnsupportedEncodingException e) {
				ErrorHandler.fatalError(e, "failed to parse map");
				
			}
		}
		return map;
	}
	
	
	public static List<String> parseList(String str) {
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(str);
		if (sb.charAt(0) == '[') {
			if (sb.charAt(sb.length()-1) != ']') {
				ErrorHandler.systemError("[possible user error] Invalid list passed to URLArguments.parseList():" + str);
				return Collections.emptyList();
			}
			sb.deleteCharAt(0); // delete first '['
			sb.deleteCharAt(sb.length()-1); // delete last ']'
			String[] split = sb.toString().split(",");
			for (String s : split) {
				s = removeQuotes(s);
				list.add(s);
			}
		} else {
			list = Collections.singletonList(removeQuotes(str.toString()));
		}
		return list;
	}
	
	private static String removeQuotes(String s) {
		s = s.trim();
		if (s.charAt(0) == '\"') {
			s = s.replaceAll("\"", ""); // TODO ONLY REMOVE FIRST AND LAST QUOTE (")
		}
		return s;
	}


	public static Map<String, String> parse(String arguments) {
		try {
			arguments = URLDecoder.decode(arguments, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ErrorHandler.systemError(e, "failed to parse url arguments");
		}
		
		String argsWithoutQM = arguments.startsWith("?") ? arguments.substring(1) : arguments;
		
		return splitArguments(argsWithoutQM);
	}
	
	public static Map<String, String> splitList(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder(str);
		sb.deleteCharAt(0); // delete first '['
		sb.deleteCharAt(sb.length()); // delete last ']'
		String justArgs = sb.toString();
		Map<String, String> argMap = splitArguments(justArgs);
		return argMap;
	}
	
	public static Map<String, String> splitArguments(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		String[] args = str.split("&");
		
		for (String arg : args) {
			String[] variable = arg.split("=");
			if (variable.length == 2) {
				String name = variable[0];
				String value = variable[1].replaceAll("\"", "");
				argsMap.put(name, value);
			}
		}
		return argsMap;
	}
	
	
	public static String buildUrl(String base, Map<String,String> parameters) {
		StringBuilder url = new StringBuilder(base);
		url.append("?");
		for (String key : parameters.keySet()) {
			String value = parameters.get(key);
			url.append(key);
			url.append("=");
			url.append(value);
			url.append("&");
		}
		url.deleteCharAt(url.length()-1);
		return url.toString();
	}
	
	
	
	
}
