package com.wut.resources.file;

import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;
import com.wut.support.settings.SettingsManager;

public class BackupBucketOperation extends BucketOperation {
	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());

	@Override
	public String getName() {
		return "backup";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customerId = request.getCustomer();
		BooleanData wasSucessful = provider.copyBucket(getBucket(request), new IdData(BACKUP_BUCKET + "/" + customerId));
		return MessageData.successOrFailure(wasSucessful);
	}
}
