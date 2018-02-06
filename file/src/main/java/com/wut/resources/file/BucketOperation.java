package com.wut.resources.file;

import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;
import com.wut.support.settings.SettingsManager;

public abstract class BucketOperation extends AbstractOperation {
	
	protected static final String BACKUP_BUCKET = SettingsManager.getSystemSetting("file.backupbucket"); 
	
	public BucketOperation() {
		super("override getName()");
	}

	protected IdData getBucket(WutRequest request) {
		String customerDomain = SettingsManager.getClientSettings(request.getCustomer(), "file.domain");
		return new IdData(customerDomain);
	}
	protected String getS3Account(String customer){ 
		String s3Account = SettingsManager.getClientSettings(customer, "file.s3-account");
		if (s3Account.isEmpty())
			s3Account = SettingsManager.getAdminSettings("file.default-s3-account");
		
		return s3Account;
	}
}
