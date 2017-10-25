package com.wut.provider.file;

import java.io.InputStream;

import com.wut.datasources.FileSource;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.BinaryStreamData;
import com.wut.model.stream.StreamData;

public class DefaultFileProvider implements FileProvider {
	private FileSource source;

	public DefaultFileProvider(FileSource source) {
		this.source = source;
	}

	public ScalarData read(IdData bucket, IdData folder, StringData filename) {
		String folderStr = folder != null ? folder.toRawString() : null;
		InputStream file = source.getFile(bucket.toRawString(), folderStr, filename.toRawString());
		StreamData data = BinaryStreamData.create(file);
		return data;
	}

	public BooleanData update(IdData bucket, IdData folder, StringData filename, StreamData data) {
		String folderStr = folder != null ? folder.toRawString() : null;
		boolean wasSuccessful = source.updateFile(bucket.toRawString(), folderStr, filename.toRawString(),
				data.getRawStream());
		return BooleanData.create(wasSuccessful);
	}

	public BooleanData delete(IdData bucket, IdData folder, StringData filename) {
		String folderStr = folder != null ? folder.toRawString() : null;
		boolean wasSuccessful = source.deleteFile(bucket.toRawString(), folderStr, filename.toRawString());
		return BooleanData.create(wasSuccessful);
	}

	public BooleanData deleteBucket(IdData bucket) {
		boolean wasSuccessful = source.deleteBucket(bucket.toRawString());
		return BooleanData.create(wasSuccessful);
	}

	public BooleanData copyBucket(IdData sourceBucket, IdData destination) {
		boolean wasSuccessful = source.copyBucket(sourceBucket.toRawString(), destination.toRawString());
		return BooleanData.create(wasSuccessful);
	}
}
