package com.wut.datasources;

import java.io.InputStream;

public interface FileSource extends DataSource {
	
	public InputStream getFile(String bucket, String folder, String filename);

	public boolean updateFile(String bucket, String folder, String filename, InputStream fileData);
	
	public boolean deleteFile(String bucket, String folder, String filename);
	
	public boolean deleteBucket(String bucket);
	
	public boolean copyBucket(String source, String destination);
	
}