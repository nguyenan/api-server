package com.wut.services;
//
//
//package com.wut.services;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.cassandra.service.Cassandra;
//import org.apache.cassandra.service.Column;
//import org.apache.cassandra.service.ColumnOrSuperColumn;
//import org.apache.cassandra.service.ColumnParent;
//import org.apache.cassandra.service.ColumnPath;
//import org.apache.cassandra.service.ConsistencyLevel;
//import org.apache.cassandra.service.InvalidRequestException;
//import org.apache.cassandra.service.NotFoundException;
//import org.apache.cassandra.service.SlicePredicate;
//import org.apache.cassandra.service.SliceRange;
//import org.apache.cassandra.service.UnavailableException;
//import org.apache.thrift.TException;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//import org.apache.thrift.transport.TTransportException;
//
//import com.wut.datasources.tablestore.RelationalStore;
//import com.wut.model.Data;
//import com.wut.support.ErrorHandler;
//import com.wut.support.UniqueIdGenerator;
//
//
//// TODO rename CassandraStore
//public class CassandraClient implements RelationalStore {
//	private final String keyspace = "Keyspace1"; // application space
//	private TTransport tr;
//	private TProtocol proto;
//	private Cassandra.Client client;
//
//	public CassandraClient() {
//		// open connection to cassandra server
//		tr = new TSocket("localhost", 9160);
//		proto = new TBinaryProtocol(tr);
//		client = new Cassandra.Client(proto);
//		try {
//			tr.open();
//		} catch (TTransportException e) {
//			ErrorHandler.dataLossError("cassandra client unable to start", e);
//		}
//	}
//	
//	public void start() {
//		
//	}
//
//	private void insert(String table, String id, Map<String, String> values) {
//		for (String name : values.keySet()) {
//			String value = values.get(name);
//			insert(table, id, name, value);
//		}
//	}
//
//	private void insert(String table, String id, String name, String value) {
//		long timestamp = System.currentTimeMillis();
//		try {
//			client.insert(keyspace, id, new ColumnPath(table, null, name
//					.getBytes("UTF-8")), value.getBytes("UTF-8"), timestamp,
//					ConsistencyLevel.ONE);
//		} catch (UnsupportedEncodingException e) {
//			ErrorHandler.dataLossError("bad string encoding", e);
//		} catch (InvalidRequestException e) {
//			ErrorHandler.dataLossError("bad cassandra request", e);
//		} catch (UnavailableException e) {
//			ErrorHandler.dataLossError("cassandra unavailable", e);
//		} catch (TException e) {
//			ErrorHandler.dataLossError("wtf happened to cassandra", e);
//		}
//	}
//
//	private Map<String, String> get(String table, String id) {
//		String key_user_id = id;
//		Map<String, String> row = new HashMap<String, String>();
//		
//		SlicePredicate predicate = new SlicePredicate(null, new SliceRange(
//				new byte[0], new byte[0], false, 10));
//		ColumnParent parent = new ColumnParent(table, null);
//		try {
//			List<ColumnOrSuperColumn> results = client.get_slice(keyspace,
//					key_user_id, parent, predicate, ConsistencyLevel.ONE);
//			
//			for (ColumnOrSuperColumn result : results) {
//				Column column = result.column;
//				String name = new String(column.name, "UTF-8");
//				String value = new String(column.value, "UTF-8");
//				//System.out.println(name + " -> " + value);
//				if (!name.startsWith("_")) {
//					row.put(name, value);
//				}
//			}
//			
//			// if row isn't empty, add in the id. this prevents us from adding
//			if (row.size() > 0) {
//				row.put("id", id);
//			}
//		} catch (InvalidRequestException e) {
//			ErrorHandler.systemError("bad row request to cassandra", e);
//		} catch (NotFoundException e) {
//			ErrorHandler.systemError("no row found in cassandra error",e);
//		} catch (UnavailableException e) {
//			ErrorHandler.dataLossError("cassandra unavailable [will lead to data loss]", e);
//		} catch (TException e) {
//			ErrorHandler.systemError("cassandra i dont know what just happened error", e);
//		} catch (UnsupportedEncodingException e) {
//			ErrorHandler.systemError("invalid string encoding", e);
//		}
//		return row;
//	}
//	
//	private void multiget() {
//		
////		def _big_multislice(keyspace, keys, column_parent):
////		    p = SlicePredicate(slice_range=SliceRange('', '', False, 1000))
////		    return client.multiget_slice(keyspace, keys, column_parent, p, ConsistencyLevel.ONE)
//		
//	}
//	
//	public void filtered_get() {
//		//  p = SlicePredicate(slice_range=SliceRange('a', 'z', False, 1000))
////	    result = client.get_slice('Keyspace1','key1', ColumnParent('Standard1'), p, ConsistencyLevel.ONE)
////	    assert len(result) == 3, result
//	}
//	
//	private List<Map<String, String>> get(String table) {
//		
//		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
//		
//		SlicePredicate predicate = new SlicePredicate(null, new SliceRange(
//				new byte[0], new byte[0], false, 10));
//		ColumnParent parent = new ColumnParent(table, null);
//		try {
//			//List<String> keys = Collections.singletonList("key");
//			//Map<String,List<ColumnOrSuperColumn>> resultparents = client.multiget_slice(keyspace, keys, parent, predicate, ConsistencyLevel.ONE);
//			int maxResults = 100;
//			List<String> items = client.get_key_range(keyspace, /*col family*/ table, /*start with*/"", /*stop at*/"", maxResults, ConsistencyLevel.ONE);
//			
//			for (String key : items) {
//				// request each one
//				System.out.println("K="+key);
//				Map<String, String> row = get(table, key);
//				rows.add(row);
//			}
//
//		} catch (InvalidRequestException e) {
//			ErrorHandler.systemError("bad row request to cassandra", e);
//		} catch (UnavailableException e) {
//			ErrorHandler.dataLossError("cassandra unavailable [will lead to data loss]", e);
//		} catch (TException e) {
//			ErrorHandler.systemError("cassandra i dont know what just happened error", e);
//		}
//		return rows;
//	}
//	
//	// not really needed -- ugggg ummm uggg
//	public void shutdown() {
//		tr.close();
//	}
//
//	public static void main(String[] args) throws TException,
//			InvalidRequestException, UnavailableException,
//			UnsupportedEncodingException, NotFoundException {
//
//		CassandraClient cc = new CassandraClient();
//		String table = "Standard1"; // tables must be defined ahead of time
//		String key_user_id = "1";
//		cc.insert(table, key_user_id, "fullname", "Chris Goffinet");
//		cc.insert(table, key_user_id, "age", "24");
//		cc.insert(table, key_user_id, "dogname", "fluffy");
//		Map<String,String> m = cc.get(table, key_user_id);
//		for (String s : m.keySet()) {
//			System.out.println(s + ":" + m.get(s));
//		}
//	}
//
//	@Override
//	public boolean createTable(String table, Map<String, Class<Data>> schema) {
//		return false;
//	}
//
//	@Override
//	public boolean deleteRow(String table, String id) {
//		return false;
//	}
//
//	@Override
//	public boolean deleteTable(String table) {
//		return false;
//	}
//
//	@Override
//	public List<Map<String, String>> getAllRows(String table) {
//		return get(table);
//	}
//
//	@Override
//	public List<String> getAllTables() {
//		return null;
//	}
//
//	@Override
//	public Map<String, String> getRow(String table, String id) {
//		return get(table, id);
//	}
//
//	@Override
//	public Map<String, Class<Data>> getTableSchema(String table) {
//		return null;
//	}
//
//	@Override
//	public String insertRow(String table, Map<String, String> row) {
//		String id = UniqueIdGenerator.getId();
//		insert(table, id, row);
//		return id; // TODO fix
//	}
//
//	@Override
//	public boolean updateRow(String table, String id, Map<String, String> row) {
//		
//		insert(table, id, row);
//		
//		return true; // TODO fix
//	}
//
//	@Override
//	public boolean updateTable(String table, Map<String, Class<Data>> schema) {
//		return false;
//	}
//
//}

