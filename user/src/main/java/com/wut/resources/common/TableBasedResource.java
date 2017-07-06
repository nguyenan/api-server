package com.wut.resources.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.table.TableProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ReadOperation;
import com.wut.resources.storage.TableResource;

public class TableBasedResource extends AbstractResource {
	private static final long serialVersionUID = 260482842774293073L;
	private static IdData TABLE_ID; //= new IdData("schema");
	//private static ModelManager masterModel = new ModelManager();
//	private static DataSourceManager dsm = new DataSourceManager();
//	private static TableSource source = dsm.getTableSource();
	private TableProvider provider = TableResource.getTableProvider();
	private String name;
	
	public TableBasedResource(String name) {
		super(name);
		TABLE_ID = new IdData(name);
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
	
	// THESE METHODS MAKE IT EASY TO EXTEND TABLEBASEDRESOURCE
	
	public Data create(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		MappedData data = request.getParameter("data");
		Data result = provider.insertRow(customer, application, TABLE_ID, data);
		return result;
	}
	
	public Data read(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String identifier = request.getScopeWithId();
		Data data = provider.getRow(customer, application, TABLE_ID, new IdData(identifier));
		return data;
	}
	
	public Data update(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String identifier = request.getScopeWithId();
		IdData rowid = new IdData(identifier);
		//Map<String,String> data = request.getParameterAsMap("data");
		MappedData data = request.getParameter("data");
		provider.updateRow(customer, application, TABLE_ID, rowid, data);
		return MessageData.success();
	}
	
	public Data delete(WutRequest request) throws MissingParameterException {
		IdData customer = getCustomer(request);
		IdData application = getApplication(request);
		String identifier = request.getScopeWithId();
		IdData rowid = new IdData(identifier);
		provider.deleteRow(customer, application, TABLE_ID, rowid);
		return MessageData.success();
	}
	
	
	private IdData getCustomer(WutRequest request) {
		String customer = request.getCustomer();
		return new IdData(customer);
	}
	
	private IdData getApplication(WutRequest request) {
		String application = request.getApplication();
		return new IdData(application);
	}

	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		operationList.add(new CreateOperation());
		operationList.add(new ReadOperation());
		operationList.add(new UpdateOperation());
		operationList.add(new DeleteOperation());
		return operationList;
	}
	
	public class CreateOperation extends com.wut.resources.operations.CreateOperation {
		public CreateOperation() {
			addParameter(OperationParameter.id("table"));
		}
		
		@Override
		public Data perform(WutRequest request) throws Exception {
			return create(request);
		}
	}
	
	
	public class ReadOperation extends com.wut.resources.operations.ReadOperation {
		public ReadOperation() {
			addParameter(OperationParameter.id("table"));
		}
		
		@Override
		public Data perform(WutRequest request) throws Exception {
			return read(request);
		}
	}
	
	public class UpdateOperation extends com.wut.resources.operations.UpdateOperation {
		public UpdateOperation() {
			addParameter(OperationParameter.id("table"));
		}
		
		@Override
		public Data perform(WutRequest request) throws Exception {
			return update(request);
		}
	}
	
	public class DeleteOperation extends com.wut.resources.operations.DeleteOperation {
		public DeleteOperation() {
			addParameter(OperationParameter.id("table"));
		}
		
		@Override
		public Data perform(WutRequest request) throws Exception {
			return delete(request);
		}
	}

}
