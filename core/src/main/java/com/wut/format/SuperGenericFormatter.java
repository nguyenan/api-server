package com.wut.format;

//import java.util.Iterator;
//import java.util.Map;
//
//import com.wut.format.common.FCharacterSet;
//import com.wut.model.Data;
//import com.wut.model.list.ListData;
//import com.wut.model.map.MappedData;
//import com.wut.model.matrix.MatrixData;
//import com.wut.model.scalar.ScalarData;
//import com.wut.pipeline.WutRequestInterface;
//import com.wut.support.ErrorHandler;
//import com.wut.support.StreamWriter;

public class SuperGenericFormatter { //extends GenericFormatter {
//	protected FCharacterSet characterSet;
//	
//	public SuperGenericFormatter(FCharacterSet characterSet) {
//		this.characterSet = characterSet;
//	}
//
//	@Override
//	public void start(StreamWriter stream, WutRequestInterface request) {
//		write(stream, characterSet.getFormatOpen());
//	}
//
//	@Override
//	public void end(StreamWriter stream) {
//		write(stream, characterSet.getFormatClose());
//	}
//
//	@Override
//	public void list(StreamWriter stream, ListData data, Context context) {
//		Iterator<? extends Data> itr = data.iterator();
//		write(stream, characterSet.getListOpen());
//		while (itr.hasNext()) {
//			Data subdata = itr.next();
//			write(stream, characterSet.getListItemOpen());
//			format(stream, subdata, context);
//			write(stream, characterSet.getListItemClose());
//			if (itr.hasNext()) {
//				write(stream, characterSet.getListDelimiter());
//			}
//		}
//		write(stream, characterSet.getListClose());
//	}
//
//	@Override
//	public void map(StreamWriter stream, MappedData data, Context context) {
//		Map<ScalarData, Data> m = data.getMap();
//		write(stream, characterSet.getMapOpen());
//		Iterator<ScalarData> keys = m.keySet().iterator();
//		while (keys.hasNext()) {
//			try {
//				ScalarData key = keys.next();
//				Data value = m.get(key);
//				if (value != null) {
//					String jsonKey = key.toRawString().replaceAll(" ", "_"); // TODO hacky
//					write(stream, characterSet.getMapKeyOpen());
//					
//					// TREAT LIKE SCALAR
//					write(stream, characterSet.getScalarOpen());
//					write(stream, jsonKey);
//					write(stream, characterSet.getScalarClose());
//					
//					write(stream, characterSet.getMapKeyClose());
//					write(stream, characterSet.getMapKeyValueDelimiter());
//
//					write(stream, characterSet.getMapValueOpen());
//					format(stream, value, context);
//					write(stream, characterSet.getMapValueClose());
//
//					if (keys.hasNext()) { // TODO this does not account for the next thing being null, in which case you'll have an extra trailing comma/map delimiter
//						write(stream, characterSet.getMapItemDelimiter());
//					}
//				}
//			} catch (Exception e) {
//				ErrorHandler.systemError("error formatting map in json", e);
//			}
//		}
//		write(stream, characterSet.getMapClose());
//	}
//
//	@Override
//	public void maxtrix(StreamWriter stream, MatrixData data, Context context) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void scalar(StreamWriter stream, ScalarData data, Context context) {
//		write(stream, characterSet.getScalarOpen());
//		String rawData = data.toRawString();
//		write(stream, rawData);
//		write(stream, characterSet.getScalarClose());
//	}

}
