package com.wut.datasources;

import java.io.InputStream;

import com.wut.model.Data;

public interface FileSource extends DataSource {
	
	public InputStream getFile(String bucket, String folder, String filename);

	public boolean updateFile(String bucket, String folder, String filename, InputStream fileData, String customer);
	
	public boolean deleteFile(String bucket, String folder, String filename);
	
	public Data listDirectory(String bucket, String prefix);
	
	public Data listFile(String bucket, String prefix);	
	
	public boolean deleteBucket(String bucket);
	
	public boolean copyBucket(String source, String destination);
	
}