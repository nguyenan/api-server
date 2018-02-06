package com.wut.resources.file;

import com.wut.provider.file.FileProvider;

public class FileOperationHelper {

	protected FileProvider getFileProvider(String s3Account) {
		FileManager fileManager = new FileManager();
		FileProvider fileProvider = fileManager.getFileProcessor(s3Account);
		return fileProvider;
	}
	
}