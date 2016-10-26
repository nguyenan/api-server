package com.wut.services;

public class CassandraService implements WutService {
	CassandraServer cassandra;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		cassandra = new CassandraServer();
		cassandra.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
