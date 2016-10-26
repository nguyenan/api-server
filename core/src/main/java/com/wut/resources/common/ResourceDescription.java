package com.wut.resources.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.wut.resources.common.WutOperation.TYPE;

// TODO rename
@Deprecated
public class ResourceDescription {
	private String name;
	private String description;
	private Class<?> resource;
	private Map<TYPE, WutOperation> operations;
	private String group;
	private int revision;
	
	public ResourceDescription(String name, String group, int revision, String description,
			Class<?> resource, Map<TYPE, WutOperation> operations) {
		this.name = name;
		this.description = description;
		this.resource = resource;
		this.group = group;
		this.operations = operations;
		this.revision = revision;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public WutOperation getOperation(TYPE operationType) {
		return operations.get(operationType);
	}
	
	public Map<TYPE, WutOperation> getOperations() {
		return operations;
	}
	
	public String getGroup() {
		return group;
	}
	
	public int getRevision() {
		return revision;
	}
	
	public void initialize() {
		for (Method m : resource.getMethods()) {
			if (m.isAnnotationPresent(Init.class)) {
				try {
					Object resInstance = resource.newInstance();
					m.invoke(resInstance);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
