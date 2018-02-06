package com.wut.resources.file;

import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;

public class ListFileOperation extends BucketOperation {
	private FileOperationHelper fileHelper = new FileOperationHelper();

	@Override
	public String getName() {
		return "list-file";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		StringData prefix = request.getParameter("prefix", true);
		Data listFile = fileHelper.getFileProvider(getS3Account(customer)).listFile(getBucket(request), prefix);
		return listFile;
	}
}
