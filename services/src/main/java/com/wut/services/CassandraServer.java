package com.wut.services;

import java.io.IOException;

//import org.apache.cassandra.service.CassandraDaemon;

import com.wut.support.ErrorHandler;
import com.wut.support.settings.SystemSettings;

public class CassandraServer {
	public static void main(String[] args) throws IOException {
		
		String cfgLoc = SystemSettings.getInstance().getSetting("cassandra.config.location");
		System.setProperty("storage-config", cfgLoc); // TODO FOR SOME REASON NOT USED BY CASSANDRA
		CassandraServer cs = new CassandraServer();
		cs.start();
	}
	
	public void start() {
		new Thread() {
			@Override
			public void run() {
				//CassandraDaemon.main(new String[] { });
			}
		}.start();
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			ErrorHandler.systemError("thread sleep interupted", e);
		}
	}
}

