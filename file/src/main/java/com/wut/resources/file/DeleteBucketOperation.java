package com.wut.resources.file;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.pipeline.WutRequest;

public class DeleteBucketOperation extends BucketOperation {
 
	private FileOperationHelper fileHelper = new FileOperationHelper();
	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		BooleanData wasSucessful = fileHelper.getFileProvider(getS3Account(customer)).deleteBucket(getBucket(request));
		return MessageData.successOrFailure(wasSucessful);
	}
}
