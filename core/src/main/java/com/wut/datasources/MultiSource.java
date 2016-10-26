package com.wut.datasources;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;

public abstract class MultiSource extends AbstractTable implements ScalarSource {
	private static final IdData SCALARS_TABLE_ID = new IdData("_scalars_table_");
	private static final StringData SCALARS_VALUE_ID = new StringData("_value_");
	
	@Override
	public ScalarData readScalar(String customer, String application, String id) {
		MappedData row = getRow(new IdData(customer), new IdData(application), SCALARS_TABLE_ID, new IdData(id));
		if (row == null) {
			return null;
		}
		ScalarData value = (ScalarData) row.get(SCALARS_VALUE_ID);
		return value;
	}

	@Override
	public boolean updateScalar(String customer, String application, String id, byte[] data) {
		IdData idData = new IdData(id);
		ScalarData scalarData = new StringData(new String(data));
		MappedData row = new MappedData();
		row.put(SCALARS_VALUE_ID, scalarData);
		updateRow(new IdData(customer), new IdData(application), SCALARS_TABLE_ID, idData, row);
		return true;
	}

}
