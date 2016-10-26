package com.wut.format.common;

import com.wut.format.Token;
import com.wut.format.Token.PURPOSE;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.list.ListModel;
import com.wut.model.map.MapModel;
import com.wut.model.scalar.ScalarModel;

// TODO make handle more than one way to format an item
// TODO rename to CharacterSet
public class FCharacterSet {

	private GenericTokenSet tokens;
	private AbstractToken close;
	private AbstractToken open;

	public FCharacterSet(AbstractToken open, GenericTokenSet ts, AbstractToken close) {
		this.tokens = ts;
		this.open = open;
		this.close = close;
	}

	// general for format
	public String getFormatOpen() {
		return open.getData();
	}

	public String getFormatClose() {
		return close.getData();
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
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_OPEN);
	}

	public String getMapKeyClose() {
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_CLOSE);
	}

	public String getMapKeyValueDelimiter() {
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_DELIMINTER);
	}

	public String getMapValueOpen() {
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_OPEN);
	}

	public String getMapValueClose() {
		return getTokenStr(MapModel.create(), PURPOSE.ITEM_CLOSE);
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

}
