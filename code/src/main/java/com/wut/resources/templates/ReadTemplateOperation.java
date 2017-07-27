package com.wut.resources.templates;

import java.io.BufferedReader;
import java.io.FileReader;
import com.wut.model.Data;
import com.wut.model.stream.ByteStreamData;
import com.wut.pipeline.WutRequest;

public class ReadTemplateOperation extends TemplateOperation {
	private static final String LINE_BREAK = System.getProperty("line.separator");

	@Override
	public String getName() {
		return "read";
	}
	
	@Override
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryNotInitialized(request)) {
			gitClone(request);
		}

		ByteStreamData fileStream = new ByteStreamData();
		
		String filePath = getInputFilePath(request);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;
		while ((line = reader.readLine()) != null) {
			fileStream.write(line.getBytes("UTF-8"));
			fileStream.write(LINE_BREAK.getBytes("UTF-8"));
		}
		reader.close();
		return fileStream;
	}
}
