package com.wut.model.scalar;

public class JsonData extends ScalarData {
	private String data;

	public JsonData(String json) {
		this.data = json;
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
