package com.wut.provider.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.wut.datasources.FileSource;
import com.wut.model.Data;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.ByteStreamData;
import com.wut.model.stream.StreamData;
import com.wut.support.ErrorHandler;

public class DefaultFileProvider implements FileProvider {
	private FileSource source;
	private static final String LINE_BREAK = System.getProperty("line.separator");

	public DefaultFileProvider(FileSource source) {
		this.source = source;
	}

	public ScalarData read(IdData bucket, IdData folder, StringData filename) {
		String folderStr = folder != null ? folder.toRawString() : null;
		InputStream file = source.getFile(bucket.toRawString(), folderStr, filename.toRawString());
		if (file == null)
			return null;
		ByteStreamData fileStream = new ByteStreamData();
		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileStream.write(line.getBytes("UTF-8"));
				fileStream.write(LINE_BREAK.getBytes("UTF-8"));
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			ErrorHandler.systemError("Error when getting file " + folderStr + filename.toRawString(), e);
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.systemError("Error when getting file " + folderStr + filename.toRawString(), e);
			e.printStackTrace();
		}
		return fileStream;

	}

	public BooleanData update(IdData bucket, IdData folder, StringData filename, StreamData data) {
		String folderStr = folder != null ? folder.toRawString() : null;
		boolean wasSuccessful = source.updateFile(bucket.toRawString(), folderStr, filename.toRawString(),
				data.getRawStream(), null);
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

	public Data listFile(IdData bucket, StringData prefix) {
		Data listFile = source.listFile(bucket.toRawString(), prefix.toRawString());
		return listFile;
	}

	public Data listDirectory(IdData bucket, StringData prefix) {
		Data listDirectory = source.listDirectory(bucket.toRawString(), prefix.toRawString());
		return listDirectory;
	}
}
