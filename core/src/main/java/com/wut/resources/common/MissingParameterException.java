package com.wut.resources.common;

// TODO this shouldn't be in common!!!
public class MissingParameterException extends Exception {
	private static final long serialVersionUID = 23452461254L;
	public MissingParameterException(String name) {
		super("Missing Parameter " + name);
	}
	public MissingParameterException(String dependency, String[] names) {
		super("If parameter " + dependency + " is present you must also send the following parameters: " + String.join(", ", names));
	}
}
