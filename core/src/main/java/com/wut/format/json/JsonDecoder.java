package com.wut.format.json;

import java.io.IOException;
import java.io.InputStream;

import com.wut.format.Decoder;
import com.wut.format.Token;
import com.wut.format.Token.PURPOSE;
import com.wut.format.common.AbstractInput;
import com.wut.format.common.GenericTokenSet;
import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.list.ListModel;
import com.wut.model.map.MapModel;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.ScalarModel;
import com.wut.model.scalar.StringData;

// TODO this is no longer json specific!!! a cbaracterSet could easily be passed into this.
public class JsonDecoder implements Decoder {

	private static JsonCharacterSet characterSet = new JsonCharacterSet();
	private static GenericTokenSet tokens = characterSet.getTokens();
	private static int MAX_INPUT_SIZE_BYTES = 1024;
	
	public static void main(String[] args) {
		// TODO implement option tokens
		//String inputStr = "{'one' : '1', 'two' : {'three':'three'},'four':'4','five':{'six':'6'}}";
		String inputStr = "{\"config\":\"{\\\"title\\\":\\\"WUT Requester\\\", size:\\\"1\\\"}\"}}";

		JsonDecoder decoder = new JsonDecoder();
		AbstractInput input = new AbstractInput(tokens, inputStr);
		Data d = decoder.parse(input);
		System.out.println("Parsed data=" + d);
	}
	
	public Data parse(AbstractInput input) {
		//System.out.println("Parsing...");
		
		input.eatWhiteSpace();

		for (Token t : tokens.getTokens(PURPOSE.OPEN)) {
			if (input.checkNext(t)) {
				if (t.getModel() instanceof ScalarModel) {
					return parseScalar(input);
				} else if (t.getModel() instanceof ListModel) {
					return parseList(input);
				} else if (t.getModel() instanceof MapModel) {
					return parseMap(input);
				}
			}
		}
		
		return parseScalar(input); // TODO should default be to parse scalar
	}
	
	private Data parseScalar(AbstractInput input) {
		//System.out.println("Parsing Scalar...");
		
		input.optionalEat(tokens.getToken(ScalarModel.create(), PURPOSE.OPEN));
		String str = input.emit();
		Data d = new StringData(str);
		input.optionalEat(tokens.getToken(ScalarModel.create(), PURPOSE.CLOSE));
		
		//System.out.println("Found " + d);
		return d;
	}
	
	private ListData parseList(AbstractInput input) {
		//System.out.println("Parsing List...");

		ListData list = new ListData();
		input.eat(tokens.getToken(ListModel.create(), PURPOSE.OPEN));
		
		// TODO handle empty list
		
		// FIRST ITEM
		Data firstItem = parse(input);
		list.add(firstItem);
		
		// REST OF LIST
		while (input.checkNext(tokens.getToken(ListModel.create(), PURPOSE.DELIMINTER))) {
			input.eat(tokens.getToken(ListModel.create(), PURPOSE.DELIMINTER));
			Data item = parse(input);
			list.add(item);
		}
		
		input.eat(tokens.getToken(ListModel.create(), PURPOSE.CLOSE));
		
		//System.out.println("Found " + list);
		return list;
	}
	
	private MappedData parseMap(AbstractInput input) {
		//System.out.println("Parsing Map...");

		MappedData map = new MappedData();
		input.eat(tokens.getToken(MapModel.create(), PURPOSE.OPEN));
		
		if (!input.checkNext(tokens.getToken(MapModel.create(), PURPOSE.CLOSE))) {
			// FIRST KEY
			ScalarData firstKey = (ScalarData) parseScalar(input);
			
			// FIRST VALUE
			input.eat(tokens.getToken(MapModel.create(), PURPOSE.ITEM_DELIMINTER));
			Data firstValue = parse(input);
			map.put(firstKey, firstValue);
			
			// REST OF MAP
			while (input.checkNext(tokens.getToken(MapModel.create(), PURPOSE.DELIMINTER))) {
				// KEY
				input.eat(tokens.getToken(MapModel.create(), PURPOSE.DELIMINTER));
				ScalarData key = (ScalarData) parseScalar(input);
				
				input.eat(tokens.getToken(MapModel.create(), PURPOSE.ITEM_DELIMINTER));
				
				// VALUE
				Data value = parse(input);
				map.put(key, value);
			}
		}
		
		input.eat(tokens.getToken(MapModel.create(), PURPOSE.CLOSE));
		
		//System.out.println("Found " + map);
		return map;
	}
	
//	private void printTokens(Collection<AbstractToken> tokens) {
//		//System.out.println("Printing Tokens...");
//
//		int i = 0;
//		for (AbstractToken t : tokens) {
//			System.out.println(i++ + "-" + t.getData());
//		}
//	}
	
	@Override
	public Data decode(InputStream input) throws IOException {
		
		byte[] buffer = new byte[MAX_INPUT_SIZE_BYTES];
		@SuppressWarnings("unused")
		int bytesRead = input.read(buffer, 0, MAX_INPUT_SIZE_BYTES);
		String str = new String(buffer);
		StringBuilder s = new StringBuilder(str);
		AbstractInput is = new AbstractInput(tokens, s.toString());
		Data d = parse(is);
		
		return d;
	}

	@Override
	public Data decode(String input) throws IOException {
		//System.out.println("INPUT:" + input);
		
		AbstractInput is = new AbstractInput(tokens, input);
		Data d = parse(is);
		
		return d;
	}
	
}
