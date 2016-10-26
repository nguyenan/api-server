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
	
	
//	@Override
//	public Data perform(WutRequest request) throws Exception {
//		//String application = request.getApplication();
//		String customer = request.getCustomer();
//		
//		String clientCodeDirectory = SettingsManager.getCustomerSettings(customer, "client.code.dir");
//		
//		//File clientCodeDirectoryFile = new File(clientCodeDirectory);
//		
//		String gitPath = SettingsManager.getSystemSetting("git.path");
//
//		if (isClientInputDirectoryInitialized(clientCodeDirectory)) {
//			return MessageData.error("client tempaltes not initialized");
//		}
//		
//		// generate into folder // data / sites / <client> / <application>
//		String templateName = request.getStringParameter("input"); // input
//		String pageName = request.getStringParameter("output"); // output
//		MappedData dataParameter = request.getParameter("parameters", true); // parameters
//		String urlParametersString = dataParameter.toUrlParameters();
//		
//		String templateLocation = clientCodeDirectory + "/" + templateName + "?" + urlParametersString;
//		
//		String renderJsLocation = SettingsManager.getToolUtility("phantomjs", "render.js");
//
//		String outputFolder = SettingsManager.getCustomerSettings(customer, "client.site.dir");
//		
//		String pageLocation = outputFolder + "/" + pageName ;
//		File page = new File(pageLocation);
//		PrintStream pageStream = new PrintStream(page);
//		
//		String[] arguments = new String[] { renderJsLocation, templateLocation };
//		
//		String phantomJsLocation = SettingsManager.getToolLocation("phantomjs");
//		SystemHelper.runCommand(null, phantomJsLocation, arguments, pageStream);
//		
//		// 3. UPLOAD TEMPLATES TO APPROPRIATE LOCATIONS (Web Server & CDN)
//		
//		return MessageData.success();
//		
//		// when jetty loads, it will get client's storefront folder from
//		// data / sites / <client> / storefront
//	}



}
