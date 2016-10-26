package com.wut.datasources;

import com.wut.model.scalar.ScalarData;

public interface ScalarSource extends DataSource {
	public ScalarData readScalar(String customer, String application, String id);
	public boolean updateScalar(String customer, String application, String id, byte[] data);
}
