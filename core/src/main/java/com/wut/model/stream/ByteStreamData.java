package com.wut.model.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ByteStreamData extends StreamData {
	private ByteArrayOutputStream data = new ByteArrayOutputStream();

	public ByteStreamData() {
		
	}
	
	public ByteStreamData(byte[] bytes) throws IOException {
		data.write(bytes);
	}
	
	public void write(byte[] bytes) throws IOException {
		data.write(bytes);
	}
	
	public void write(byte[] bytes, int offset, int length) {
		data.write(bytes, offset, length);
	}

	public void write(int integer) {
		data.write(integer);
	}

	@Override
	public String toRawString() {
		try {
			String raw = new String(data.toByteArray(), "UTF-8");
			raw = encodeURIComponent(raw);
			return raw;
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	@Override
	public void fromRawString(String str) {
		//data = new StringBuilder(str);
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
	public static String encodeURIComponent(String s) {
	    String result;

	    try {
	        result = URLEncoder.encode(s, "UTF-8")
	                .replaceAll("\\+", "%20")
	                .replaceAll("\\%21", "!")
	                .replaceAll("\\%27", "'")
	                .replaceAll("\\%28", "(")
	                .replaceAll("\\%29", ")")
	                .replaceAll("\\%7E", "~");
	    } catch (UnsupportedEncodingException e) {
	        result = s;
	    }

	    return result;
	}


}
