package com.wut.services;

//import com.wut.support.settings.SystemSettings;

public class LoadBalancerService implements WutService {

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
//		String loadBalancerPortStr = SystemSettings.getInstance().getSetting("loadbalancer.port");
//		int loadBalancerPort = Integer.parseInt(loadBalancerPortStr);
		//Distributor d = new Distributor(loadBalancerPort, Level.WARNING);
		//d.balance(); // TODO needs to happen after REST service is active
		
		//d.addServer(hostname, serverPort);
		//d.refreshTargets();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
