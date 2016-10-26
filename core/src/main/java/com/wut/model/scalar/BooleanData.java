package com.wut.model.scalar;


public class BooleanData extends ScalarData {
	public static final BooleanData TRUE = new BooleanData(true);
	public static final BooleanData FALSE = new BooleanData(false);

	boolean value;

	public BooleanData(boolean value) {
		this.value = value;
	}

	@Override
	public String toRawString() {
		return value ? "TRUE" : "FALSE";
	}

	@Override
	public void fromRawString(String str) {
		throw new RuntimeException("from raw string not supported");
	}


	public static BooleanData create(boolean booleanValue) {
		return booleanValue ? BooleanData.TRUE : BooleanData.FALSE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BooleanData other = (BooleanData) obj;
		if (value != other.value)
			return false;
		return true;
	}


}
