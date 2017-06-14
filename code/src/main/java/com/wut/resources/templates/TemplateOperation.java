package com.wut.resources.templates;

import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintStream;
import java.io.IOException;
import java.net.URL;

import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.Defaults;
import com.wut.support.StringHelper;
import com.wut.support.SystemHelper;
import com.wut.support.fileio.WutFile;
import com.wut.support.settings.SettingsManager;

public abstract class TemplateOperation extends AbstractOperation {
	
	public TemplateOperation() {
		super("override getName()");
	}

	protected boolean isClientInputDirectoryInitialized(WutRequest request) {
		String clientCodeDirectory = getClientCodeDirectory(request);
		File clientCodeDirectoryFile = new File(clientCodeDirectory);
		return clientCodeDirectoryFile.listFiles() != null && clientCodeDirectoryFile.listFiles().length <= 0;
	}
	
	
	protected String getClientCodeDirectory(WutRequest request) {
		String customerId = request.getCustomer();
		String domain = SettingsManager.getClientSettings(customerId, "template.domain");
		return SettingsManager.getSystemSetting("code.dir") + domain;
	}
	
	protected String getClientSiteDirectory(String customerId) {
		String domain = SettingsManager.getClientSettings(customerId, "template.domain");
		return SettingsManager.getSystemSetting("site.dir") + domain;
	}
	
	// TODO rename pull out common code with getOutputDirectory()
	private String getInputDirectory(WutRequest request) throws MissingParameterException {		
		StringBuilder inputFolder = new StringBuilder();
		
//		String customer = request.getCustomer();
		String clientCodeDirectory = getClientCodeDirectory(request);
		inputFolder.append(clientCodeDirectory);
		inputFolder.append("/");
		
		String application = request.getApplication();
		if (Defaults.isDefaultApplication(application)) {
			application = ".";
		} else if (application.contains("/") || application.contains(".")) {
			throw new RuntimeException("invalid argument passed for application");
		}
		inputFolder.append(application);

		StringData inputBucket = request.getParameter("bucket", true);
		if (inputBucket != null) {
			String bucketString = inputBucket.toRawString();
			if (bucketString.contains("/") || bucketString.contains(".")) {
				throw new RuntimeException("invalid argument passed for outputBucket");
			}
			inputFolder.append("/");
			inputFolder.append(bucketString);
			//WutFile.createFolderIfNeeded(inputFolder.toString());
		}
		
		return inputFolder.toString();
	}
	
	protected boolean gitClone(WutRequest request) {
		try {
			String customer = request.getCustomer();
			String clientCodeDirectory = getClientCodeDirectory(request);
			File clientCodeDirectoryFile = new File(clientCodeDirectory);
			
			String gitPath = getGitPath();
			if (!clientCodeDirectoryFile.exists())
				clientCodeDirectoryFile.mkdirs();
			if (clientCodeDirectoryFile.listFiles().length <= 0) {
				String gitUrl = SettingsManager.getClientSettings(customer, "template.git.repository");
				String gitBranch = SettingsManager.getClientSettings(customer, "template.git.branch");
				SystemHelper.runCommand(clientCodeDirectory, gitPath, new String[] { "clone", "-b", gitBranch, gitUrl, "."}, null);
				return true;
			}
			
		} catch (Exception e) {
			System.out.println("Git clone exception occured");
		}
		
		System.out.println("Git directory already has files");
		return false;
			
	}

	private String getGitPath() {
		String gitPath = SettingsManager.getSystemSetting("git.path");
		return gitPath;
	}
	
//	private void gitPull(WutRequest request) {
//		String clientCodeDirectory = getClientCodeDirectory(request);
//		String gitPath = getGitPath();
//		SystemHelper.runCommand(clientCodeDirectory, gitPath, new String[] { "pull" }, null);
//	}
	
	private String getOutputDirectory(WutRequest request) throws MissingParameterException {
		StringBuilder outputFolder = new StringBuilder();
		
		String customerId = request.getCustomer();
		String siteFolder = getClientSiteDirectory(customerId);
		outputFolder.append(siteFolder);
		outputFolder.append("/");
		
		String application = request.getApplication();
		if (application.equals("wut") || application.equals("storefront")) {
			application = ".";
		}
		outputFolder.append(application);
		WutFile.createFolderIfNeeded(outputFolder.toString());

		StringData outputBucket = request.getParameter("outputBucket", true);
		if (outputBucket != null) {
			String bucketString = outputBucket.toRawString();
			if (bucketString.contains("/") || bucketString.contains(".")) {
				throw new RuntimeException("invalid argument passed for outputBucket");
			}
			outputFolder.append("/");
			outputFolder.append(bucketString);
			WutFile.createFolderIfNeeded(outputFolder.toString());
		}
		
		return outputFolder.toString();
	}
	
	protected String getOutputFilePath(WutRequest request) throws MissingParameterException {
		String outputFolder = getOutputDirectory(request);
		String pageName = request.getStringParameter("output");
		String pageLocation = outputFolder + "/" + pageName ;
		return pageLocation;
	}
	
	protected String getInputFilePath(WutRequest request) throws MissingParameterException {
		String inputFolder = getInputDirectory(request);
		String templateName = request.getStringParameter("id"); // input
		String pageLocation = inputFolder + "/" + templateName;
		return pageLocation;
	}
	
	protected String[] getPhantomParameters(WutRequest request) throws MissingParameterException {
		String renderJsLocation = getRenderJsPath();

		String templateLocation = getInputFilePath(request);
		boolean doesTemplateExist = WutFile.exists(templateLocation);
		if (!doesTemplateExist) {
			throw new RuntimeException("input folder not found");
		}
		String urlParameters = getUrlParametersString(request);
		String templateLocationWithParams = templateLocation + (urlParameters != null ? "?" + getUrlParametersString(request) : "");
		
		StringData showConsoleOutputInsteadOfPage = request.getParameter("console", true);
		
		final String ignoreSSLArgument = "--ignore-ssl-errors=yes";
		
		final String noImages = "--load-images=false";
		
		String[] arguments;
		if (showConsoleOutputInsteadOfPage != null) {
			String consoleOutput = showConsoleOutputInsteadOfPage.toRawString();
			arguments = new String[] { ignoreSSLArgument, noImages, renderJsLocation, templateLocationWithParams, consoleOutput };
		} else {
			arguments = new String[] { ignoreSSLArgument, noImages, renderJsLocation, templateLocationWithParams };
		}
		return arguments;
	}

	protected String getUrlParametersString(WutRequest request)
			throws MissingParameterException {
		MappedData dataParameter = request.getParameter("parameters", true); // parameters
		if (dataParameter != null) {
			String urlParametersString = dataParameter.toUrlParameters();
			return urlParametersString;
		} else {
			return null;
		}
	}

	// TODO make not public
	protected static String getPhantomJsPath() {
		String current;
		try {
			current = new java.io.File( "." ).getCanonicalPath();
			System.out.println("Current dir:"+current);
			
			System.out.println(System.getenv("CATALINA_BASE"));
			System.out.println(System.getenv("CATALINA_HOME"));

			System.out.println("home:"+System.getProperty("catalina.home"));
			System.out.println("base:"+System.getProperty("catalina.base"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String phantomJsLocation = SettingsManager.getToolLocation("phantomjs");
		return phantomJsLocation;
	}

	// TODO make not public
	public static String getRenderJsPath() {
		//String basePath = getToolsBasePath();
		String renderJsLocation = SettingsManager.getToolUtility("phantomjs", "render.js");
		return renderJsLocation;
	}
	
	
	private static String getToolsBasePath() {
		URL path = TemplateResource.class.getResource(".");
                //.getClassLoader().getResource(".");
		String pathStr = StringHelper.removePrefixIfPresent("file:", path.toString());
		return pathStr + "../webapps/template";
	}

}
