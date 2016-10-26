package com.wut.provider.operation;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public interface UpdateProvider extends Provider {
	public BooleanData update(IdData id, MappedData data);

}
