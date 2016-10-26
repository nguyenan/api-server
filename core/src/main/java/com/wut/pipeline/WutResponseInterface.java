package com.wut.pipeline;

import com.wut.format.Formatter;
import com.wut.model.Data;
import com.wut.support.StreamWriter;

public interface WutResponseInterface {

	public abstract Data getError();

	//public abstract void represent(StreamWriter out, WutRequestInterface request);

	public abstract Data getData();

	public abstract StreamWriter getStream();

	public abstract boolean isFinalized();

	public abstract Formatter getFormat();

}