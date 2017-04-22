package com.wut.provider.file;

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
	
	public BooleanData deleteBucket(IdData bucket);
	
}
