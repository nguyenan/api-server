package com.wut.resources.services;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;
import com.wut.services.ServiceManager;

//com.wut.resources.templates.TemplateResource

public class ServiceResource extends AbstractResource {

	private static final long serialVersionUID = 5461222419822890264L;
	
	static {
		ServiceManager services = new ServiceManager();
		services.startServices();
	}

	public ServiceResource() {
		super("service");
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new StartServiceOperation());
		operations.add(new StopServiceOperation());
		return operations;
	}

}
