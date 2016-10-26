package com.wut.format.json;

import com.wut.format.CharacterSet;
import com.wut.format.Token.PURPOSE;
import com.wut.format.common.AbstractToken;
import com.wut.format.common.GenericTokenSet;
import com.wut.model.DocumentModel;
import com.wut.model.list.ListModel;
import com.wut.model.map.MapModel;
import com.wut.model.scalar.ScalarModel;

public class JsonCharacterSet implements CharacterSet {
	
	private GenericTokenSet tokens = new GenericTokenSet();

	@Override
	public GenericTokenSet getTokens() { // TODO rename getTokenSet
		return tokens;
	}

	public JsonCharacterSet() {
		// DOCUMENT
		tokens.add(new AbstractToken(DocumentModel.create(), PURPOSE.OPEN, "{\"result\":"));
		tokens.add(new AbstractToken(DocumentModel.create(), PURPOSE.CLOSE, "}"));
		
		
		// SCALAR
		tokens.add(new AbstractToken(ScalarModel.create(), PURPOSE.OPEN, "\"", true));
		tokens.add(new AbstractToken(ScalarModel.create(), PURPOSE.CLOSE, "\"", true));

		// LIST
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.OPEN, "["));
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.CLOSE, "]"));
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.DELIMINTER, ","));
		
		// MAP
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.OPEN, "{"));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.CLOSE, "}"));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.DELIMINTER, ","));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.ITEM_DELIMINTER, ":"));
		
		// WHITESPACE
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, " "));
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, "\t"));
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, "\n"));
	}
}


