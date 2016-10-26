package com.wut.format.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//import com.google.common.collect.ArrayListMultimap;
import com.wut.format.Token;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.model.Data;
import com.wut.model.Model;

public class GenericTokenSet implements TokenSet {

	// private Map<TokenIdentifier, Token> characterSet = new
	// HashMap<TokenIdentifier, Token>();
	private Map<Model<? extends Data>, Collection<Token>> tokensByModel = new HashMap<Model<? extends Data>, Collection<Token>>();
	private Map<PURPOSE, Collection<Token>> tokensByPurpose = new HashMap<Token.PURPOSE, Collection<Token>>();
	private Collection<Token> allTokens = new ArrayList<Token>();
	// HashMultimap

	@Override
	public void add(Token t) {
		if (!tokensByModel.containsKey(t.getModel())) {
			Collection<Token> tokens = new ArrayList<Token>();
			tokens.add(t);
			tokensByModel.put(t.getModel(), tokens);
		} else {
			Collection<Token> tokens = tokensByModel.get(t.getModel());
			tokens.add(t);
		}
		//tokensByModel.put(t.getModel(), t);
		if (!tokensByPurpose.containsKey(t.getPurpose())) {
			Collection<Token> tokens = new ArrayList<Token>();
			tokens.add(t);
			tokensByPurpose.put(t.getPurpose(), tokens);
		} else {
			Collection<Token> tokens = tokensByPurpose.get(t.getPurpose());
			tokens.add(t);
		}
		//tokensByPurpose.put(t.getPurpose(), t);
		allTokens.add(t);
	}

	// public Token getToken(String data) {
	// return characterSet.get(tokenModel);
	// }
	
	@Override
	public Collection<Token> getTokens() {
		return allTokens;
	}

	@Override
	public Collection<Token> getTokens(Model<? extends Data> modelClass) {
		return tokensByModel.get(modelClass);
	}

	@Override
	public Collection<Token> getTokens(PURPOSE p) {
		return tokensByPurpose.get(p);
	}

	@Override
	public Token getToken(Model<? extends Data> model, PURPOSE p) {
		Collection<Token> byModel = getTokens(model);
		for (Token t : byModel) {
			if (t.getPurpose() == p) {
				return t;
			}
		}
		return null;
	}
}

/*
 * 
public class GenericTokenSet implements TokenSet {

	// private Map<TokenIdentifier, Token> characterSet = new
	// HashMap<TokenIdentifier, Token>();
	private ArrayListMultimap<Model<? extends Data>, Token> tokensByModel = ArrayListMultimap
			.create();
	private ArrayListMultimap<PURPOSE, Token> tokensByPurpose = ArrayListMultimap
			.create();

	// HashMultimap

	@Override
	public void add(Token t) {
		tokensByModel.put(t.getModel(), t);
		tokensByPurpose.put(t.getPurpose(), t);
	}

	// public Token getToken(String data) {
	// return characterSet.get(tokenModel);
	// }
	
	@Override
	public Collection<Token> getTokens() {
		return tokensByModel.values();
	}

	@Override
	public Collection<Token> getTokens(Model<? extends Data> modelClass) {
		return tokensByModel.get(modelClass);
	}

	@Override
	public Collection<Token> getTokens(PURPOSE p) {
		return tokensByPurpose.get(p);
	}

	@Override
	public Token getToken(Model<? extends Data> model, PURPOSE p) {
		Collection<Token> byModel = getTokens(model);
		for (Token t : byModel) {
			if (t.getPurpose() == p) {
				return t;
			}
		}
		return null;
	}
}
*/
