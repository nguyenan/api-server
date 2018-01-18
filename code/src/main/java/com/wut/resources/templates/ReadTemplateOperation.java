package com.wut.resources.templates;

import java.io.BufferedReader;
import java.io.FileReader;
import com.wut.model.Data;
import com.wut.model.stream.ByteStreamData;
import com.wut.pipeline.WutRequest;
import com.wut.support.fileio.WutFile;
import com.wut.support.logging.StackTraceData;

public class ReadTemplateOperation extends TemplateOperation {
	private static final String LINE_BREAK = System.getProperty("line.separator");

	@Override
	public String getName() {
		return "read";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		try {
			if (isClientInputDirectoryNotInitialized(request)) {
				StackTraceData wasCloneSuccessful = gitClone(request);
				if (!wasCloneSuccessful.isSuccess())
					return wasCloneSuccessful;
			}

			ByteStreamData fileStream = new ByteStreamData();

			String templateLocation = getInputFilePath(request);
			boolean doesTemplateExist = WutFile.exists(templateLocation);
			if (!doesTemplateExist) {
				return StackTraceData.errorMsg("File not found: " + templateLocation);
			}
			BufferedReader reader = new BufferedReader(new FileReader(templateLocation));
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileStream.write(line.getBytes("UTF-8"));
				fileStream.write(LINE_BREAK.getBytes("UTF-8"));
			}
			reader.close();
			return fileStream;
		} catch (Exception e) {
			return StackTraceData.error(null, null, "ReadTemplateOperation exception occured",request, e);
		}
	}
}
