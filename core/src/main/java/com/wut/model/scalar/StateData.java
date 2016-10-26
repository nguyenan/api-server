package com.wut.model.scalar;

public class StateData extends ScalarData {
	private String data;

	public StateData(String string) {
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
