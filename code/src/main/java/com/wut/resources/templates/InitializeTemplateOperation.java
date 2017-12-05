package com.wut.resources.templates;

import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
//import com.wut.support.SystemHelper;
import com.wut.support.fileio.WutFile;
import com.wut.support.logging.StackTraceData;

public class InitializeTemplateOperation extends TemplateOperation {

	@Override
	public String getName() {
		return "initialize";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		StringData override = request.getParameter("override", true);
		try {
			if ((new StringData("true")).equals(override)) {
				String clientCodeDirectoryStr = getClientCodeDirectory(request);
				StackTraceData deleteFiles = WutFile.deleteFiles(clientCodeDirectoryStr);
				if (!deleteFiles.isSuccess()) {
					deleteFiles.setWutRequest(request);
					return deleteFiles;
				}
			}

			StackTraceData wasCloneSuccessful = gitClone(request);
			return wasCloneSuccessful;
		} catch (Exception e) {
			return StackTraceData.error(null, null, "Git clone exception occured", request, e);
		}
	}
}
