package com.wut.model.stream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.wut.model.scalar.ScalarData;

// TODO remove extending of ScalarData ???
public class StreamData extends ScalarData {
	private String data;

	public StreamData(String string) {
		this.data = string;
	}
	
	public StreamData() {
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StreamData)) {
			return false;
		}
		StreamData other = (StreamData) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}

	@Override
	public String toRawString() {
		return data;
	}

	@Override
	public void fromRawString(String str) {
		data = str;
	}
	
	@Override
	public String toString() {
		return data;
	}
	
	public InputStream getRawStream() {
		try {
			InputStream is = new ByteArrayInputStream( data.getBytes( "UTF-8" ) );
			return is;
		} catch (UnsupportedEncodingException e) {
			//ErrorHandler.systemError(e, "ERROR converting to string");
		}
		return null;
	}

//	public static StreamData create(OutputStream file) {
//		return null;
//	}
}
