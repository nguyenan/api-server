package com.wut.pipeline;

import com.wut.support.fileio.WutFile;

public class LogRequestProcessor extends AbstractProcessor {

	@Override
	public boolean process(WutRequest request, WutResponse response) {
		//System.out.println(request);
		
		System.out.println("REQUEST: " + request);
		
		//BufferedWriter out = new BufferedWriter(new FileWriter("./html/requests.xml",true));
		//out.write(request.toXMLString());
		//out.close();
		WutFile requestWriter = WutFile.create("html/requests.xml");
		requestWriter.write(request.toXMLString());
		requestWriter.close();
		
		return true;
	}

}
