package com.wut.model.scalar;

public class UrlData extends ScalarData {
	private String url;

	public UrlData(String url) {
		this.url = url;
	}

	@Override
	public void fromRawString(String str) {
		url = str;
	}

	@Override
	public String toRawString() {
		return url;
	}

}
