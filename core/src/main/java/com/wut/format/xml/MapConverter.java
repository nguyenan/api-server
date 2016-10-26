package com.wut.format.xml;

//import java.util.Map;
//
////import com.thoughtworks.xstream.converters.Converter;
////import com.thoughtworks.xstream.converters.MarshallingContext;
////import com.thoughtworks.xstream.converters.UnmarshallingContext;
////import com.thoughtworks.xstream.io.HierarchicalStreamReader;
////import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.wut.model.map.MappedData;

@Deprecated
public class MapConverter { //implements Converter {

//	@SuppressWarnings("unchecked")
//	public boolean canConvert(Class clazz) {
//		return Map.class.isAssignableFrom(clazz) || MappedData.class.isAssignableFrom(clazz);
//	}
//
//	@SuppressWarnings("unchecked")
//	public void marshal(Object value, HierarchicalStreamWriter writer,
//			MarshallingContext context) {
//		if (value instanceof MappedData) {
//			value = ((MappedData) value).getMap();
//		}
//		Map<?, ?> map = (Map<String, Object>) value;
//		writer.startNode("map");
//		for (Object key : map.keySet()) {
//			//System.out.println("key="+key);
//			//System.out.println("value="+map.get(key));
//			writer.startNode(String.valueOf(key).toLowerCase().replaceAll(" ", "-"));
//			//String val = String.valueOf(map.get(key));
//			//writer.setValue(val);
//			context.convertAnother(map.get(key));
//			writer.endNode();
//		}
//		writer.endNode();
//	}
//
//	public Object unmarshal(HierarchicalStreamReader reader,
//			UnmarshallingContext context) {
//		// TODO implement when needed!!
//		throw new RuntimeException("unmarshall not supported");
//		// Person person = new Person();
//		// reader.moveDown();
//		// person.setName(reader.getValue());
//		// reader.moveUp();
//		// return person;
//	}

}
