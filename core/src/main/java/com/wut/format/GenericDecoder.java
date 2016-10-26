package com.wut.format;

import java.io.IOException;
import java.io.InputStream;

import com.wut.format.common.AbstractInput;
import com.wut.model.Data;

public class GenericDecoder implements Decoder {
	//private CharacterSet characterSet;
	private TokenSet tokens;

	public GenericDecoder(CharacterSet characterSet) {
		//this.characterSet = characterSet;
		this.tokens = characterSet.getTokens();
	}
	
	public GenericDecoder(TokenSet tokens) {
		this.tokens = tokens;
	}
	
	private static int MAX_INPUT_SIZE_BYTES = 1024;
	
	public static void main(String[] args) {
//		// TODO implement option tokens
//		//String inputStr = "{'one' : '1', 'two' : {'three':'three'},'four':'4','five':{'six':'6'}}";
//		String inputStr = "{\"config\":\"{\\\"title\\\":\\\"WUT Requester\\\", size:\\\"1\\\"}\"}}";
//
//		JsonDecoder decoder = new JsonDecoder();
//		Input input = new Input(tokens, inputStr);
//		Data d = decoder.parse(input);
//		//System.out.println("Parsed data=" + d);
	}
	
	public static class DecoderError extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DecoderError(String string) {
			super(string);
		}
	}
	
	//////////// THIS MOVED INTO "MODEL" CLASSES /////////////////
	
	/*
	public Data parse(AbstractInput input) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		//System.out.println("Parsing...");
		
		input.eatWhiteSpace();

		for (Token t : tokens.getTokens(PURPOSE.OPEN)) {
			if (input.checkNext(t)) {
				Class modelClass = t.getModel();
				Constructor modelConstructor = modelClass.getConstructor();
				Data modelObject = (Data) modelConstructor.newInstance();
				Data parsedData = modelObject.parse(tokens, input);
				return parsedData;
//				if (t.getModel() == ScalarData.class) {
//					return parseScalar(input);
//				} else if (t.getModel() == ListData.class) {
//					return parseList(input);
//				} else if (t.getModel() == MappedData.class) {
//					return parseMap(input);
//				}
			}
		}
		
		return parseScalar(input); // TODO should default be to parse scalar
	}
	
	private Data parseScalar(AbstractInput input) {
		//System.out.println("Parsing Scalar...");
		
		input.optionalEat(tokens.getToken(ScalarData.class, PURPOSE.OPEN));
		String str = input.emit();
		Data d = new StringData(str);
		input.optionalEat(tokens.getToken(ScalarData.class, PURPOSE.CLOSE));
		
		//System.out.println("Found " + d);
		return d;
	}
	
	private ListData parseList(AbstractInput input) {
		//System.out.println("Parsing List...");

		ListData list = new ListData();
		input.eat(tokens.getToken(ListData.class, PURPOSE.OPEN));
		
		// TODO handle empty list
		
		// FIRST ITEM
		Data firstItem = parse(input);
		list.add(firstItem);
		
		// REST OF LIST
		while (input.checkNext(tokens.getToken(ListData.class, PURPOSE.DELIMINTER))) {
			input.eat(tokens.getToken(ListData.class, PURPOSE.DELIMINTER));
			Data item = parse(input);
			list.add(item);
		}
		
		input.eat(tokens.getToken(ListData.class, PURPOSE.CLOSE));
		
		//System.out.println("Found " + list);
		return list;
	}
	
	private MappedData parseMap(AbstractInput input) {
		//System.out.println("Parsing Map...");

		MappedData map = new MappedData();
		input.eat(tokens.getToken(MappedData.class, PURPOSE.OPEN));
		
		if (!input.checkNext(tokens.getToken(MappedData.class, PURPOSE.CLOSE))) {
			// FIRST KEY
			ScalarData firstKey = (ScalarData) parseScalar(input);
			
			// FIRST VALUE
			input.eat(tokens.getToken(MappedData.class, PURPOSE.ITEM_DELIMINTER));
			Data firstValue = parse(input);
			map.put(firstKey, firstValue);
			
			// REST OF MAP
			while (input.checkNext(tokens.getToken(MappedData.class, PURPOSE.DELIMINTER))) {
				// KEY
				input.eat(tokens.getToken(MappedData.class, PURPOSE.DELIMINTER));
				ScalarData key = (ScalarData) parseScalar(input);
				
				input.eat(tokens.getToken(MappedData.class, PURPOSE.ITEM_DELIMINTER));
				
				// VALUE
				Data value = parse(input);
				map.put(key, value);
			}
		}
		
		input.eat(tokens.getToken(MappedData.class, PURPOSE.CLOSE));
		
		//System.out.println("Found " + map);
		return map;
	}
	
	*/
	
//	private void printTokens(Collection<AbstractToken> tokens) {
//		//System.out.println("Printing Tokens...");
//
//		int i = 0;
//		for (AbstractToken t : tokens) {
//			System.out.println(i++ + "-" + t.getData());
//		}
//	}
	
	/* (non-Javadoc)
	 * @see com.wut.format.Decoder#decode(java.io.InputStream)
	 */
	@Override
	public Data decode(InputStream input) throws IOException {
		
		byte[] buffer = new byte[MAX_INPUT_SIZE_BYTES];
		int bytesRead = input.read(buffer, 0, MAX_INPUT_SIZE_BYTES);
		
		if (bytesRead == MAX_INPUT_SIZE_BYTES) {
			System.err.println("decoding a file thats too large to decode -- cropping file");
		}
		
		String str = new String(buffer);
		StringBuilder s = new StringBuilder(str);
		
		Data d = decode(s.toString());
		
//		AbstractInput is = new AbstractInput(tokens, s.toString());
//		Data d = is.parse();
		
		return d;
	}

	/* (non-Javadoc)
	 * @see com.wut.format.Decoder#decode(java.lang.String)
	 */
	@Override
	public Data decode(String input) throws IOException {
		//System.out.println("INPUT:" + input);
		
		AbstractInput is = new AbstractInput(tokens, input);
		Data d = is.parse();
		
		return d;
	}
	
}
