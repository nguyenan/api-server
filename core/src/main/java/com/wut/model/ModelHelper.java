package com.wut.model;

import java.io.IOException;

import com.wut.format.Token;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.model.list.ListData;
import com.wut.model.list.ListModel;
import com.wut.model.map.MapModel;
import com.wut.model.map.MappedData;
import com.wut.model.matrix.MatrixData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.ScalarModel;
import com.wut.support.StreamWriter;
import com.wut.support.logging.StackTraceData;

public class ModelHelper {
	private TokenSet tokens;

	public ModelHelper(TokenSet tokens) {
		this.tokens = tokens;
	}

	// TODO review
	private String getTokenStr(Model<? extends Data> class1, PURPOSE purpose) {
		Token t = tokens.getToken(class1, purpose);
		if (t != null) {
			String tokenStr = tokens.getToken(class1, purpose).getData();
			return tokenStr;
		} else {
			return "";
		}
	}

	// list
	public String getListOpen() {
		return getTokenStr(ListModel.create(), PURPOSE.OPEN);
	}

	public String getListItemOpen() {
		return getTokenStr(ListModel.create(), PURPOSE.ITEM_OPEN);
	}

	public String getListDelimiter() {
		return getTokenStr(ListModel.create(), PURPOSE.DELIMINTER);
	}

	public String getListItemClose() {
		return getTokenStr(ListModel.create(), PURPOSE.ITEM_CLOSE);
	}

	public String getListClose() {
		return getTokenStr(ListModel.create(), PURPOSE.CLOSE);
	}

	// map
	public String getMapOpen() {
		return getTokenStr(MapModel.create(), PURPOSE.OPEN);
	}

	public String getMapKeyOpen() {
		return getTokenStr(MapModel.create(), PURPOSE.KEY_OPEN);
	}

	public String getMapKeyClose() {
		return getTokenStr(MapModel.create(), PURPOSE.KEY_CLOSE);
	}

	public String getMapKeyValueDelimiter() {
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_DELIMINTER);
	}

	public String getMapValueOpen() {
		return getTokenStr(MapModel.create(), PURPOSE.VALUE_OPEN);
	}

	public String getMapValueClose() {
		return getTokenStr(MapModel.create(), PURPOSE.VALUE_CLOSE);
	}

	public String getMapItemDelimiter() {
		return getTokenStr(MapModel.create(), PURPOSE.DELIMINTER);
	}

	public String getMapClose() {
		return getTokenStr(MapModel.create(), PURPOSE.CLOSE);
	}

	// TODO matrix
	// ignore

	// scalar
	public String getScalarOpen() {
		return getTokenStr(ScalarModel.create(), PURPOSE.OPEN);
	}

	public String getScalarClose() {
		return getTokenStr(ScalarModel.create(), PURPOSE.CLOSE);
	}
	
	public static void write(StreamWriter stream, String text) {
		try {
			stream.write(text);
		} catch (IOException e) {
			throw new RuntimeException("io problem writing to stream");
			//ErrorHandler.systemError("io problem writing to stream", e);
		}
	}
	
	// TODO this method needs to be somewhere else
	public static void format(TokenSet tokens, StreamWriter stream, Data data,
			Context context) {
		
		if (data instanceof ListData) {
			ListData list = (ListData) data;
			Model<ListData> model = list.getModel();
			model.format(tokens, stream, list, context);
		} else if (data instanceof StackTraceData) {
			((StackTraceData) data).save();
			MappedData map = (MappedData) data;
			Model<MappedData> model = map.getModel();
			model.format(tokens, stream, map, context);
		} else if (data instanceof MappedData) {
			MappedData map = (MappedData) data;
			Model<MappedData> model = map.getModel();
			model.format(tokens, stream, map, context);
		} else if (data instanceof MatrixData) {
			throw new RuntimeException("matrix data not supported");
		} else if (data instanceof ScalarData) {
			ScalarData scalar = (ScalarData) data;
			Model<ScalarData> model = scalar.getModel();
			model.format(tokens, stream, scalar, context);
		} else {
			throw new RuntimeException("reached unknown data type in ModelHelper");
		}
	}
}
