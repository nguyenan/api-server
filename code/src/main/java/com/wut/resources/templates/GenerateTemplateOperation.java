package com.wut.resources.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.threading.WutProcessExecuter;
import com.wut.threading.WutSystemCommandProcess;

public class GenerateTemplateOperation extends TemplateOperation {

	@Override
	public String getName() {
		return "generate";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryInitialized(request)) {
			return MessageData.error("client tempaltes not initialized");
		}
		
		String pageLocation = getOutputFilePath(request);
		
		File page = new File(pageLocation);
		PrintStream pageStream;
		try {
			pageStream = new PrintStream(page);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to open destination file");
		}
		
		String[] arguments = getPhantomParameters(request);
		
		String phantomJsLocation = getPhantomJsPath();
		
		WutSystemCommandProcess phantomJs = new WutSystemCommandProcess(phantomJsLocation, arguments);
		phantomJs.setOutputStream(pageStream);
		
		int returnCode = WutProcessExecuter.execute(phantomJs);
		
		return MessageData.returnCode(returnCode); 
	}
}
