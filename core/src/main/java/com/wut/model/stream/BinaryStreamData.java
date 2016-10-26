package com.wut.model.stream;

import java.io.InputStream;

public class BinaryStreamData extends StreamData {
	private String data;

	private BinaryStreamData(InputStream stream) {
		super(""); // TODO really???
	}
	
	public static BinaryStreamData create(InputStream stream) {
		BinaryStreamData bsd = new BinaryStreamData(stream);
		return bsd;
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
}
