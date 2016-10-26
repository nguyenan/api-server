package com.wut.resources.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// TODO this class is all broken. fix like RudResource.
public class CCrudResource implements WutResource {

	private String name;

	public CCrudResource(String name) {
		this.name = name;
	}
	
	@Override
	public List<String> getExamples() {
		return Collections.singletonList("find me some of them examples");
	}

	@Override
	public String getHelp() {
		return "help this";
	}

	@Override
	public String getRevision() {
		return "1.00";
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<WutOperation> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}

}
