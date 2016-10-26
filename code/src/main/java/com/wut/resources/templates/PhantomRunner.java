package com.wut.resources.templates;

import java.io.PrintStream;
import java.util.concurrent.Callable;

import com.wut.support.SystemHelper;

public class PhantomRunner implements Callable<Process> {
	String renderJsLocation = TemplateOperation.getRenderJsPath();
	String phantomJsLocation = TemplateOperation.getPhantomJsPath();
	private PrintStream pageStream;
	private String[] arguments;
	
	public PhantomRunner(String[] arguments, PrintStream pageStream) {
		this.arguments = arguments;
		this.pageStream = pageStream;
	}

	@Override
	public Process call() {
		Process phantomProcess = SystemHelper.runProcess(null, phantomJsLocation, arguments, pageStream);

		try {
			int exitVal = phantomProcess.waitFor();
			System.out.println("Exited with error code "+exitVal);
		} catch (InterruptedException e) {
			phantomProcess.destroy();
		}
        
		return phantomProcess;
	}

}
