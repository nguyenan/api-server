package com.wut.services;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.derby.drda.NetworkServerControl;

import com.wut.support.settings.SettingsManager;

public class DerbyService implements WutService {
	private NetworkServerControl server;
	private static final int DERBY_PORT = Integer.parseInt(SettingsManager.getSystemSetting("derby.port"));
	private final String DERBY_DB_USERNAME = SettingsManager.getSystemSetting("derby.username");
	private final String DERBY_DB_PASSWORD = SettingsManager.getSystemSetting("derby.password");
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
//		String userHomeDir = System.getProperty("user.home", ".");
//		String systemDir = userHomeDir + File.separator + "data";
//		System.setProperty("derby.system.home", systemDir);
//		File fileSystemDir = new File(systemDir);
//		fileSystemDir.mkdir();
		
		//NetworkServerControl serverControl = new NetworkServerControl(InetAddress.getByName("myhost"),1621);
//		serverControl.shutdown();
		
		System.setProperty("derby.drda.startNetworkServer", "true");
		// start derby in port 20000

		 try {
			InetAddress localhostAddress = InetAddress.getByName("localhost");
			
			server = new NetworkServerControl(localhostAddress, 
			                                   DERBY_PORT,
			                                   DERBY_DB_USERNAME, 
			                                   DERBY_DB_PASSWORD);
			
			java.io.PrintWriter consoleWriter = new java.io.PrintWriter(System.out, true);
			server.start(consoleWriter);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void stop() {
		try {
			server.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
