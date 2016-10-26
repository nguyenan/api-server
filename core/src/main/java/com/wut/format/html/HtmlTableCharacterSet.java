package com.wut.format.html;

import com.wut.format.CharacterSet;
import com.wut.format.Token.PURPOSE;
import com.wut.format.common.AbstractToken;
import com.wut.format.common.GenericTokenSet;
//import com.wut.model.Data;
import com.wut.model.list.ListModel;
import com.wut.model.map.MapModel;
import com.wut.model.scalar.ScalarModel;

public class HtmlTableCharacterSet implements CharacterSet {
	
	private GenericTokenSet tokens = new GenericTokenSet();

	@Override
	public GenericTokenSet getTokens() { // TODO rename getTokenSet
		return tokens;
	}

	public HtmlTableCharacterSet() {
		// SCALAR
		tokens.add(new AbstractToken(ScalarModel.create(), PURPOSE.OPEN, "", true));
		tokens.add(new AbstractToken(ScalarModel.create(), PURPOSE.CLOSE, "", true));

		// LIST
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.OPEN, "<tr>"));
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.CLOSE, "</tr>"));
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.NESTED_ITEM_OPEN, "<td>"));
		tokens.add(new AbstractToken(ListModel.create(), PURPOSE.NESTED_ITEM_CLOSE, "</td>"));
		//tokens.add(new AbstractToken(ListModel.create(), PURPOSE.DELIMINTER, ","));
		
		// MAP
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.OPEN, "<tr class='map'>"));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.CLOSE, "</tr>"));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.NESTED_ITEM_OPEN, "<td>"));
		tokens.add(new AbstractToken(MapModel.create(), PURPOSE.NESTED_ITEM_CLOSE, "</td>"));
		
		// WHITESPACE
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, " "));
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, "\t"));
		tokens.add(new AbstractToken(null, PURPOSE.WHITE_SPACE, "\n"));
	}
}


