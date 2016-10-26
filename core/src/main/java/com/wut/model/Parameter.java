package com.wut.model;

import java.util.Collections;
import java.util.List;

public class Parameter {
	
	private Class<? extends Data> type;
	private String name;
	private Data value;
	private boolean isOptional = false;
	private List<String> modifiers = Collections.emptyList();
	
	public Parameter(String name) {
		this.name = name;
	}
	public Parameter() {
	}
	public boolean addModifiers(String e) {
		return modifiers.add(e);
	}
	public String getModifiers(int index) {
		return modifiers.get(index);
	}
	public Class<? extends Data> getType() {
		return type;
	}
	public void setType(Class<? extends Data> type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Data getValue() {
		return value;
	}
	public void setValue(Data value) {
		this.value = value;
	}
	public boolean isOptional() {
		return isOptional;
	}
	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}
	public List<String> getModifiers() {
		return modifiers;
	}
	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}
	@Override
	public String toString() {
		return name;
	}
	

	// type = data class
	// name
	// value
	// optional
	// modifiers - string[] List<String>
	
}
