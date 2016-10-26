package com.wut.services;

public class CasandraClientNew {
	
	/*
	public static void main(String[] args) {
		String serverIP = "127.0.0.1";
		String keyspace = "system";

		Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();

		Session session = cluster.connect(keyspace);

		// read
		String cqlStatement = "SELECT * FROM local";
		for (Row row : session.execute(cqlStatement)) {
			System.out.println(row.toString());
		}

		// create update delete
		// for all three it works the same way (as a note the 'system' keyspace
		// cant
		// be modified by users so below im using a keyspace name
		// 'exampkeyspace' and
		// a table (or columnfamily) called users

		String cqlStatementC = "INSERT INTO exampkeyspace.users (username, password) "
				+ "VALUES ('Serenity', 'fa3dfQefx')";

		String cqlStatementU = "UPDATE exampkeyspace.users"
				+ "SET password = 'REPLACE_ME',"
				+ "WHERE username = 'Serenity';";

		String cqlStatementD = "DELETE FROM exampkeyspace.users "
				+ "WHERE username = 'Serenity';";

		session.execute(cqlStatementC); // interchangeable, put any of the
										// statements u wish.

		// keyspace

		String cqlStatement = "CREATE KEYSPACE myfirstcassandradb WITH "
				+ "replication = {'class':'SimpleStrategy','replication_factor':1}";

		session.execute(cqlStatement);

		// column family

		// based on the above keyspace, we would change the cluster and session
		// as follows:
		Cluster cluster = Cluster.builder().addContactPoints(serverIP).build();
		Session session = cluster.connect("myfirstcassandradb");

		String cqlStatement = "CREATE TABLE users ("
				+ " user_name varchar PRIMARY KEY," + " password varchar "
				+ ");";

		session.execute(cqlStatement);
	}
	*/
	
}
