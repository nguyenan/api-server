package com.wut.datasources.keyvalue.voldemort;
/*
import voldemort.server.VoldemortConfig;
import voldemort.server.VoldemortServer;

import com.wut.support.settings.SystemSettings;

public class WutVoldemortServer {
	private VoldemortServer server;
	
	public boolean start() {
		VoldemortConfig config = VoldemortConfig.loadFromVoldemortHome("conf/voldemort");
		String dataDir = SystemSettings.getInstance().getSetting("data.location");
		String voldemortDataDir = dataDir + "/voldemort";
		config.setDataDirectory(voldemortDataDir);
		server = new VoldemortServer(config);
		server.start();
		while (!server.isStarted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// eat exception
			}
		}
		return true;
	}
	
	public boolean stop() {
		server.stop();
		return true;
	}
	
}
*/