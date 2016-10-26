package com.wut.format;

import java.util.Collection;

import com.wut.format.Token.PURPOSE;
import com.wut.model.Data;
import com.wut.model.Model;

public interface TokenSet {

	// TODO does add need to be part of the API
	public void add(Token t);

	public Collection<Token> getTokens();
	
	public Collection<Token> getTokens(Model<? extends Data> modelClass);
	
	public Collection<Token> getTokens(PURPOSE p);

	public Token getToken(Model<? extends Data> model, PURPOSE p);
}
