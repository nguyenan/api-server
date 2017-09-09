package com.wut.resources.file;

import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;

public class DeleteBucketOperation extends BucketOperation {

	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		BooleanData wasSucessful = provider.deleteBucket(getBucket(request));
		return MessageData.successOrFailure(wasSucessful);
	}
}
