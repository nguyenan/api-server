package com.wut.resources.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.Base64StreamData;
import com.wut.model.stream.BinaryStreamData;
import com.wut.model.stream.ByteStreamData;
import com.wut.model.stream.StreamData;
import com.wut.pipeline.WutRequest;

public class ReadTemplateOperation extends TemplateOperation {
	private static final String LINE_BREAK = System.getProperty("line.separator");

	@Override
	public String getName() {
		return "read";
	}
	
	@Override
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryInitialized(request)) {
			return MessageData.error("client tempaltes not initialized"); // TODO give this a proper error message (fix everywhere)
		}

		ByteStreamData fileStream = new ByteStreamData();
		
		String filePath = getInputFilePath(request);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;
		while ((line = reader.readLine()) != null) {
			fileStream.write(line.getBytes("UTF-8"));
			fileStream.write(LINE_BREAK.getBytes("UTF-8"));
		}
		reader.close();
		return fileStream;
	}
	/*
	
	@Override
	//BufferedReader bufReader = new BufferedRe
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryInitialized(request)) {
			return MessageData.error("client tempaltes not initialized"); // TODO give this a proper error message (fix everywhere)
		}

		String filePath = getInputFilePath(request);
		InputStream fileInputStream = new FileInputStream(filePath);
		
		StreamData data = Base64StreamData.create(fileInputStream);
		StringData strData = new StringData(data.toRawString());
		
		return data;
	}
*/
	
	/*
	@Override
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryInitialized(request)) {
			return MessageData.error("client tempaltes not initialized"); // TODO give this a proper error message (fix everywhere)
		}

		ByteStreamData fileStream = new ByteStreamData();
		
		String filePath = getInputFilePath(request);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		
		String line = null;
		while ((line = reader.readLine()) != null) {
			fileStream.write(line.getBytes("UTF-8"));
			fileStream.write(LINE_BREAK.getBytes("UTF-8"));
		}
		reader.close();
		return fileStream;
		
//		
//		String str = "";
//		char[] chars = new char[1000];
//		try {
//			BufferedReader bufReader = new BufferedReader(new FileReader(new File(OUTPUT_FILE)),4096)
//			while( (bufReader.read(chars)) != -1 ) {
//		
//		
//		               String chunk = new String(chars);
//		
//		               //alternative
//		
//		              // String chunk = String.valueOf(chars)
//		
//		 
//		
//		               System.out.println(chunk );
//		
//		           }
//		
//		 
//		
//		        } catch (IOException e) {
//		
//		            e.printStackTrace();
//		
//		        }

	}
	*/
}
