package com.wut.model.scalar;

public class IntegerData extends ScalarData {
	int data;

	public IntegerData(int integer) {
		this.data = integer;
	}

	@Override
	public String toRawString() {
		return String.valueOf(data);
	}

	@Override
	public void fromRawString(String str) {
		data = Integer.parseInt(str);
	}

	public int getInteger() {
		return data;
	}

}
