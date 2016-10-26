package com.wut.resources.templates;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.WutRequest;
import com.wut.support.fileio.WutFile;

public class CopyTemplateOperation extends TemplateOperation {

	@Override
	public String getName() {
		return "copy";
	}
	
//	protected void copy(WutRequest request) throws MissingParameterException {
//		
//		
//		//String renderJsLocation = "/Users/russell/git/webutilitykit/WutTools/mac/osx64/phantomjs/render.js"; // SettingsManager.getSystemSetting("renderjs.location"); //"render.js";
//		//String outputFolder = getOutputDirectory(request);
//		
//		//String sitesDir = "/Users/russell/git/data" + "/sites"; //SettingsManager.getSystemSetting("sites.dir");
//		//String clientSitesDir = sitesDir + "/" + customer;
//		
//		String pageLocation = getOutputFilePath(request);
//		File page = new File(pageLocation);
//		PrintStream pageStream;
//		try {
//			pageStream = new PrintStream(page);
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException("Unable to open destination file");
//		}
//		
//		String templateLocation = getInputFilePath(request);
//		boolean doesTemplateExist = WutFile.exists(templateLocation);
//		if (!doesTemplateExist) {
//			throw new RuntimeException("input folder not found");
//		}
//		String templateLocationWithParams = templateLocation + "?" + getUrlParametersString(request);
//		String[] arguments = new String[] { renderJsLocation, templateLocationWithParams };
//		
//		String phantomJsLocation = getPhantomJsPath();
//		SystemHelper.runCommand(null, phantomJsLocation, arguments, pageStream);
//	}

	@Override
	public Data perform(WutRequest request) throws Exception {
//		String customer = request.getCustomer();
//		
//		String clientCodeDirectory = SettingsManager.getCustomerSettings(customer, "client.code.dir");
//		
//		File clientCodeDirectoryFile = new File(clientCodeDirectory);
//		
//		if (clientCodeDirectoryFile.listFiles().length <= 0) {
//			return MessageData.error("client tempaltes not initialized");
//		}
//		
//		String templateName = request.getStringParameter("input");
//		String pageName = request.getStringParameter("output");
//		
//		String templateLocation = clientCodeDirectory + "/" + templateName;
//
//		String outputFolder = SettingsManager.getCustomerSettings(customer, "client.site.dir");
//		String pageLocation = outputFolder + "/" + pageName ;
//		
//		boolean wasSucessful = WutFile.copyFile(templateLocation, pageLocation);
//		
//		return MessageData.successOrFailure(wasSucessful);
		
		if (isClientInputDirectoryInitialized(request)) {
			return ErrorData.NOT_INITIALIZED;
		}
		
		String origPath = getInputFilePath(request);
		String destPath = getOutputFilePath(request);

		boolean wasSucessful = WutFile.copyFile(origPath, destPath);
		
		return MessageData.successOrFailure(wasSucessful);
	}

}
