package com.wut.provider.operation;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public interface CreateProvider extends Provider {
	public IdData create(MappedData map);
}
