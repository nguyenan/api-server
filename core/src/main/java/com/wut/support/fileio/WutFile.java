package com.wut.support.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.wut.support.ErrorHandler;
import com.wut.support.logging.StackTraceData;
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
			e.printStackTrace();
		}
	}

	public static WutFile create(String customer, String filePlusPath) {
		return new WutFile(customer, filePlusPath);
	}

	public static WutFile create(String filePlusPath) {
		return new WutFile(null, filePlusPath);
	}

	public void write(String text) {
		write(text.toCharArray());
	}

	public void write(char[] text) {
		try {
			out.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(int c) {
		try {
			out.write(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean copyFile(String fromPath, String toPath) {
		throw new RuntimeException("copy not supported");
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

	public static StackTraceData deleteFiles(String directoryPathStr) {
		try {
			StackTraceData deleteFiles = new StackTraceData("");
			File directory = new File(directoryPathStr);
			if (!directory.exists())
				return StackTraceData.success();

			if (!directoryPathStr.startsWith(SystemSettings.getInstance().getSetting("code.dir")))
				return StackTraceData.errorMsg("Trying to delete: '" + directoryPathStr + "'. Rejected!");

			File[] fList = directory.listFiles();
			if (fList != null && fList.length > 0) {
				for (File file : fList) {
					if (file.isFile()) {
						if (!file.delete())
							return StackTraceData.errorMsg("error deleting file: " + file.getPath());
					} else if (file.isDirectory()) {
						logger.info("nested directory: " + file.getName());
						String directoryPath = file.getAbsolutePath();
						deleteFiles = deleteFiles(directoryPath);
						if (!deleteFiles.isSuccess()) {
							return deleteFiles;
						}
					}
				}
			}
			if (!directory.delete()) {
				return StackTraceData.errorMsg("error deleting directory: " + directory.getPath());
			}
			return StackTraceData.success();
		} catch (Exception e) {
			ErrorHandler.systemError("error deleting directory: " + directoryPathStr, e);
			return StackTraceData.error(null, null, "error deleting directory: " + directoryPathStr, null, e);
		}
	}
}
