package com.wut.resources.file;

import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;

public class ListDirectoryOperation extends BucketOperation {
	private FileOperationHelper fileHelper = new FileOperationHelper();

	@Override
	public String getName() {
		return "list-directory";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		StringData prefix = request.getParameter("prefix", true);
		Data listDirectory = fileHelper.getFileProvider(getS3Account(customer)).listDirectory(getBucket(request), prefix);
		return listDirectory;
	}
}
