package com.wut.resources.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.resources.ResourceFactory;

public class CrudResource extends RudResource {
	private static final long serialVersionUID = 1L;
	private CrudSource source;
	
	public CrudResource(String name, CrudSource source) {
		super(name, source);
		this.source = source;
		
		ResourceFactory.getInstance().addResource(this);
	}
	
	@Override
	public CrudSource getSource() {
		return source;
	}

	@Override
	public Collection<WutOperation> getOperations() {
		Collection<WutOperation> operationList = super.getOperations();
		ArrayList<WutOperation> operationListCopy = new ArrayList<WutOperation>(operationList);
		operationListCopy.add(getCreateOperation());
		return operationListCopy;
	}
	
	public WutOperation getCreateOperation() {
		return new CreateOperation();
	}

	public Data create(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		String application = request.getApplication();
		Map<String,String> data = request.getParameterAsMap("data");
		
		return source.create(customer, application, data);
	}
	
	public class CreateOperation extends com.wut.resources.operations.CreateOperation {
		@Override
		public String getName() {
			return WutOperation.CREATE;
		}

		@Override
		public Data perform(WutRequest request) throws Exception {
			return create(request);
		}
	}

}
