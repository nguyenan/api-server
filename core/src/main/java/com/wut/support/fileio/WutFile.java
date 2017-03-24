package com.wut.support.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.wut.support.ErrorHandler;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SystemSettings;

public class WutFile {
	private BufferedWriter out;
	private static WutLogger logger = WutLogger.create(WutFile.class);
	private WutFile(String customer, String filePlusPath) {
		try {
			// TODO do this for each customer at system startup instead of on each file creation
			String dataDir = SystemSettings.getInstance().getSetting("data.dir");
			String fullPath;
			if (customer != null) {
				fullPath = dataDir + File.separator + customer;				
			} else {
				fullPath = dataDir;
			}
			File f = new File(fullPath);
			@SuppressWarnings("unused")
			boolean successfullyMadeDirectories = f.mkdirs();
//			if (!successfullyMadeDirectories) {
//				throw new RuntimeException("unable to create directories for path " + fullPath);
//			}
			String fileFullPath = fullPath + File.separator + filePlusPath;
			File thisFile = new File(fileFullPath);
			if (!thisFile.exists()) {
				boolean created = thisFile.createNewFile();
				if (!created) {
					throw new RuntimeException("unable to create file " + fileFullPath);
				}
			}
			this.out = new BufferedWriter(new FileWriter(fileFullPath, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		BufferedWriter out = new BufferedWriter(new FileWriter("./html/requests.xml",true));
//		out.write(request.toXMLString());
//		out.close();
	}
	
	// TODO change name to get() since file isn't always created it's misleading
//	public static WutFile create(String path) {
//		return new WutFile(path);
//	}
	
	public static WutFile create(String customer, String filePlusPath) {
		return new WutFile(customer, filePlusPath);
	}
	
	public static WutFile create(String filePlusPath) {
		return new WutFile(null, filePlusPath);
	}
	
//	public void open() {
//		
//	}
	
	public void write(String text) {
		write(text.toCharArray());
	}

	public void write(char[] text) {
		try {
			out.write(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(int c) {
		try {
			out.write(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean copyFile(String fromPath, String toPath) {
//		File originalFile = new File(fromPath);
//		File destinationFile = new File(toPath);
		throw new RuntimeException("copy not supported");
		/*
		try {
			FileUtils.copyFile(originalFile, destinationFile);
			return true;
		} catch (IOException e) {
			ErrorHandler.systemError("error copying file from" + fromPath + " to " + toPath);
			return false;
		}
		*/
	}

	public static void createFolderIfNeeded(String path) {
		File dir = new File(path);
		
		try {
			if (!dir.exists()) {
				logger.info("# creating directory " + path);
				boolean directoryCreated = dir.mkdirs();
				if (!directoryCreated) {
					logger.fatal("error creating folder for path " + path);
				}
			} else if (!dir.isDirectory()) {
				logger.fatal("unable to create folder " + path + " because file is present");
			}
		} catch (Exception e) {
			logger.fatal("error creating folder for path " + path);
		}
	}

	public static boolean exists(String path) {
		File dir = new File(path);
		return dir.exists();
	}

	public static boolean deleteFiles(String directoryPathStr) {
		try {
			File directory = new File(directoryPathStr);
			if (!directoryPathStr.startsWith(SystemSettings.getInstance().getSetting("code.dir"))){
				//prevent delete unwanted files
				ErrorHandler.systemError("Trying to delete: '" + directoryPathStr +"'. Rejected!");
				return false;
			}	
			File[] fList = directory.listFiles();
			
	        for (File file : fList){
	            if (file.isFile()) {
	            	logger.info("deleting: " + file.getName());
	                if (!file.delete()){
	                	ErrorHandler.systemError("error deleting file: " + file.getPath());
	                	return false;
	                }
	            } else if (file.isDirectory()) {
	            	logger.info(file.getPath());
	            	logger.info("nested directory: " + file.getName());
	            	String directoryPath  = file.getAbsolutePath();
	            	deleteFiles(directoryPath);
	            	if (!file.delete()){
	            		ErrorHandler.systemError("error deleting file: " + file.getPath());
	                	return false;
	                }
	            }
	        }
	        if (!directory.delete()){
	        	ErrorHandler.systemError("error deleting directory: " + directory.getPath());
            	return false;
            }
	        return true;
		} catch (Exception e) {
			ErrorHandler.systemError("error deleting directory: " + directoryPathStr, e);
			return false;
		}
	}
	
}
