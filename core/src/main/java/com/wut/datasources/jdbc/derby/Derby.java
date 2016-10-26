package com.wut.datasources.jdbc.derby;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.datasources.TableRowSource;
import com.wut.datasources.jdbc.ResultSetListHelper;
import com.wut.datasources.jdbc.SqlStatementHelper;
import com.wut.format.Decoder;
import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
import com.wut.format.json.JsonDecoder;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.support.ErrorHandler;
import com.wut.support.logging.WutLogger;

public class Derby implements TableRowSource {
	private static final String DERBY_DB_USERNAME = "derbyuser";
	private static final String DERBY_DB_PASSWORD = "derbyuser";
	public static final String database = "webutilitykit";
	public static final int DERBY_PORT = 1527;
	

	private static final String dbURL = "jdbc:derby://localhost:" + DERBY_PORT + "/" + database + ";create=true;user=" + DERBY_DB_USERNAME + ";password=" + DERBY_DB_PASSWORD;
	private static final String driver = "org.apache.derby.jdbc.ClientDriver";
	//private static final String connectionString = "jdbc:derby:myDB;create=true;user=me;password=mine";
	//private static final String connectionString = "jdbc:derby:myDB;create=true";
	private static WutLogger log = new WutLogger(Derby.class);
	private static Connection connection;
	//private static boolean initialized = false;
	//private static String dbName="wut";
	//private static String connectionURL = "jdbc:derby:" + dbName + ";create=true";
	
	public Derby() {
		// SETUP DATA DIRECTORY FOR DERBY
//		String userHomeDir = System.getProperty("user.home", ".");
//		String systemDir = userHomeDir + File.separator + "data";
//		System.setProperty("derby.system.home", systemDir);
//		File fileSystemDir = new File(systemDir);
//		fileSystemDir.mkdir();
		
//		try {
//			DriverManager.getConnection("jdbc:derby:ContactsManagerDirectory");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		// start derby
		
		
		/*
		//String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		try {
		    Class.forName(driver);
		} catch(java.lang.ClassNotFoundException e) {
			log.error("error starting derby");
			ErrorHandler.fatalError(e, "error starting derby");
		}
		
		// BOOT THE DB
		String connectionURL = "jdbc:derby:" + database + ";create=true";
		try {
		    this.connection = DriverManager.getConnection(connectionString);
		} catch (Throwable e) {
			log.error("error establishing derby connection");
			ErrorHandler.fatalError("error establishing derby connection");
		}
		*/
	}
	
//	private void start() {
//		//NetworkServerControl serverControl = new NetworkServerControl(InetAddress.getByName("myhost"),1621);
////		serverControl.shutdown();
//	}
	
	private void checkConnection() {
		if (connection == null) {
			createConnection();
		}
	}
	
	private synchronized void createConnection()
    {
        try
        {
        	//if (!initialized) {
//        		// DONT THINK NEW INSTANCE IS APPROPRIATE
               // Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
                //Get a connection
                //connection = DriverManager.getConnection(connectionString);
                
        		Class.forName(driver).newInstance();
        		connection = DriverManager.getConnection(dbURL);
        		
        		try {
	                createTable("webutilitykit", false);
	                createTable("tokens", false);
        		} catch (Exception e) {
        			// fuck that
        		}
                
                //initialized = true;
        //	}
        }
        catch (SQLException e)
        {
    		if (e.getNextException().getErrorCode() ==  45000) {
    			// derby already running which is ok for us
    			e.printStackTrace();
    		} else {
            	e.printStackTrace();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	private boolean createTable(String name, boolean dropIfExists) throws SQLException {
		checkConnection();
		
		// CHECK if table exists
		DatabaseMetaData dbm = connection.getMetaData();
		// check if "employee" table is there
		ResultSet tables = dbm.getTables(null, null, name, null);
		boolean tableExists = false;
		if (tables.next()) { // TODO handle case insensitivity of table name (check twice lower and upper case)
			tableExists = true;
		}
		
		if (tableExists && dropIfExists) {
			// Drop Table
			log.info("Dropping " + name + " table...");
	        Statement dropStmt = connection.createStatement();
	        String dropSql = "DROP TABLE " + name;
	        try {
	        	dropStmt.executeUpdate(dropSql); // TODO use executeDDL
	        } catch (Exception e) {
	        	// eat it
	        	ErrorHandler.fatalError(e, "failed to drop " + name);
	        }
		}
		
		if (!tableExists || dropIfExists) {
			System.out.println("Creating " + name + " table...");
	        Statement createStmt = connection.createStatement();
	        
	        String createTableSql = "CREATE TABLE " + name + " " +
	                     "(id VARCHAR(255) not NULL, " +
	                     " customer VARCHAR(255), " + 
	                     " tablename VARCHAR(255), " + 
	                     " updated INTEGER, " + 
	                     " created INTEGER, " + 
	                     " updator VARCHAR(255), " + 
	                     " creator VARCHAR(255), " + 
	                     " data LONG VARCHAR, " + 
	                     " PRIMARY KEY ( id ))";

	        createStmt.executeUpdate(createTableSql);
	        System.out.println("Created table " + name + "...");
		}
		
        return true;
	}

//		Statement stmt = dbConnection.createStatement();
//		stmt.execute(strCreateContactsTable);
//		
//		
//		String createString = "CREATE TABLE WISH_LIST  "
//		  +  "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY "
//		  +  " WISH_ITEM VARCHAR(32) NOT NULL) " ;
//		
//		// START DERBY ENGINE
//		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
//		...
//		try {
//		    Class.forName(driver);
//		} catch(java.lang.ClassNotFoundException e) {
//		... }
//		
//		// BOOT THE DB
//		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
//		...
//		try {
//		    conn = DriverManager.getConnection(connectionURL);
//		... <most of the program code is contained here> } catch (Throwable e) {
//		... }
//		
//		// CREATE STATEMENT
//		
//		s = conn.createStatement();
//		if (! WwdUtils.wwdChk4Table(conn))
//		{
//		   System.out.println (" . . . . creating table WISH_LIST");
//		   s.execute(createString);
//		}
//		
//		psInsert = conn.prepareStatement
//				   ("insert into WISH_LIST(WISH_ITEM) values (?)");
//		
//		
//		￼psInsert.setString(1,answer);
//		psInsert.executeUpdate();
//		
//		myWishes = s.executeQuery("select ENTRY_DATE, WISH_ITEM
//	               from WISH_LIST order by ENTRY_DATE");" +
//	               		"" +
//	               		";" +
//	               		"";
//	               
//	               ￼while (myWishes.next())
//	               {
//	    
//	               ￼System.out.println("On " + myWishes.getTimestamp(1) +
//	                  " I wished for " + myWishes.getString(2));
//	               }
//	}

	public void shutdown() {
		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) { // TODO this check is not needed
			boolean gotSQLExc = false;
			try {
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException se) {
				if (se.getSQLState().equals("XJ015")) {
					gotSQLExc = true;
				}
			}
			if (!gotSQLExc) {
				log.error("Database did not shut down normally");
			} else {
				log.info("Database shut down normally");
			}
		}
	}

	@Override
	public List<Map<String, String>> get(String customer, String application, String tableId) {
		List<Map<String, String>> listOfResults = new ArrayList<Map<String,String>>();
		
		try {
			String statementStr = SqlStatementHelper.select(tableId);
			ResultSet results = executeQuery(statementStr);
			while (results.next()) {
				//results.getString("_updated");
				String table = results.getString("tablename");
				
				String data = results.getString("data");
				MappedData mappedData = expandDataIntoMap(data);
				Map<String, String> map = mappedData.getMapAsPojo();
				
				map.put("_table", String.valueOf(table));

				int updated = results.getInt("updated");
				map.put("_updated", String.valueOf(updated));
				
				int created = results.getInt("created");
				map.put("_created", String.valueOf(created));
				
				String customer2 = results.getString("customer");
				map.put("_customer", String.valueOf(customer2));
				
				String creator = results.getString("creator");
				map.put("_creator", String.valueOf(creator));
				
				listOfResults.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listOfResults;
	}
	
	@Override
	public List<Map<String, String>> filter(String customer, String application, String tableId,
			Map<String, String> filter) {
		
		try {
			String statementStr = SqlStatementHelper.select(tableId, filter);
			ResultSet results = executeQuery(statementStr);
			while (results.next()) {
				int updated = results.getInt("_updated");
				int created = results.getInt("_created");
				String owner = results.getString("_owner");
				String data = results.getString("_data");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Map<String, String> get(String customer, String application, String tableId, String rowId) {
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put("id", rowId);
		
		String readSql = SqlStatementHelper.select(tableId, conditions);

		ResultSet resultSet = executeQuery(readSql);
		
		try {
			List<Map<String,String>> results = ResultSetListHelper.getList(resultSet);
			if (results != null && results.size() < 1) { // < 1 == 0
				return null;
			} else if (results != null) {
				Map<String,String> result = results.get(0);
				String dataResultString = result.get("DATA");
				Map<String,String> dataMap = parseFromString(dataResultString);
				//String resultMapStr = dataMap.get("result");
				//Map<String,String> resultMap = parseFromString(resultMapStr);
				return dataMap;
			}
		} catch (SQLException e) {
			// eat it
		}
		
		return null;
	}

	@Override
	public boolean remove(String customer, String application, String tableId, String rowId) {
		
		String deleteSql = SqlStatementHelper.remove(tableId, rowId);
		boolean resultSuccessful = executeDML(deleteSql);
		
		return resultSuccessful;
	}

	@Override
	public boolean update(String customer, String application, String tableId, String rowId, Map<String, String> data) {
		data.put("id", rowId); // TODO hacky and weird but needed so SqlHelper generates correct sql
		Map<String, String> statementData = convertMapToString(data);
		//statementData.put("id", rowId); 
		String updateSql = SqlStatementHelper.update(tableId, statementData);
		boolean resultSuccessful = executeDML(updateSql);
		
		return resultSuccessful;
	}

	@Override
	public boolean crupdate(String customer, String application, String tableId, String rowId, Map<String, String> data) {
		boolean updated = update(customer, application, tableId, rowId, data);
		if (!updated) {
			//String insertSql = SqlStatementHelper.insert(tableId, rowId, data);
			data.put("id", rowId);
			String id = insert(customer, application, tableId, data);
			return (id != null);
		}

		return updated;
	}
	
	@Override
	public String insert(String customer, String application, String tableId, Map<String, String> data) {
		Map<String, String> statementData = new HashMap<String, String>();

		convertMapElement("id", data, "id", statementData);
		convertMapElement("_table", data, "tablename", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		convertMapElement("_created", data, "created", statementData);
		convertMapElement("_updator", data, "updator", statementData);
		convertMapElement("_customer", data, "customer", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		
		String dataAsString = condenseToString(data);
		statementData.put("data", dataAsString);

		String insertSql = SqlStatementHelper.insert(tableId, statementData);
		boolean resultSuccessful = executeDML(insertSql);
		
		if (resultSuccessful) {
			return data.get("id");
		} else {
			return null;
		}
	}
	
	// HELPER FUNCTIONS
	
	private static void convertMapElement(String originalKey, Map<String, String> originalData, String destinationKey, Map<String, String> destinationData) {
		if (originalData.containsKey(originalKey)) {
			String originalValue = originalData.get(originalKey);
			destinationData.put(destinationKey, originalValue);
		}
	}
	
	private Map<String, String> convertMapToString(String rowId, Map<String, String> data) {
		Map<String, String> statementData = convertMapToString(data);
		statementData.put("id", rowId);
		return statementData;
	}
	
	private Map<String, String> convertMapToString(Map<String, String> data) {
		Map<String, String> statementData = new HashMap<String, String>();

		convertMapElement("id", data, "id", statementData);
		convertMapElement("_table", data, "tablename", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		convertMapElement("_created", data, "created", statementData);
		convertMapElement("_updator", data, "updator", statementData);
		convertMapElement("_customer", data, "customer", statementData);
		convertMapElement("_updated", data, "updated", statementData);
		
		String dataAsString = condenseToString(data);
		statementData.put("data", dataAsString);

		return statementData;
	}
	
	public MappedData expandDataIntoMap(String data) {
		try {
			JsonDecoder decoder = new JsonDecoder();
			MappedData map = (MappedData) decoder.decode(data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new MappedData();
	}
	
	
	// TODO name stringify() ?
	public String condenseToString(Map<String,String> data) {
		MappedData mappedData = MappedData.convert(data);

		Formatter encoder = FormatFactory.getDefaultFormatter();
		String encodedMap = encoder.format(mappedData);
		
		return encodedMap;
	}
	
	public Map<String,String> parseFromString(String data) {
		try {
			Decoder decoder = FormatFactory.getDefaultDecoder();
			
			String fuckMyLife = null;
		    if (data.startsWith("{\"result\":")) {
		    	String noStartResult = data.substring(10, data.length()-1);
		    	String noEndResult = noStartResult.substring(0, noStartResult.length());
		    	fuckMyLife = noEndResult;
		    } else {
		    	fuckMyLife = data;
		    }
			Data dataObj = decoder.decode(fuckMyLife);
			MappedData mapData = (MappedData) dataObj;
			Map<String,String> pojoData = mapData.getMapAsPojo();
			return pojoData;
		} catch (IOException e) {
			ErrorHandler.systemError(e, "error parsing data in derby");
			return null;
		}
	}
	
	
	
	// JDBC HELPER FUNCTIONS
	
	/*
	 * SELECT - retrieve data from the a database
INSERT - insert data into a table
UPDATE - updates existing data within a table
DELETE - deletes all records from a table, the space for the records remain
MERGE - UPSERT operation (insert or update)
CALL - call a PL/SQL or Java subprogram
EXPLAIN PLAN - explain access path to data
LOCK TABLE - control concurrency
	 */
	public boolean executeDML(String statement) {
		checkConnection();
		System.out.println(statement);
		log.debug("Executing SQL: " + statement);
		try {
			Statement stmt = connection.createStatement();
			boolean resultIsResultSet = stmt.execute(statement);
			if (!resultIsResultSet) {
				int updatedCount = stmt.getUpdateCount();
				return updatedCount > 0;
				//return true;
			}
			return false;
		} catch (SQLException e) {
			ErrorHandler.userError(e.getMessage() + " in statement " + statement);
		}
		return false;
	}
	
	/*
	 * CREATE - to create objects in the database
ALTER - alters the structure of the database
DROP - delete objects from the database
TRUNCATE - remove all records from a table, including all spaces allocated for the records are removed
COMMENT - add comments to the data dictionary
RENAME - rename an object
	 */
	public boolean executeDDL(String statement) {
		checkConnection();
		
		log.debug("Executing SQL: " + statement);
		try {
			Statement stmt = this.connection.createStatement();
			boolean resultIsResultSet = stmt.execute(statement);
			return true;
		} catch (SQLException e) {
			ErrorHandler.userError(e.getMessage() + " in statement " + statement);
		}
		return false;
	}
	
	public ResultSet executeQuery(String query) {
		checkConnection();
		
		System.out.println(query);

		log.debug("Executing SQL: " + query);
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			ErrorHandler.userError("error executing sql query " + query, e);
		}
		return null;				
	}

}
