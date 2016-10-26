package com.wut.format.json;

//import java.util.Iterator;
//import java.util.Map;
//
//import com.wut.format.GenericFormatter;
//import com.wut.model.Data;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.model.matrix.MatrixData;
//import com.wut.model.scalar.ScalarData;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.ErrorHandler;
//import com.wut.support.StreamWriter;

public class NewJsonFormatter { // extends GenericFormatter {
//
//	@Override
//	public void start(StreamWriter stream, WutRequestInterface request) {
//		write(stream, "{");
//		write(stream, "\"result\":");
//	}
//
//	@Override
//	public void end(StreamWriter stream) {
//		write(stream, "}");
//	}
//
//	@Override
//	public void list(StreamWriter stream, ListData data, Context context) {
//		Iterator<? extends Data> itr = data.iterator();
//		write(stream, "[");
//		while (itr.hasNext()) {
//			Data subdata = itr.next();
//			format(stream, subdata, context);
//			if (itr.hasNext()) {
//				write(stream, ",");
//			}
//		}
//		write(stream, "]");
//	}
//
//	@Override
//	public void map(StreamWriter stream, MappedData data, Context context) {
//		Map<ScalarData, Data> m = data.getMap();
//		write(stream, "{");
//		Iterator<ScalarData> keys = m.keySet().iterator();
//		while (keys.hasNext()) {
//			try {
//				ScalarData scalarKey = keys.next();
//				Data value = m.get(scalarKey);
//				if (value != null) {
//					String key = String.valueOf(scalarKey);
//					String jsonKey = key.replaceAll(" ", "_");
//					write(stream, "\"");
//					write(stream, jsonKey);
//					write(stream, "\"");
//					write(stream, ":");
//					//write(stream, "\"");
//					format(stream, value, context);
//					//write(stream, "\"");
//					if (keys.hasNext()) {
//						write(stream, ", ");
//					}
//				}
//			} catch (Exception e) {
//				ErrorHandler.systemError("error formatting map in json", e);
//			}
//		}
//		write(stream, "}");
//	}
//
//	@Override
//	public void maxtrix(StreamWriter stream, MatrixData data, Context context) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void scalar(StreamWriter stream, ScalarData data, Context context) {
//		write(stream, "\"");
//		String rawData = data.toRawString();
//		rawData = rawData.replaceAll("'", ""); // TODO hack to remove single quotes (will likely remove good quotes too)
//		write(stream, rawData);
//		write(stream, "\"");
//	}

}
