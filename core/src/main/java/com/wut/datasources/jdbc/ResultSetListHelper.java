package com.wut.datasources.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetListHelper {

	public static List<Map<String,String>> getList(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>(50);
		while (rs.next()) {
			//MappedData row = new MappedData();
			HashMap<String,String> row = new HashMap<String,String>(columns);
			for (int i = 1; i <= columns; ++i) {
				String columnName = md.getColumnName(i);
				String columnVal  = String.valueOf(rs.getObject(i));
				row.put(columnName, columnVal);
			}
			list.add(row);
		}
		return list;
	}
}
