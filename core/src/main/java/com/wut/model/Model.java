package com.wut.model;

import com.wut.format.Input;
import com.wut.format.TokenSet;
import com.wut.support.StreamWriter;


public interface Model<T> {

	// TODO rename to "decode()"
	public T parse(TokenSet tokens, Input input);
	
	// TODO create "encode()"
	public void format(TokenSet tokens, StreamWriter stream, T data, Context context);

	//public Representation getRepresentation(String representationName); // getFormatedData
	//public Object getData();
	//public void format(String format, OutputStream stream);
 
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
