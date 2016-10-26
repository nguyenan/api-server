package com.wut.resources.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.ResourceFactory;
import com.wut.resources.operations.ParameteredOperation;

public abstract class ObsoleteCrudResource extends AbstractResource {
	private static final long serialVersionUID = 452262046459168L;

	private List<WutOperation> operations = new ArrayList<WutOperation>();

	public ObsoleteCrudResource() {
		super("obsoletecrudresource");
		initializeOperations();
		
		ResourceFactory.getInstance().addResource(this);
	}
	
	private void initializeOperations() {
		List<String> opNames = Arrays.asList("create", "read", "update", "delete");
		for (String opName : opNames) {
			try { // take affect finally 
				java.lang.reflect.Method m = this.getClass().getMethod(opName, WutRequest.class);
				if (m.getDeclaringClass() != ObsoleteCrudResource.class) {
					CrudOperation op = new CrudOperation(opName);
					operations.add(op);
				}
			} catch (Exception e) {
				// Eat exception
			}
		}
	}

	public Data create(WutRequest request) throws Exception {
		return MessageData.NOT_IMPLEMENTED;
	}
	
	public Data read(WutRequest request) throws Exception {
		return MessageData.NOT_IMPLEMENTED;
	}

	public Data update(WutRequest request) throws Exception {
		return MessageData.NOT_IMPLEMENTED;
	}

	public Data delete(WutRequest request) throws Exception {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public List<String> getExamples() {
		return Collections.singletonList("no examples for this resource yet");
	}

	@Override
	public String getHelp() {
		return "no help message yet";
	}

	@Override
	public String getName() {
		String className = getClass().getSimpleName();
		String shortenClassName = className.replaceAll("Resource", "");
		StringBuilder name = new StringBuilder(className.length() + 3);

		for (int i = 0; i < shortenClassName.length(); i++) {
			String c = new String(shortenClassName.charAt(i) + "");
			if (i != 0 && c.equals(c.toUpperCase())) {
				name.append(".");
			}
			name.append(c.toLowerCase());
		}
		// System.out.println("RESOURCE NAME:" + name);
		return name.toString();
	}

	private class CrudOperation extends ParameteredOperation {
		private final String name;

		public CrudOperation(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public Data perform(WutRequest ri) throws Exception {
			if (name.equals("create")) {
				return create(ri);
			} else if (name.equals("read")) {
				return read(ri);
			} else if (name.equals("update")) {
				return update(ri);
			} else if (name.equals("delete")) {
				return delete(ri);
			}
			return MessageData.NOT_IMPLEMENTED;
		}
		
		@Override
		public String toString() {
			return "op:" + ObsoleteCrudResource.this.getName() + ":" + getName();
		}
	}

	@Override
	public Collection<WutOperation> getOperations() {
		return operations;
	}

	@Override
	public boolean initialize() {
		return true;
	}
	
	public String getRevision() {
		return "1.0";
	}

	@Override
	public String toString() {
		return getName();
	}
	
	protected IdData getCustomer(WutRequest request) {
		String customer = request.getCustomer();
		return new IdData(customer);
	}
	
	protected IdData getApplication(WutRequest request) {
		String application = request.getApplication();
		return new IdData(application);
	}
}
