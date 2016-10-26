package com.wut.model.scalar;

import com.wut.model.AbstractData;
import com.wut.model.Model;

public abstract class ScalarData extends AbstractData {
	@Override
	public String toString() { return toRawString(); }
	
	public abstract String toRawString();
	
	public abstract void fromRawString(String str);
	
	@Override
	public Model<ScalarData> getModel() {
		return ScalarModel.create();
	}
}
