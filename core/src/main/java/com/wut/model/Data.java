package com.wut.model;



public interface Data /*extends Serializable*/ {

	//public Representation getRepresentation(String representationName); // getFormatedData
	//public Object getData();
	//public void format(String format, OutputStream stream);
	
	public Model<? extends Data> getModel();
 
	// getRawData
	
	// TODO add format
	
}

/* Implementors:
aka types of data
generic (obj) -> toString (raw), xml, json
iterable -> xml, json, html, ...
tabular -> xml, json, html, ...
image -> jpg, png, gif, bmp, etc
*/
