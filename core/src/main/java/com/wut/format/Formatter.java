package com.wut.format;

import com.wut.model.Data;
import com.wut.pipeline.WutRequestInterface;
import com.wut.support.StreamWriter;

// TODO rename Encoder
public interface Formatter {
	
	public String format(Data data);//, WutRequestInterface request);
	
	// TODO rename Encode
	//public void format(Data data, OutputStream stream, WutRequest request);
	public void format(Data data, StreamWriter stream);//, WutRequestInterface request);
	
	public void start(StreamWriter stream);//, WutRequestInterface request);
	
	public void end(StreamWriter stream);//, WutRequestInterface request);
	
	// descriptiveFormat
	// plainFormat
	// headerFormat
	
	// startItem(type)
	// item(string)
	// endItem()
	// increaseDepth()
	// decreaseDepth()
	// startHeader()
	// endHeader()
}
