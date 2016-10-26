package com.wut.format;

import java.io.IOException;
import java.io.InputStream;

import com.wut.model.Data;

public interface Decoder {
	
	public abstract Data decode(InputStream input) throws IOException;

	public abstract Data decode(String input) throws IOException;

}