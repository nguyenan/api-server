package com.wut.resources.templates;

import java.io.File;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.support.SystemHelper;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SettingsManager;

public class RefreshTemplateOperation extends TemplateOperation {
	private static WutLogger logger = WutLogger.create(RefreshTemplateOperation.class);
	@Override
	public String getName() {
		return "refresh";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		//String application = request.getApplication();
		String customer = request.getCustomer();
		
		String clientCodeDirectory = SettingsManager.getClientSettings(customer, "client.code.dir");
		
		logger.info("refreshing directory " + clientCodeDirectory);
		File clientCodeDirectoryFile = new File(clientCodeDirectory);
		
		if (clientCodeDirectoryFile.listFiles() == null || clientCodeDirectoryFile.listFiles().length <= 0) {
			return MessageData.error("client templates not initialized " + clientCodeDirectory);
		}
		
		String gitPath = SettingsManager.getSystemSetting("git.path");
		int returnCode = SystemHelper.runCommand(clientCodeDirectory, gitPath, new String[] { "pull" }, null);
		
		return MessageData.returnCode(returnCode);
	}
}
