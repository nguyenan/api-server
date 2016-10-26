package com.wut.pipeline;

import java.util.ArrayList;
import java.util.List;

//import org.restlet.representation.Representation;



import com.wut.cache.CacheProcessor;
import com.wut.cache.SimpleCacheProcessor;
import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
//import com.wut.model.Data;
import com.wut.model.message.ErrorData;
//import com.wut.protocols.rest.representations.WutRepresentation;
import com.wut.support.ErrorHandler;
import com.wut.support.StreamWriter;

// TODO add Manager to name of class (?)
public class ProcessingPipeline {
	// TODO make a list of WutRequestProcessor(s) that each take a request (immutable) and response (modifications expected)
	//private PerformanceMonitor monitor = PerformanceMonitor.getInstance();
	//private ResourceFactory resFactory = ResourceFactory.getInstance();
	//private Authenticator auth = new Authenticator();
	
	// TODO PermissionProcessor
	// TODO UsageProcessor
	// TODO AuthenticationProcessor
	// TODO CacheProcessor
	// TODO SettingsProcessor
	// TODO ResourceProcessor (aka do the request)
	// TODO PublicApiCheck (differentiate which features are public and which are not)
	
	private List<Processor> processors = new ArrayList<Processor>();
	
	public ProcessingPipeline() {
		// NOTE: PROCESSORS ARE LISTED IN A PARTICULAR ORDER
		// CHANGING THE ORDER OF THESE MAY BREAK THE APPLICATION.
		processors.add(new FormatProcessor());
		processors.add(new PermissionProcessor()); // needs to be first/second ?
		processors.add(new Authenticator()); // needs to be first/second ?
		//processors.add(new LogRequestProcessor()); // TODO re-enable this at some point
		//processors.add(new CacheProcessor());
		processors.add(new SimpleCacheProcessor(true));
		processors.add(new RequestProcessor()); // WILL ALWAYS FINALIZE REQUEST
		//processors.add(new CacheProcessor(false)); // RUN THIS TWICE (ONCE BEFORE FINALIZED, ONCE AFTER)
		processors.add(new LogResponseProcessor());
	}

	// TODO remove this method
//	public Representation representRequest(WutRequest request) {
//		WutResponse response = new WutResponse();
//		
//		for (Processor p : processors) {
//			boolean shouldContinue = p.process(request, response);
//			if (!shouldContinue) {
//				break;
//			}
//		}
//		
//		// TODO remove "request" as parameter
//		Representation rep; // = response.representResponse(request);
//		
//		try {
//			Formatter f = response.getFormat();
//			if (f == null) {
//				f = FormatFactory.getDefaultFormatter();
//				rep = new WutRepresentation(f, ErrorData.INVALID_FORMAT, request);
//			} else {
//				Data d = response.getData();
//				rep = new WutRepresentation(f, d, request);
//			}
//		} catch (Exception e) {
//			ErrorHandler.systemError("error processing request", e);
//		}
//		rep = null;
//		
//		return rep;
//	}
	
	// TODO deprecate this method
	public void process(StreamWriter out, WutRequest request) {
		WutResponse response = new WutResponse();
		response.setStream(out);
		
		process(response, request);
	}
	
	public void process(WutResponse response, WutRequest request) {
		// preprocess
		for (Processor p : processors) {
			p.preProcess(request, response);
		}
		
		// process
		for (Processor p : processors) {
			boolean shouldContinue = p.process(request, response);
			if (!shouldContinue) {
				break;
			}
		}
		
		// postprocess
		for (Processor p : processors) {
			p.postProcess(request, response);
		}
		
		//response.represent(response.getStream(), request);
		
		try {
			Formatter f = response.getFormat();
			if (f == null) {
				f = FormatFactory.getDefaultFormatter();
				f.format(ErrorData.INVALID_FORMAT, response.getStream());
			} else {
				f.format(response.getData(), response.getStream());
			}
		} catch (Exception e) {
			ErrorHandler.systemError("error formatting request", e);
		}
		
	}
	
	public static ProcessingPipeline create() {
		return new ProcessingPipeline();
	}

}
