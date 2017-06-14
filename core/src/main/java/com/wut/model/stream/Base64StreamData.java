package com.wut.model.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Base64StreamData extends com.wut.model.stream.StreamData {
//	private byte[] data;
	private InputStream is;
	private String streamAsString;

	public Base64StreamData(InputStream is) {
		this.is = is;
		
		// read stream to string
		StringBuilder sb = new StringBuilder();
		
		String str = "";
		byte[] chars = new byte[1000];
		try {
			//BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			//InputStream inputStream = new BufferedInputStream(is);
			while ((is.read(chars)) != -1) {
				byte[] bytes64bitEncoded = chars; //Base64.encodeBase64(chars); // this needs to be done at the end. encoding chunks don't work.
				String chunk = new String(bytes64bitEncoded);
				sb.append(chunk);
				chars = new byte[1000];
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		streamAsString = sb.toString(); //Base64.encodeBase64String(sb.toString().getBytes());
	}
	
	public static Base64StreamData create(InputStream is) {
		return new Base64StreamData(is);
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

//	public static StreamData create(OutputStream file) {
//		return null;
//	}
}
