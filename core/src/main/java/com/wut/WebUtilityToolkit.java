package com.wut;
/*
import java.net.UnknownHostException;
import java.util.Properties;

import com.wut.services.Jetty8Service;
import com.wut.services.ServiceManager;
import com.wut.support.ErrorHandler;
import com.wut.support.logging.WutLogger;

public class WebUtilityToolkit {
	WutLogger logger = WutLogger.create(getClass());
	//private static int serverCount = 1;
	ServiceManager serviceManager = new ServiceManager();
	
	public static void main(String[] args) {
//		WutLogger.stopAllLogging();
//		WebUtilityToolkit toolkit = new WebUtilityToolkit();
//		toolkit.startWut();
		
		//addResource(new DiagnosticResource());
//		addResource(new SettingsResource());
//		//addResourceGroup("admin", new ServerInitializationResource());
//		addResource(new PerformanceResource());
//		//addResourceGroup("geocode", new GeocodeResource()); // TODO fix
//		addResource(new HelpResource());
//		addResource(new PhotoSearchResource());
//		addResource(new SinglePhotoResource());
//		addResource(new WebSearchResource());
//		addResource(new HashResource());
//		addResource(new PageGenerator());
////		addResource(new AuthenticationResource());
//		addResource(new YetAnotherDatabaseResource());
//		//addResource(new RudResource("schema", new SchemaStore()));
//		addResource(new UsersResource());
//		addResource(new ComponentResource());
//		addResource(new ApplicationResource("application", null));
//		addResource(new CrudResource("application.component", new ApplicationComponentStore()));
//		addResource(new com.wut.resources.application.ComponentResource("component", new ApplicationComponentStore()));
//		
//		// STORAGE
//		addResource(new TableResource());
//		addResource(new ScalarResource());
//		
//		// PAYMENT
//		addResource(new PaymentResource());

//		new EmailResource();
//		new TemplateResource();
//		new FileBasedResource("file");
//		new FileResource();

		
		try {
			Jetty8Service jetty = new Jetty8Service();
		} catch (Exception e) {
			ErrorHandler.fatalError(e, "jetty" + " service failed to start");
		}
	}
	
	public void startWut() throws UnknownHostException {
		Properties props = System.getProperties();
		props.put("storage-config", ".");
		System.setProperties(props);
		
		serviceManager.startServices();
		
		System.out.println("*** all services active ***");
	}
	
	public void startWithThred() throws UnknownHostException {
		new Thread() {
			@Override
			public void run() {
				try {
					startWut();
				} catch (UnknownHostException e) {
					ErrorHandler.fatalError("error starting wut", e);
				}
			}
		}.start();
		
		WutLogger.stopAllLogging();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
*/