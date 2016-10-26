package com.wut.format.xml;

//import java.io.IOException;
//
////import org.restlet.data.MediaType;
////
////import com.thoughtworks.xstream.XStream;
////import com.thoughtworks.xstream.io.xml.DomDriver;
//import com.wut.format.Formatter;
//import com.wut.model.Data;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.model.map.MessageData;
//import com.wut.model.message.ErrorData;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.StreamWriter;

@Deprecated
public class XMLFormatter { //implements Formatter {
//	private DomDriver driver = new DomDriver();
//	private XStream xstream = null;
//
//	public XMLFormatter() {
//		xstream = getConfiguredXStream();
//	}
//
//	private XStream getConfiguredXStream() {
//		XStream xstream = new XStream(driver);
//		// xstream.
//		// xstream.alias("map", LinkedHashMap.class);
//		// TODO still crappy coupling
//		// xstream.alias("Entity", MapEntity.class);
//		// xstream.alias("List", ArrayEntityList.class);
//		// xstream.alias("list", ListData.class);
//		// xstream.registerConverter(new EntityListConverter());
//		//xstream.alias("data", DatabaseData.class);
//		xstream.alias("data", ListData.class);
//		xstream.alias("data", MappedData.class);
//		xstream.alias("data", MessageData.class);
//		xstream.registerConverter(new MapConverter());
//		xstream.registerConverter(new ListConverter());
//		xstream.registerConverter(new ScalarConverter());
//		// xstream.processAnnotations(ListData.class);
//		// TODO this is crappy coupling. try to fix this design.
//		xstream.processAnnotations(ErrorData.class);
//		xstream.processAnnotations(MessageData.class);
//		// xstream.processAnnotations(ArrayEntityList.class);
//		// xstream.processAnnotations(Entity.class);
//		// xstream.getMapper().
//		return xstream;
//	}
//
//	public MediaType getMediaType() {
//		return MediaType.TEXT_XML;
//	}
//
//	@Override
//	public void format(Data data, StreamWriter stream, WutRequestInterface request) {
//		try {
//			String xml = xstream.toXML(data);
//			stream.write(xml);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
