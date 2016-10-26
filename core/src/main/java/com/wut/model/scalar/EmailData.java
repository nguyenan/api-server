package com.wut.model.scalar;

public class EmailData extends ScalarData {
	private String data;

	public EmailData(String string) {
		this.data = string;
	}

	@Override
	public String toRawString() {
		return data;
	}

	@Override
	public void fromRawString(String str) {
		data = str;
	}

}
