//package com.wut.resources.templates;
//
//import com.wut.model.Data;
//import com.wut.model.map.MessageData;
//import com.wut.model.message.ErrorData;
//import com.wut.pipeline.WutRequest;
//import com.wut.support.fileio.WutFile;
//
//public class CopyTemplateOperation extends TemplateOperation {
//
//	@Override
//	public String getName() {
//		return "copy";
//	}
//
//	@Override
//	public Data perform(WutRequest request) throws Exception {
//		
//		if (isClientInputDirectoryNotInitialized(request)) {
//			return ErrorData.NOT_INITIALIZED;
//		}
//		
//		String origPath = getInputFilePath(request);
//		String destPath = getOutputFilePath(request);
//
//		boolean wasSucessful = WutFile.copyFile(origPath, destPath);
//		
//		return MessageData.successOrFailure(wasSucessful);
//	}
//
//}
