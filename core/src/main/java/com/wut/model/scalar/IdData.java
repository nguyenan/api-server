package com.wut.model.scalar;

public class IdData extends ScalarData implements Comparable<IdData> {
	private String data;

	public IdData(String string) {
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
	
	@Override
	public String toString() {
		return data;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		IdData other = (IdData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public int compareTo(IdData o) {
		return data.compareTo(o.toRawString());
	}

	public static IdData create(String client) {
		return new IdData(client);
	}
	
	
}
