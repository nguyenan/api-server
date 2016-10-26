package com.wut.provider.operation;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.provider.Provider;

public interface ReadProvider extends Provider {
	public MappedData read(IdData id);

}
