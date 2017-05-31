package com.wut.provider.scalar;

import com.wut.datasources.ScalarSource;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;

public class DefaultScalarProvider implements ScalarProvider {
	
	private ScalarSource source;

	public DefaultScalarProvider(ScalarSource source) {
		this.source = source;
	}

	@Override
	public ScalarData read(IdData customer, IdData application, IdData scalarId) {
		return source.readScalar(customer.toRawString(), application.toRawString(), scalarId.toRawString());
	}

	@Override
	public BooleanData update(IdData customer, IdData application, IdData scalarId, ScalarData scalarData) {
		boolean updateSucceeded = source.updateScalar(customer.toRawString(), application.toRawString(), scalarId.toRawString(), scalarData.toRawString().getBytes());
		return new BooleanData(updateSucceeded);
	}

	@Override
	public BooleanData delete(IdData customer, IdData application, IdData scalarId) {
		throw new RuntimeException("delete not supported");
	}

}
