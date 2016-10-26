package com.wut.format.xml;

//import java.util.Iterator;
//
////import com.thoughtworks.xstream.converters.Converter;
////import com.thoughtworks.xstream.converters.MarshallingContext;
////import com.thoughtworks.xstream.converters.UnmarshallingContext;
////import com.thoughtworks.xstream.io.HierarchicalStreamReader;
////import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.wut.model.Data;
//import com.wut.model.list.ListData;

@Deprecated
public class ListConverter { //implements Converter {

//        public boolean canConvert(Class claz) {
//        	return ListData.class.isAssignableFrom(claz);
//        }
//
//        public void marshal(Object value, HierarchicalStreamWriter writer,
//                        MarshallingContext context) {
//        	ListData list = (ListData) value;
//        	Iterator itr = list.iterator();
//        	//writer.startNode("list");
//        	while (itr.hasNext()) {
//            	//writer.startNode("item");
//        		context.convertAnother(itr.next());
//        	    //writer.endNode();
//        	}
//        	//writer.endNode();
//        }
//
//        public Object unmarshal(HierarchicalStreamReader reader,
//                        UnmarshallingContext context) {
//               throw new UnsupportedOperationException();
//        }

}
