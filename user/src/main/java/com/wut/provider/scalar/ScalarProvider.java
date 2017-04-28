package com.wut.provider.scalar;

import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.provider.Provider;

public interface ScalarProvider extends Provider {
	
	public ScalarData read(IdData customer, IdData application, IdData scalarId);
	
	public BooleanData update(IdData customer, IdData application, IdData scalarId, ScalarData scalarData);
	
	public BooleanData delete(IdData customer, IdData application, IdData scalarId);
	
}
