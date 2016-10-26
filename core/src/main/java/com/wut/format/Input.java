package com.wut.format;

import com.wut.model.Data;

public interface Input {
	public boolean optionalEat(Token token);
	
	public void eat(Token token);
	
	public String emit();
	
	public boolean checkNext(Token t); // TODO rename peek
	
	// TODO rename peek
	public boolean checkNext(Token t, int startPos, boolean ignoreWhiteSpace);
	
	public void eatWhiteSpace();
	
	public int findWhiteSpace();

	public Data parse();
	
}
