package com.wut.model.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.wut.model.AbstractData;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.StringData;

// Note: Iterable data can have multiple demensions of iterability????
@SuppressWarnings("rawtypes")
public class ListData extends AbstractData implements Iterable {

	private List<Data> members;

	public ListData(Iterable<? extends Data> iterableData) {
		this.members = new ArrayList<Data>();
		// TODO make this more efficient
		for (Data d : iterableData) {
			members.add(d);
		}
	}

	public ListData() {
		members = new ArrayList<Data>(); // TODO replace with Collections.emptyList() for performance
	}
	
	public ListData(List<String> listDomains) {
		this();
		for (String s : listDomains) {
			members.add(new StringData(s));
		}
	}

	public Iterator<? extends Data> iterator() {
		return members.iterator();
	}

	public void add(Data d) {
		members.add(d);
	}
	
	@Override
	public String toString() {
		return members.toString();
	}

	// // TODO fix formatting to work like this instead (composite design
	// pattern)
	// public void format(Formatter formatter) {
	// for (Object o : data) {
	// //formatter.format();
	// }
	// }s
	
	public static ListData convert(List<Map<String,String>> data) {
		ListData list = new ListData();
		for (Map<String,String> map : data) {
			MappedData dataMap = new MappedData();
			for (String key : map.keySet()) {
				dataMap.put(key, map.get(key));
			}
			list.add(dataMap);
		}
		return list;
	}

	public Data get(int index) {
		Data d = this.members.get(index);
		return d;
	}
	
	public void remove(Data d) {
		members.remove(d);
	}

	public int size() {
		return members.size();
	}
	
	public <T> List<T> toList() {
		return (List<T>) (members);
	}

	@Override
	public Model<ListData> getModel() {
		return ListModel.create();
	}

}
