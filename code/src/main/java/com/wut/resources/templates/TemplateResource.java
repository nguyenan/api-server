package com.wut.resources.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;

public class TemplateResource extends AbstractResource {

	private static final long serialVersionUID = 5461222419822890264L;

	public TemplateResource() {
		super("template");
	}
	 
	@Override
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[]{"template.domain", "template.git.branch"});
	}
	
	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[]{"template.domain", "template.git.branch", "template.git.repository"});
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new ReadTemplateOperation());
		operations.add(new RenderTemplateOperation());
		operations.add(new RefreshTemplateOperation());
		operations.add(new InitializeTemplateOperation());
		operations.add(new KillTemplateOperation());
		operations.add(new GetSettingOperation());
		operations.add(new SetSettingOperation());
		return operations;
	}

}
