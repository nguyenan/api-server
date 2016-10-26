package com.wut.model.list;

import java.util.Iterator;

import com.wut.format.Input;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.model.Context;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.ModelHelper;
import com.wut.support.StreamWriter;

public class ListModel implements Model<ListData> {
	private static ListModel singleton = new ListModel();
	
	private ListModel() {
		
	}
	
	public static Model<ListData> create() {
		return singleton;
	}

	@Override
	public ListData parse(TokenSet tokens, Input input) {
		// System.out.println("Parsing List...");

		ListData list = new ListData();
		input.eat(tokens.getToken(this, PURPOSE.OPEN));

		// TODO handle empty list

		// FIRST ITEM
		Data firstItem = input.parse();
		list.add(firstItem);

		// REST OF LIST
		while (input.checkNext(tokens.getToken(this,
				PURPOSE.DELIMINTER))) {
			input.eat(tokens.getToken(this, PURPOSE.DELIMINTER));
			Data item = input.parse();
			list.add(item);
		}

		input.eat(tokens.getToken(this, PURPOSE.CLOSE));

		// System.out.println("Found " + list);
		return list;
	}

	@Override
	public void format(TokenSet tokens, StreamWriter stream, ListData data,
			Context context) {
		ModelHelper characterSet = new ModelHelper(tokens);
		
		Iterator<? extends Data> itr = data.iterator();
		
		ModelHelper.write(stream, characterSet.getListOpen());
		while (itr.hasNext()) {
			Data subdata = itr.next();
			ModelHelper.write(stream, characterSet.getListItemOpen());
			ModelHelper.format(tokens, stream, subdata, context);
			ModelHelper.write(stream, characterSet.getListItemClose());
			if (itr.hasNext()) {
				ModelHelper.write(stream, characterSet.getListDelimiter());
			}
		}
		ModelHelper.write(stream, characterSet.getListClose());
	}




}
