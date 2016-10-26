package com.wut.format;

import com.wut.format.common.GenericTokenSet;

// TODO make handle more than one way to format an item
public interface CharacterSet {
	
	//public abstract Token getToken(TokenIdentifier tokenModel);
	//public List<Token> getTokensForModel();
	//public abstract Collection<Token> getAllTokens();
	//public abstract Collection<Token> getTokensForModel(Class<? extends Data> modelClass);
	
	// TODO rename getTokenSet
	public GenericTokenSet getTokens();
	// preamble
	// postamble
}
