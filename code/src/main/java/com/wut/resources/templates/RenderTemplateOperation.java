package com.wut.resources.templates;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.ByteStreamData;
import com.wut.model.stream.OutputStreamAdapter;
import com.wut.pipeline.WutRequest;
import com.wut.support.fileio.WutFile;
import com.wut.threading.WutProcessExecuter;
import com.wut.threading.WutSystemCommandProcess;

public class RenderTemplateOperation extends TemplateOperation {

	//private static final String QUOTE = "\"";

	@Override
	public String getName() {
		return "render";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		if (isClientInputDirectoryNotInitialized(request)) {
			gitClone(request);
		}
		String renderJsLocation = getRenderJsPath();
		int returnCode = 0;
		OutputStreamAdapter output = new OutputStreamAdapter(new ByteStreamData());
		
		try {
			String templateLocation = getInputFilePath(request);
			boolean doesTemplateExist = WutFile.exists(templateLocation);
			if (!doesTemplateExist) {
				throw new RuntimeException("input folder not found");
			}
			String urlParameters = getUrlParametersString(request);
			String templateLocationWithParams = "file://" + templateLocation + (urlParameters != null ? "?" + getUrlParametersString(request) : "");
			
			StringData showConsoleOutputInsteadOfPage = request.getParameter("console", true);
			
			final String ignoreSSLArgument = "--ignore-ssl-errors=yes";
			
			final String noImages = "--load-images=false";
			
			final String sslType = "--ssl-protocol=tlsv1";
			
			final String encoding = "--output-encoding=utf8";
			
			final String allowCors = "--web-security=false";
			
			String[] arguments;
			if (showConsoleOutputInsteadOfPage != null) {
				String consoleOutput = showConsoleOutputInsteadOfPage.toRawString();
				arguments = new String[] { ignoreSSLArgument, noImages, sslType, encoding, allowCors, renderJsLocation, templateLocationWithParams, consoleOutput };
			} else {
				arguments = new String[] { ignoreSSLArgument, noImages, sslType, encoding, allowCors, renderJsLocation, templateLocationWithParams };
			}
			
			String phantomJsLocation = getPhantomJsPath();
			//String outputDirectory = getOutputDirectory(request);
			//int returnCode = SystemHelper.runCommand(null, phantomJsLocation, arguments, pageStream);
			//int returnCode = PhantomExecuter.execute(arguments, pageStream);
			
			WutSystemCommandProcess phantomJs = new WutSystemCommandProcess(phantomJsLocation, arguments);
			phantomJs.setOutputStream(output);
			
			returnCode = WutProcessExecuter.execute(phantomJs);
			
		} finally {
			output.close();
		}
		
		if (returnCode == 0) {
			return output.getByteStream();
		} else {
			return MessageData.returnCode(returnCode);
		}
	}
}
