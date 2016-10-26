package com.wut.services;

//import com.wut.protocols.rest.ResletServer;
import com.wut.support.logging.WutLogger;
import com.wut.support.settings.SystemSettings;

// TODO rename Resource Service -- start all protocols (or just start 1 by 1 ? )
public class RestService implements WutService {
	WutLogger logger = WutLogger.create(getClass());

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		String standAlonePortStr = SystemSettings.getInstance().getSetting("standalone.port");
		int standAlonePort = Integer.parseInt(standAlonePortStr);
		int serverPort = standAlonePort++;
        String hostname = "NOHOSTNAME";
        //InetAddress address = InetAddress.getByName("localhost"); // TODO implement
        String hostAddress = "1.1.1.1";
        logger.info("starting this shit!!!");
		System.out.println("*** starting server on host " + hostname + " (" + hostAddress + ")" + " with port " + serverPort + "***");
		logger.info("*** starting server on host " + hostname + " (" + hostAddress + ")" + " with port " + serverPort + "***");
		//System.out.println("*** uugggg ***");
//		ResletServer server = new ResletServer(hostname, serverPort);
//		server.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
