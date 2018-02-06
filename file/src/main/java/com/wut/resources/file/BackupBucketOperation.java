package com.wut.resources.file;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;

public class BackupBucketOperation extends BucketOperation {
	private FileOperationHelper fileHelper = new FileOperationHelper();

	@Override
	public String getName() {
		return "backup";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		String backupS3 = String.format(BACKUP_BUCKET, getS3Account(customer));
		BooleanData wasSucessful = fileHelper.getFileProvider(getS3Account(customer)).copyBucket(getBucket(request),
				new IdData(backupS3 + "/" + customer));
		if (BooleanData.TRUE.equals(wasSucessful))
			fileHelper.getFileProvider(getS3Account(customer)).deleteBucket(getBucket(request));
		return MessageData.successOrFailure(wasSucessful);
	}
}
