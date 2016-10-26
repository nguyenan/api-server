package com.wut.resources.templates;

//import java.io.File;
//import java.io.IOException;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
//import com.wut.support.SystemHelper;
import com.wut.support.fileio.WutFile;

public class InitializeTemplateOperation extends TemplateOperation {

	@Override
	public String getName() {
		return "initialize";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		StringData override = request.getParameter("override", true);
		if (override != null && override.equals(new StringData("true"))) {
			String clientCodeDirectoryStr = getClientCodeDirectory(request);
			boolean deleteSuccessful = WutFile.deleteFiles(clientCodeDirectoryStr);
			return MessageData.successOrFailure(deleteSuccessful);
		}
		
		boolean wasCloneSuccessful = gitClone(request);
		return MessageData.successOrFailure(wasCloneSuccessful);
	}

}
