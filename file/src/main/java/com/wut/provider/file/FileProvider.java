package com.wut.provider.file;

import com.wut.model.Data;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.provider.Provider;

public interface FileProvider extends Provider {
	
	public ScalarData read(IdData bucket, IdData folder, StringData id);
	
	public BooleanData update(IdData bucket, IdData folder, StringData file, StreamData data);
	
	public BooleanData delete(IdData bucket, IdData folder, StringData id);
	
	public Data listDirectory(IdData bucket, StringData prefix);
	
	public Data listFile(IdData bucket, StringData prefix);	
	
	public BooleanData deleteBucket(IdData bucket);
	
	public BooleanData copyBucket(IdData source, IdData destination);
	
}
