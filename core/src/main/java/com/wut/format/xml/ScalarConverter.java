package com.wut.format.xml;

//import java.util.Iterator;
//
////import com.thoughtworks.xstream.converters.Converter;
////import com.thoughtworks.xstream.converters.MarshallingContext;
////import com.thoughtworks.xstream.converters.UnmarshallingContext;
////import com.thoughtworks.xstream.io.HierarchicalStreamReader;
////import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.wut.model.list.ListData;
//import com.wut.model.scalar.*;

@Deprecated
public class ScalarConverter {// implements Converter {

//        public boolean canConvert(Class claz) {
//        	return ScalarData.class.isAssignableFrom(claz);
//        }
//
//        public void marshal(Object value, HierarchicalStreamWriter writer,
//                        MarshallingContext context) {
//        	ScalarData scalar = (ScalarData) value;
//        	String name = scalar.getClass().getSimpleName().replace("Data", "");
//        	writer.startNode(name);
//        	String val = String.valueOf(scalar) == null ? "null" : String.valueOf(scalar);
//        	writer.setValue(val);
//        	writer.endNode();
//        }
//
//        public Object unmarshal(HierarchicalStreamReader reader,
//                        UnmarshallingContext context) {
//               throw new UnsupportedOperationException();
//        }

}
