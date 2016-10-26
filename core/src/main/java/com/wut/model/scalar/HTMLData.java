package com.wut.model.scalar;

public class HTMLData extends ScalarData {
	private String data;

	public HTMLData(String html) {
		this.data = html;
	}

	@Override
	public String toRawString() {
		return data;
	}

	@Override
	public void fromRawString(String str) {
		data = str;
	}
	
	@Override
	public String toString() {
		return data;
	}
}
