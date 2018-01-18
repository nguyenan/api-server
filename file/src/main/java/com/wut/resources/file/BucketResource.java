package com.wut.resources.file;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

public class BucketResource extends AbstractResource {

	public BucketResource() {
		super("bucket");
	}

	private static final long serialVersionUID = -1678486712182811729L;

	@Override
	public String getName() {
		return "bucket";
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new DeleteBucketOperation());
		operations.add(new BackupBucketOperation());
		operations.add(new RestoreBucketOperation());
		operations.add(new ListDirectoryOperation());
		operations.add(new ListFileOperation());
		return operations;
	}
}