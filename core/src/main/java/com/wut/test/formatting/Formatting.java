package com.wut.test.formatting;

import java.io.IOException;

import com.wut.format.GenericDecoder;
import com.wut.model.Data;

public class Formatting {

	public static void main(String[] args) throws IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><credentials xmlns=\"http://docs.rackspacecloud.com/auth/api/v1.1\" username=\"russellpalmiter\" key=\"REPLACE_ME\" />";
		final RackSpaceXmlCharacterSet characterSet = new RackSpaceXmlCharacterSet();
		GenericDecoder decoder = new GenericDecoder(characterSet);
		Data data = decoder.decode(xml);
		System.out.println(data);
	}
}
