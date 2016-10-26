package com.wut.format.common;

import com.wut.format.Input;
import com.wut.format.Token;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.format.DecoderError;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.scalar.ScalarModel;
import com.wut.support.logging.WutLogger;

public class AbstractInput implements Input {
	private TokenSet tokens;
	private String input; // TODO rename buffer
	private int pos = 0;
	private static WutLogger logger = WutLogger.create(AbstractInput.class);
	
	public AbstractInput(TokenSet tokens, String inputStr) {
		this.tokens = tokens;
		this.input = inputStr;
	}
	
	// TODO this method can replace eat!!!
	// returns whether or not the token was eaten (consumed, emitted, etc)
	@Override
	public boolean optionalEat(Token token) { // TODO rename eat
		logger.debug("Eating optional token:"+token.getData());
		
		eatWhiteSpace();
		
		String expected = token.getData();
		if (token.matchesBuffer(input, pos)) {
			pos = pos + expected.length();
			return true;
		} else if (!token.isOptional()) {
			throw new DecoderError("Expected token: '" + expected + "' (" + token.getPurpose().toString() + ") but found token: '" + input.substring(pos, pos + expected.length()) + "' at location " + pos + " when parsing input:\n" + input);
		}
		return false;
	}
	
	public void eat(Token token) { // TODO rename eat
		logger.debug("Eating token:"+token.getData());
		
		eatWhiteSpace();
		
		String expected = token.getData();
		if (token.matchesBuffer(input, pos)) {
			pos = pos + expected.length();
		} else { //if (!token.isOptional()) { // TODO for optional tokens
			throw new DecoderError("Expected token: '" + expected + "' but found token: '" + input.substring(pos, pos + expected.length()) + "' at location " + pos + " when parsing input:\n" + input);
		}
	}
	
	public String emit() { // TODO for variables
		ScalarModel scalarModel = ScalarModel.create();
		String scalarClose = tokens.getToken(scalarModel, PURPOSE.CLOSE).getData();
		int firstLocation = pos + input.substring(pos).indexOf(scalarClose);
		// if the close character is escaped, keep looking // TODO make escape character generic and use tokens.getToken() or tokens.getEscapeCharacter()
		while (firstLocation > 0 && input.charAt(firstLocation-1) == '\\') {
			firstLocation += input.substring(firstLocation+1).indexOf(scalarClose) + 1;
		}
		String toEmit = input.substring(pos, firstLocation);
		pos = firstLocation;
		
		logger.debug("Emitting token:"+toEmit);
		return toEmit;
		// TODO doesn't handle quotes within quotes (backslashed)
	}
	
	public boolean checkNext(Token t) {
		return checkNext(t, pos, true);
	}
	
	public boolean checkNext(Token t, int startPos, boolean ignoreWhiteSpace) { // TODO rename peek
		if (!t.isOptional() && !t.getPurpose().equals(PURPOSE.WHITE_SPACE))
			//System.out.println("Checking token:"+t.getData());
		
		if (ignoreWhiteSpace == true) {
			startPos = startPos + findWhiteSpace();
		}
		
		return t.matchesBuffer(input, startPos);
	}
	
	public void eatWhiteSpace() {
		final int whiteSpaceLength = findWhiteSpace();
		if (whiteSpaceLength > 0) {
			logger.debug("Eating whitespace (" + whiteSpaceLength + ")");
		}
		pos = pos + whiteSpaceLength;
	}
	
	// TODO rename nextWhiteSpaceLocation
	public int findWhiteSpace() {
		int whiteSpace = 0;
		int position = pos;
		boolean foundWhiteSpace;
		do {
			foundWhiteSpace = false;
			for (Token t : tokens.getTokens(PURPOSE.WHITE_SPACE)) {
				if (checkNext(t, position, false)) {
					position = position + t.getData().length();
					whiteSpace++;
					foundWhiteSpace = true;
					break;
				}
			}
		} while (foundWhiteSpace);
		
		return whiteSpace;
	}

	@Override
	public Data parse() {
		logger.debug("Parsing...");
		
		eatWhiteSpace();

		for (Token t : tokens.getTokens(PURPOSE.OPEN)) {
			if (checkNext(t)) {
				Model<? extends Data> model = t.getModel();
//				Constructor modelConstructor = modelClass.getConstructor();
//				Data modelObject = (Data) modelConstructor.newInstance();
				Data parsedData = model.parse(tokens, this);
				return parsedData;
			}
		}
		
		ScalarModel scalarModel = ScalarModel.create();
		return scalarModel.parse(tokens, this);
		//return parseScalar(input); // TODO should default be to parse scalar
	}
	
}
