package com.wut.format.html;

//import java.io.IOException;
//import java.util.Map;
//
////import org.restlet.data.MediaType;
//
//import com.wut.format.Formatter;
//import com.wut.model.Data;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.model.matrix.MatrixData;
//import com.wut.model.scalar.ScalarData;
//import com.wut.model.scalar.UrlData;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.ErrorHandler;
//import com.wut.support.StreamWriter;


public class HTMLTableFormatter {// implements Formatter {
//	private enum Type { HEADER, FOOTER, NORMAL } // TODO rename position (with) FIRST, LAST, MIDDLE
//	
//	private void format(StringBuilder html, Object data, Type type, int depth) {
//		if (data instanceof Iterable) {
//			list(html, (Iterable) data, type, depth);
//		} else if (data instanceof ListData) {
//			list(html, (Iterable) ((ListData)data).iterator(), type, depth);
//		} else if (data instanceof Map) {
//			map(html, (Map) data, type, depth);
//		} else if (data instanceof MappedData) {
//			map(html, ((MappedData)data).getMap(), type, depth);
//		} else if (data instanceof MatrixData) {
//			maxtrix(html, (MatrixData) data, type, depth);
//		} else if (data instanceof ScalarData) {
//			scalar(html, (ScalarData)data, type, depth);
//		} else {
//			other(html, data, type, depth);
//		}
//	}
//	
//	private void maxtrix(StringBuilder html, MatrixData data, Type type, int depth) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	private void list(StringBuilder html, Iterable itrable, Type type, int depth) {
//		String startAll = depth % 3 == 0 ? "<table>\n<tr>\n" : "";
//		String openTag = depth % 2 == 0 ? "<td class='sublist'>" : "<tr class='list'>\n";
//		String endTag = depth % 2 == 0 ? "</td>" : "</tr>\n";
//		String endAll = depth % 3 == 0 ? "</tr>\n</table>\n" : "";
//
//		html.append(startAll);
//		boolean isFirst = true;
//		for (Object o : itrable) {
//			html.append(openTag);
//			format(html, o, isFirst ? Type.HEADER : Type.NORMAL, depth+1);
//			html.append(endTag);
//			isFirst = false;
//		}
//		html.append(endAll);
//	}
//
//	private void map(StringBuilder html, Map m, Type type, int depth) {
//		// append header information
//		if (type.equals(Type.HEADER)) {
//			html.append("<tr class='header'>");
//			for (Object key : m.keySet()) {
//				html.append("<td><b>");
//				format(html, key, type, depth+1);
//				html.append("</b></td>");
//			}
//			html.append("</tr>");
//		}
//		// append standard map values
//		html.append("<tr>");
//		for (Object key : m.keySet()) {
//			html.append("<td class='map'>");
//			Object val = m.get(key);
//			format(html, val, type, depth+1);
//			html.append("</td>");
//		}
//		html.append("</tr>");
//	}
//	
//	private void scalar(StringBuilder html, ScalarData data, Type type, int depth) {
//		if (data instanceof UrlData) {
//			html.append("<a href=\"" + data.toString() + "\">link</a>");
//		} else {
//			other(html, data, type, depth);
//		}
//	}
//
//	private void other(StringBuilder html, Object data, Type type, int depth) {
//		html.append(String.valueOf(data));
//		//System.out.println("Other:" + data); // TODO FIX!! this should NEVER get to other()
//	}
//	
////	public MediaType getMediaType() {
////		return MediaType.TEXT_HTML;
////	}
//
//	@Override
//	public void format(Data data, StreamWriter stream, WutRequestInterface request) {
//		StringBuilder html = new StringBuilder();
//		
//		html.append("<table>\n");
//		format(html, data, Type.HEADER, 1);
//		html.append("</table>\n");
//		
//		try {
//			stream.write(html.toString());
//		} catch (IOException e) {
//			ErrorHandler.systemError("error writing to stream", e);
//		}
//	}
//

}
