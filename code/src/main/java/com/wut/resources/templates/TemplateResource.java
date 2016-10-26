package com.wut.resources.templates;

import java.util.ArrayList;
import java.util.Collection;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

//com.wut.resources.templates.TemplateResource

public class TemplateResource extends AbstractResource {

	private static final long serialVersionUID = 5461222419822890264L;

	public TemplateResource() {
		super("template");
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new ReadTemplateOperation());
		//operations.add(new GenerateTemplateOperation());
		operations.add(new RenderTemplateOperation());
		operations.add(new RefreshTemplateOperation());
		operations.add(new InitializeTemplateOperation());
		operations.add(new CopyTemplateOperation());
		operations.add(new KillTemplateOperation());
		return operations;
	}

}
