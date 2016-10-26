package com.wut.model;

public abstract class AbstractData implements Data {

	// public void format(String format, OutputStream stream) {
	// Formatter f = FormatFactory.getFormatter(format);
	// f.format(this, stream, null);
	// }

	
	
	// public abstract Object getData();
	// TODO remove this function. it's horrible. no statics like this here. each to confuse for MappedData.get(value). Don't like. Make non static if needed.
	public static String getValue(Data data) {
		return String.valueOf(data);
	}
}
