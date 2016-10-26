package com.wut.resources;

import com.wut.model.Model;
import com.wut.model.scalar.ScalarModel;

public class OperationParameter {
	public static final OperationParameter ID = OperationParameter.create("id", ScalarModel.create(), true);
			
	private String name;
	private Model type;
	private boolean required;
	private boolean isPartOfScope;

	private OperationParameter(String name, Model type, boolean required, boolean isPartOfScope) {
		super();
		this.name = name;
		this.type = type;
		this.required = required;
		this.isPartOfScope = isPartOfScope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Model getType() {
		return type;
	}

	public void setType(Model type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isPartOfScope() {
		return isPartOfScope;
	}

	public void setPartOfScope(boolean isPartOfScope) {
		this.isPartOfScope = isPartOfScope;
	}
	
	public static OperationParameter create(String name, Model type, boolean isPartOfScope) {
		return new OperationParameter(name, type, true, isPartOfScope);
	}
	public static OperationParameter create(String name, Model type) {
		return new OperationParameter(name, type, true, false);
	}

	public static OperationParameter string(String name) {
		return create(name, ScalarModel.create());
	}

	public OperationParameter optional() {
		setRequired(false);
		return this;
	}

	public static OperationParameter id(String name) {
		return create(name, ScalarModel.create()); // fix one day
	}
}
