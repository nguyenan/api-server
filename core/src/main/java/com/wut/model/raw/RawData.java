package com.wut.model.raw;

import com.wut.model.AbstractData;
import com.wut.model.Data;
import com.wut.model.Model;

public class RawData extends AbstractData {
	private Object data;
	public RawData(Object o) {
		this.data = o;
	}
	public Object getData() {
		return data;
	}
	public String toString() {
		return data.toString();
	}
	@Override
	public Model<? extends Data> getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
