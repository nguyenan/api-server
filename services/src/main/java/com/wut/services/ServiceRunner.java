package com.wut.services;

public class ServiceRunner implements Runnable {
	private WutService service;
	
	public ServiceRunner(WutService service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		service.start();
	}

}
