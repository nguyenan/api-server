package com.wut.model;

//import javax.xml.crypto.KeySelector.Purpose;

import com.wut.format.Input;
import com.wut.format.Token;
import com.wut.format.TokenSet;
import com.wut.format.Token.PURPOSE;
import com.wut.support.StreamWriter;


public class DocumentModel implements Model<Data> {
	private static DocumentModel singleton = new DocumentModel();
	
	private DocumentModel() {
		
	}
	
	public static DocumentModel create() {
		return singleton;
	}

	@Override
	public Data parse(TokenSet tokens, Input input) {
		Token startOfDocument = tokens.getToken(this, PURPOSE.OPEN);
		input.eat(startOfDocument);
		
		Data data = input.parse();

		Token endOfDocument = tokens.getToken(this, PURPOSE.CLOSE);
		input.eat(endOfDocument);
		
		return data;
	}

	@Override
	public void format(TokenSet tokens, StreamWriter stream, Data data,
			Context context) {
		Token startOfDocument = tokens.getToken(this, PURPOSE.OPEN);
		ModelHelper.write(stream, startOfDocument.getData());
		
		ModelHelper.format(tokens, stream, data, context);

		Token endOfDocument = tokens.getToken(this, PURPOSE.CLOSE);
		ModelHelper.write(stream, endOfDocument.getData());
	}

}
