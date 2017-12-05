package com.wut.resources.templates;

import java.io.File;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.support.SystemHelper;
import com.wut.support.logging.StackTraceData;
import com.wut.support.settings.SettingsManager;

public class RefreshTemplateOperation extends TemplateOperation {
	@Override
	public String getName() {
		return "refresh";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		try {
			String clientCodeDirectory = getClientCodeDirectory(request);
			File clientCodeDirectoryFile = new File(clientCodeDirectory);

			if (clientCodeDirectoryFile.listFiles() == null || clientCodeDirectoryFile.listFiles().length <= 0) {
				return StackTraceData.errorMsg("client templates not initialized " + clientCodeDirectory);
			}

			String gitPath = SettingsManager.getSystemSetting("git.path");
			int returnCode = SystemHelper.runCommand(clientCodeDirectory, gitPath, new String[] { "pull" }, null);

			return StackTraceData.returnCode(returnCode);
		} catch (Exception e) {
			return StackTraceData.error(null, null, "RefreshTemplateOperation exception occured", request,e);
		}
	}
}
