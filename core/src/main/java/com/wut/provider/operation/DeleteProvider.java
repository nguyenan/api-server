package com.wut.provider.operation;

import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public interface DeleteProvider extends Provider {
	public BooleanData delete(IdData id);

}
