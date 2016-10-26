package com.wut.resources; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.OperationIdentifier;
import com.wut.resources.common.WutOperation;
import com.wut.resources.common.WutResource;
import com.wut.support.ErrorHandler;
import com.wut.support.annotations.ClassPreamble;
import com.wut.support.performance.PerformanceTimer;

// TODO Rename ResourceManager
@ClassPreamble (author = "Russell Palmiter", date="12/2008")
public class ResourceFactory {
	private PerformanceTimer monitor = PerformanceTimer.create(getClass());
	private Map<OperationIdentifier, WutOperation> operations = new HashMap<OperationIdentifier, WutOperation>();
	private Collection<WutResource> resources = new ArrayList<WutResource>();
	private static ResourceFactory singleton = new ResourceFactory();
	
	// TODO abstract all resources away from the datasource (or provider) of data
	
	private ResourceFactory() {
		try {
//			addResource(new DiagnosticResource());
//			addResource(new SettingsResource());
//			//addResourceGroup("admin", new ServerInitializationResource());
//			addResource(new PerformanceResource());
//			addResource(new EmailResource());
//			//addResourceGroup("geocode", new GeocodeResource()); // TODO fix
//			addResource(new HelpResource());
//			addResource(new PhotoSearchResource());
//			addResource(new SinglePhotoResource());
//			addResource(new WebSearchResource());
//			addResource(new HashResource());
//			addResource(new PageGenerator());
////		addResource(new AuthenticationResource());
//			addResource(new YetAnotherDatabaseResource());
//			//addResource(new RudResource("schema", new SchemaStore()));
//			addResource(new UsersResource());
//			addResource(new ComponentResource());
//			addResource(new ApplicationResource("application", null));
//			addResource(new CrudResource("application.component", new ApplicationComponentStore()));
//			addResource(new com.wut.resources.application.ComponentResource("component", new ApplicationComponentStore()));
//			
//			// STORAGE
//			addResource(new TableResource());
//			addResource(new ScalarResource());
//			
//			// PAYMENT
//			//addResource(new OldPaymentResource());
//			addResource(new PaymentResource());
//			
//			// TEMPLATE
//			addResource(new TemplateResource());
//			
//			// FILE
//			//addResource(new FileBasedResource("file"));
//
//			addResource(new FileResource());

		} catch (Exception e) {
			System.err.println("Error initializing a resource");
			e.printStackTrace();
			ErrorHandler.systemError(e, "Error initializing a resource");
		}
	}
	
	public static ResourceFactory getInstance() {
		return singleton;
	}
	
	public void addResource(WutResource resource) {
		// TODO initialize elsewhere????
		boolean initialized = resource.initialize(); 
		if (!initialized) {
			ErrorHandler.fatalError("resource " + resource.getName() + " failed to initialize");
		}
		resources.add(resource);
		String resName = resource.getName();
		for (WutOperation op : resource.getOperations()) {
			String opName = op.getName();
			OperationIdentifier opId = new OperationIdentifier(resName, opName);
			operations.put(opId, op);
		}
	}

	public WutResource getResource(String name) {
		for (WutResource r : resources) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}

	public Collection<WutResource> getResources() {
		return resources;
	}

	public WutOperation getOperation(OperationIdentifier opId) {
		return operations.get(opId);
	}
	
	public boolean checkResourceValid(WutRequest request) {
		String name = request.getResource();
		WutResource resource = getResource(name);
		if (resource == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public OperationIdentifier getOperationId(WutRequest request) {
		String resource = request.getResource();
		String operation = request.getAction().toLowerCase();
		OperationIdentifier opId = new OperationIdentifier(resource, operation);
		return opId;
	}
	
	public boolean checkOperationValid(WutRequest request) {
		OperationIdentifier opId = getOperationId(request);
		
		WutOperation op = getOperation(opId);
		if (op == null) {
			return false;
		} else {
			return true;
		}
	}
	
	// TODO move this function into the processing pipeline under a new process
	// TODO move this back here?
	public Data performRequest(WutRequest request) {
		boolean isValidResource = checkResourceValid(request);
		if (!isValidResource) {
			return new MessageData("Invalid resource " + request.getResource()
					+ ". Available resources include:"
					+ getResources());
		}
		
		boolean isValidOperation = checkOperationValid(request);
		
		if (!isValidOperation) {
			return new MessageData(
					"invalid operation. avaible operation are: "
							+ getResource(request.getResource()).getOperations());
		}

		// start timing request
		String groupPlusName = request.getResource();
		monitor.start(groupPlusName);

		// 2. process request
		Data data = null;
		try {
			OperationIdentifier opId = getOperationId(request);
			WutOperation op = getOperation(opId);
			data = op.perform(request);
		} catch (MissingParameterException e) {
			return new ErrorData(e);
		} catch (Exception e) {
			final String msg = "failed request due to " + e;
			ErrorHandler.userError(request, msg, e);
			MessageData err = ErrorData.error(msg);
			err.setData(new StringData(msg));
			return err;
		} finally {
			monitor.stop(groupPlusName);
		}

		// Logger logger = Logger.getLogger("com.mycompany");
		
		return data;
	}
}
