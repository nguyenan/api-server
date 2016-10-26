package com.wut.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CasandraClientNew2 {
	
	public static void main(String[] args) {
		try {
			Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
			Connection con = DriverManager
					.getConnection("jdbc:cassandra://localhost:9170/Keyspace1");

			String query = "UPDATE Test SET a=?, b=? WHERE KEY=?";
			PreparedStatement statement = con.prepareStatement(query);

			statement.setLong(1, 100);
			statement.setLong(2, 1000);
			statement.setString(3, "key0");

			statement.executeUpdate();

			statement.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
