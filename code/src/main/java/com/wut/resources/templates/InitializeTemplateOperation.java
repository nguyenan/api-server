package com.wut.resources.templates;

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
		if ((new StringData("true")).equals(override)) {
			String clientCodeDirectoryStr = getClientCodeDirectory(request);
			boolean deleteSuccessful = WutFile.deleteFiles(clientCodeDirectoryStr);
			if (!deleteSuccessful)
				return MessageData.FAILURE;
		}
		
		boolean wasCloneSuccessful = gitClone(request);
		return MessageData.successOrFailure(wasCloneSuccessful);
	}
}
