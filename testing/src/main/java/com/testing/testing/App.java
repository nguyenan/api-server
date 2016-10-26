package com.testing.testing;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Cluster cluster;
        Session session;
        
        // Connect to the cluster and keyspace "demo"
        cluster = Cluster.builder().addContactPoint("db1.webutilitykit.com").build();
        session = cluster.connect("mykeyspace");
        
        //session.execute("INSERT INTO users (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");
        session.execute("INSERT INTO users (user_id,  fname, lname) VALUES (1245, 'ruck', 'miter')");
        session.execute("INSERT INTO users (user_id,  fname, lname) VALUES (1345, 'david', 'smith')");
        session.execute("INSERT INTO users (user_id,  fname, lname) VALUES (1445, 'jeremy', 'jones')");
        
        // Use select to get the user we just entered
        ResultSet results = session.execute("SELECT * FROM users WHERE lname = 'smith'");
        for (Row row : results) {
        	System.out.format("%s %d\n", row.getString("fname"), 4 /* row.getInt("age") */);
        }
        
        cluster.close();
        
    }
}
