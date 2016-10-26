package com.wut.provider.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.derby.impl.tools.sysinfo.Main;

import com.wut.datasources.FileSource;
import com.wut.datasources.aws.S3FileSource;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.BinaryStreamData;
import com.wut.model.stream.StreamData;
import com.wut.support.ErrorHandler;
import com.wut.support.logging.WutLogger;

public class DefaultSettingProvider implements SettingProvider {
	private FileSource source;
	private StringData folder = new StringData("settings");
	private StringData bucket = new StringData("www.tend.ag");
	private StringData filename = new StringData("settings.properties");
	
	public DefaultSettingProvider(FileSource source) {
		this.source = source;
	}
	
	
//	@Override
//	public ScalarData read(StringData setting) {
//		String folderStr = folder != null ? folder.toRawString() : null;
//		InputStream file = source.getFile(bucket.toRawString(), folderStr, filename.toRawString());
//		StreamData data = BinaryStreamData.create(file);
//		return data;
//	}
//
//	@Override
//	public BooleanData update(StringData setting) {
//		String folderStr = folder != null ? folder.toRawString() : null;
//		boolean success = source.updateFile(bucket.toRawString(), folderStr, filename.toRawString(), null); // data.getRawStream());
//		return BooleanData.create(success);
//	}
	
	// TODO make initialize method which just reads settings and puts them into "core" space
	

	@Override
	public ScalarData read(IdData customer, StringData settingName) {
		StringData settingValueData = null;
		try {
			InputStream settingsFile = source.getFile(customer.toRawString(), folder.toRawString(), filename.toRawString()); 
			Properties props = new Properties();
			props.load(settingsFile);
			String settingValue = props.getProperty(settingName.toRawString());
			settingValueData = new StringData(settingValue);
		} catch (IOException e) {
			ErrorHandler.fatalError(e, "unable to load settings file");
		}
		return settingValueData;
	}

	@Override
	public BooleanData update(IdData customer, StringData setting) {
		
		// read properties file
		
		// TURN file into java properties object
		
		// update particular setting
		
		// update file in s3 using updateFile()
		
		boolean success = source.updateFile(customer.toRawString(), null, filename.toRawString(), null); // data.getRawStream());

		
		return BooleanData.create(success);
	}
	
	
	
	// FOR LATER
	public BooleanData delete(IdData bucket, IdData folder, StringData filename) {
		String folderStr = folder != null ? folder.toRawString() : null;
		boolean wasSuccess = source.deleteFile(bucket.toRawString(), folderStr, filename.toRawString());
		return BooleanData.create(wasSuccess);
	}
	
	public static void main(String[] args) {
		
		S3FileSource source = new S3FileSource();
		DefaultSettingProvider provider = new DefaultSettingProvider(source);

		provider.`
	}


}
