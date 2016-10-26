package com.wut.format;

import com.wut.model.Data;
import com.wut.model.Model;

public interface Token {
	
	public enum PURPOSE {
		OPEN, 
		CLOSE, 
		DELIMINTER, // this splits up items
		ITEM_OPEN, // TODO remove
		ITEM_CLOSE, // TODO remove
		ITEM_DELIMINTER, // rename KEY VALUE DELIM // TODO turn into START_KEY, END_KEY, START_VALUE, END_VALUE
		TERMINAL, // TODO use
		WHITE_SPACE,
		// NEW!!!!
		NESTED_ITEM_OPEN, // TODO not used
		NESTED_ITEM_CLOSE, // TODO not used
		KEY_OPEN, // used
		KEY_CLOSE, // used
		VALUE_OPEN, // used
		VALUE_CLOSE // used
	};
	
	public Model<? extends Data> getModel();
	
	public void setModel(Model<? extends Data> model);

	public PURPOSE getPurpose();

	public void setPurpose(PURPOSE purpose);

	public String getData();

	public void setData(String data);
	
	public boolean matchesBuffer(String buffer, int startPosition);

	public boolean isOptional();
	
}
