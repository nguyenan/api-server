package com.wut.model.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URIEncodedStreamData extends com.wut.model.stream.StreamData {
	private InputStream is;
	private String streamAsString;

	public URIEncodedStreamData(InputStream is) {
		this.is = is;
		
		// read stream to string
		StringBuilder sb = new StringBuilder();
		
		String str = "";
		byte[] chars = new byte[1000];
		try {
			while ((is.read(chars)) != -1) {
				byte[] bytes64bitEncoded = chars;
				String chunk = new String(bytes64bitEncoded);
				sb.append(chunk);
				chars = new byte[1000];
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			streamAsString = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	
	public static URIEncodedStreamData create(InputStream is) {
		return new URIEncodedStreamData(is);
	}
	
	@Override
	public String toRawString() {
		return streamAsString;
	}

	@Override
	public void fromRawString(String base64str) {
		is = new ByteArrayInputStream(base64str.getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public String toString() {
		return toRawString();
	}
	
	public InputStream getRawStream() {
		return is;
	}

}
