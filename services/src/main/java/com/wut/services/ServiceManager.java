package com.wut.services;

public class ServiceManager {

	WutService[] services = new WutService[] { 
			//new CassandraService(),
			//new VoldermortService(), 
			//new LoadBalancerService(),
			//new RestService()
			//new DerbyService()
	};

	public void startServices() {
		for (WutService service : services) {
			service.initialize();
		}

		for (WutService service : services) {
			System.out.println("Starting " + service.getClass().getCanonicalName() + "...");
			ServiceRunner serviceRunner = new ServiceRunner(service);
			Thread thread = new Thread(serviceRunner);
			thread.start();
		}
	}
	
	public void stopServices() {
		for (WutService service : services) {
			service.stop();
		}
	}
}
