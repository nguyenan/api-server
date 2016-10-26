package com.wut.threading;

import java.util.concurrent.Callable;

//import com.wut.support.SystemHelper;

public abstract class WutProcess implements Callable<Integer> {
	protected Process process;

	public WutProcess(Process p) {
		this.process = p;
	}

//	@Override
//	public Integer call() throws Exception {
//		process = SystemHelper.runProcess(null, phantomJsLocation, arguments, pageStream);
//
//		try {
//			int exitVal = phantomProcess.waitFor();
//			System.out.println("Exited with error code "+exitVal);
//		} catch (InterruptedException e) {
//			phantomProcess.destroy();
//		}
//        
//		return new Integer(exitVal);
//	}
	
	public void kill() {
		if (process != null) {
			process.destroy();
		}
	}
	
	
}
