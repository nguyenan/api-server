package com.wut.resources.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.ModelManager;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;
import com.wut.resources.operations.ReadOperation;

public class FileBasedResource implements WutResource {
	@SuppressWarnings("unused")
	private static ModelManager masterModel = new ModelManager();
	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());

	private String name;
	
	public FileBasedResource(String name) {
		this.name = name;
	}
	
	public List<String> getExamples() {
		return Collections.singletonList("find me some of them examples");
	}

	public String getHelp() {
		return "help this";
	}

	public String getRevision() {
		return "1.00";
	}

	public boolean initialize() {
		return true;
	}

	public String getName() {
		return name;
	}
	
	// THESE METHODS MAKE IT EASY TO EXTEND TABLEBASEDRESOURCE
	
	public Data create(WutRequest request) throws MissingParameterException {
		//MappedData data = request.getParameter("data");
		//Data result = provider.insertRow(TABLE_ID, data);
		throw new RuntimeException("cant create");
		//return null;
	}
	
	public Data read(WutRequest request) throws MissingParameterException {
		String identifier = request.getScopeWithId();
		String client = request.getCustomer();
		String bucket = request.getParameter("bucket");

		Data data = provider.read(IdData.create(client), IdData.create(bucket), new StringData(identifier));
		return data;
	}
	
	public Data update(WutRequest request) throws MissingParameterException {
		String identifier = request.getScopeWithId();
		//IdData rowid = new IdData(identifier);
		String client = request.getCustomer();
		String bucket = request.getParameter("bucket");
		StreamData data = request.getParameter("data");
		provider.update(IdData.create(client), IdData.create(bucket), new StringData(identifier), data);
		return MessageData.success();
	}
	
	public Data delete(WutRequest request) throws MissingParameterException {
//		String identifier = request.getScopeWithId();
//		IdData rowid = new IdData(identifier);
//		provider.deleteRow(TABLE_ID, rowid);
		return MessageData.error("not implemented");
	}
	
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operationList = new ArrayList<WutOperation>();
		//operationList.add(new CreateOperation());
		operationList.add(new ReadOperation());
		operationList.add(new UpdateOperation());
		//operationList.add(new DeleteOperation());
		return operationList;
	}
	
	public class CreateOperation extends com.wut.resources.operations.CreateOperation {
		@Override
		public Data perform(WutRequest request) throws Exception {
			return create(request);
		}
	}
	
	public class ReadOperation extends com.wut.resources.operations.ReadOperation {
		@Override
		public Data perform(WutRequest request) throws Exception {
			return read(request);
		}
	}
	
	public class UpdateOperation extends com.wut.resources.operations.UpdateOperation {
		@Override
		public Data perform(WutRequest request) throws Exception {
			return update(request);
		}
	}
	
	public class DeleteOperation extends com.wut.resources.operations.DeleteOperation {
		@Override
		public Data perform(WutRequest request) throws Exception {
			return delete(request);
		}
	}

}
