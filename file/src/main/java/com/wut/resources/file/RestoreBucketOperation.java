package com.wut.resources.file;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;

public class RestoreBucketOperation extends BucketOperation {
	private FileOperationHelper fileHelper = new FileOperationHelper();

	@Override
	public String getName() {
		return "restore";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		String backupS3 = String.format(BACKUP_BUCKET, getS3Account(customer));
		BooleanData wasSucessful = fileHelper.getFileProvider(getS3Account(customer))
				.copyBucket(new IdData(backupS3 + "/" + customer), getBucket(request));
		return MessageData.successOrFailure(wasSucessful);
	}
}
