package com.wut.model.scalar;

public class ZipCodeData extends ScalarData {
	private String data;

	public ZipCodeData(String string) {
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
