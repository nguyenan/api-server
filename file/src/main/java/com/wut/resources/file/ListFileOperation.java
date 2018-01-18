package com.wut.resources.file;

import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;

public class ListFileOperation extends BucketOperation {
	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());

	@Override
	public String getName() {
		return "list-file";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		StringData prefix = request.getParameter("prefix", true);
		Data listFile = provider.listFile(getBucket(request), prefix);
		return listFile;
	}
}
