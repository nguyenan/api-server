package com.wut.provider.operation;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.provider.Provider;

public interface SearchProvider extends Provider {
	public MappedData search(StringData term);
}
