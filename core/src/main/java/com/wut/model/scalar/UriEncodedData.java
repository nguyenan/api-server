package com.wut.model.scalar;

import com.wut.encodingAndDecoding.WutEncoder;

public class UriEncodedData extends StringData {

	public UriEncodedData(String data) {
		super(data);
	}
	
	public static UriEncodedData createFromUnencodedString(String unencodedString) {
		return new UriEncodedData(WutEncoder.encodeURIComponent(unencodedString));
	}

}
