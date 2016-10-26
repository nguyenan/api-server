package com.wut.threading;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

import com.wut.support.ErrorHandler;
import com.wut.support.SystemHelper;
import com.wut.support.logging.WutLogger;

public class WutSystemCommandProcess extends WutProcess {
	private String command;
	private String[] arguments;
	private PrintStream output;
	private String workingDirectory;
	//private String processId;

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public WutSystemCommandProcess(String command, String[] arguments) {
		super(null); // to be set later in the call method
		this.command = command;
		this.arguments = arguments;
	}

	// TODO create a setOutputFile() method

	public void setOutputStream(OutputStream stream) {
		this.output = new PrintStream(stream);
	}
	
	// TODO deprecate
	public void setPrintStream(PrintStream stream) {
		this.output = stream;
	}

	@Override
	public Integer call() throws Exception {
		File executableFile = new File(command);
		if (!executableFile.exists()) {
			throw new RuntimeException("missing command " + command);
		}
		if (!executableFile.canExecute()) {
			throw new RuntimeException("unable to execute command " + command);
			//Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		}
		
		WutLogger.create(WutSystemCommandProcess.class).info(workingDirectory + ":" + command + ":" + arguments);
		System.out.println(workingDirectory + ":" + command + ":" + arguments);
		this.process = SystemHelper.runProcess(workingDirectory, command, arguments, output);

		try {
			int exitVal = process.waitFor();
			System.out.println("Exited with error code " + exitVal);
			return new Integer(exitVal);
		} catch (InterruptedException e) {
			ErrorHandler.userError("phantom interrupted", e);
			process.destroy();
		}

		return Integer.valueOf(-9999999);
	}

	@Override
	public void kill() {
		super.kill();

		try {
			//PrintStream killOutput = System.out;
			//SystemHelper.runProcess(null, "kill", new String[] { "-9", processId }, killOutput);
		} catch (Exception e) {
			// eat this
		}
	}

	private static final String fallback = "999999999999";

	@Deprecated
	public static String getProcessId() {
		// Note: may fail in some JVM implementations
		// therefore fallback has to be provided

		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf('@');

		if (index < 1) {
			// part before '@' empty (index = 0) / '@' not found (index = -1)
			throw new RuntimeException(
					"This operating environment (os) does not support retreiving pid");
		}

		try {
			return Long.toString(Long.parseLong(jvmName.substring(0, index)));
		} catch (NumberFormatException e) {
			// ignore
		}
		return fallback;
	}

}
