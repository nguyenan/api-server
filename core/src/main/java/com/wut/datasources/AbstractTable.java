package com.wut.datasources;

import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.support.UniqueIdGenerator;


// TODO abstract "soft delete" functionality into it's own class (use decorator pattern for these)
public abstract class AbstractTable implements TableSource {

	@Override
	public ListData getRowsWithFilter(IdData customer, IdData application, IdData tableId, MappedData filter) {
		ListData allRows = getAllRows(customer, application, tableId);
		ListData goodRows = new ListData();
		
		for (int i=0; i<allRows.size(); i++) {
			MappedData row = (MappedData) allRows.get(i);
			
			boolean rowIsMatch = true;
			for (ScalarData key : filter.keys()) {
				String filterPropAsString = filter.get(key).toString();
				String rowPropAsString = String.valueOf(row.get(key));
				boolean propertyMatch = filterPropAsString.equals(rowPropAsString);
				if (!propertyMatch) {
					rowIsMatch = false;
				}
			}
			
			if (rowIsMatch) {
				goodRows.add(row);
			}
		}
		
		return goodRows;
	}

	@Override
	public MappedData getRow(IdData customer, IdData application, IdData tableId, IdData rowId) {
		MappedData filter = new MappedData();
		filter.put("id", rowId);
		ListData allRows = getRowsWithFilter(customer, application, tableId, filter);
		MappedData row = null;
		if (allRows.size() > 0) {
			row = (MappedData) allRows.get(0);
		}
		return row;
	}

//	@Override
//	public BooleanData deleteRow(IdData tableId, IdData rowId) {
//		// TODO Auto-generated method stub
//		// TODO use update
//		MappedData softDelete = new MappedData();
//		softDelete.put("_deleted_", "true");
//		BooleanData update = updateRow(tableId, rowId, softDelete);
//		return update;
//	}

	public BooleanData updateRow(IdData customer, IdData application, IdData tableId, IdData rowId, MappedData data) {
		//System.out.println("TABLE UPDATE " + tableId);

		data.put("_deleted_", "false");

		// TODO first do a check for existance, fail if already exists (hence insert and not crupdate)
		BooleanData successfulCrupdate = crupdateRow(customer, application, tableId, rowId, data);
		
		return successfulCrupdate;
	}

	@Override
	public IdData insertRow(IdData customer, IdData application, IdData tableId, MappedData data) {
		System.out.println("TABLE INSERT " + tableId);

		IdData newId = new IdData(UniqueIdGenerator.getId());
		IdData rowId = newId; //new IdData(getRowScope(tableId, newId));
		
		// TODO first do a check for existance, fail if already exists (hence insert and not crupdate)
		data.put("_deleted_", "false");
		BooleanData successfulCrupdate = crupdateRow(customer, application, tableId, rowId, data);
		
		return successfulCrupdate.toRawString().equalsIgnoreCase("TRUE") ? rowId : null;
	}
	
//	private String getRowScope(IdData tableId, IdData rowId) {
//		return tableId.toRawString() + ":" + rowId.toRawString();
//	}
	
}
