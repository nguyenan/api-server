package com.wut.model.scalar;

import com.wut.format.Input;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.model.Context;
import com.wut.model.Model;
import com.wut.model.ModelHelper;
import com.wut.support.StreamWriter;

public class ScalarModel implements Model<ScalarData> {
	private static ScalarModel singleton = new ScalarModel();
	
	private ScalarModel() {
		
	}
	public static ScalarModel create() {
		return singleton;
	}
	
	@Override
	public ScalarData parse(TokenSet tokens, Input input) {
		input.optionalEat(tokens.getToken(this, PURPOSE.OPEN));
		String str = input.emit();
		ScalarData d = new StringData(str);
		input.optionalEat(tokens.getToken(this, PURPOSE.CLOSE));
		return d;
	}
	
	
	@Override
	public void format(TokenSet tokens, StreamWriter stream, ScalarData data,
			Context context) {
		ModelHelper characterSet = new ModelHelper(tokens);
		
		ModelHelper.write(stream, characterSet.getScalarOpen());
		String rawData = data.toRawString();
		ModelHelper.write(stream, rawData);
		ModelHelper.write(stream, characterSet.getScalarClose());
		
	}
}
