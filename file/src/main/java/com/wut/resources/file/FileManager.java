package com.wut.resources.file;

import com.wut.datasources.aws.S3FileSource;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;

// TODO rename all "Managers" to "Factory" as that's the real name of this pattern
public class FileManager {
	public FileProvider getFileProcessor(String s3Account) {
		if (!s3Account.isEmpty())
			return new DefaultFileProvider(new S3FileSource(s3Account));

		throw new RuntimeException("no File provider found under name " + s3Account);
	}
}
