package com.wut.provider.operation;

import com.wut.model.scalar.BooleanData;
import com.wut.provider.Provider;

public interface ReadAllProvider extends Provider {
	public BooleanData read();
}
