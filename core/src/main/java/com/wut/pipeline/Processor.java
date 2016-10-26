package com.wut.pipeline;

public interface Processor {
	// returns true if process worked. pipeline will continue if true, stop if false.
	public boolean process(WutRequest request, WutResponse response);
	
	// TODO get rid of these
	public void preProcess(WutRequest request, WutResponse response);
	public void postProcess(WutRequest request, WutResponse response);
}
