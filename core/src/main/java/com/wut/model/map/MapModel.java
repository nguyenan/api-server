package com.wut.model.map;

import java.util.Iterator;
import java.util.Map;
import com.wut.format.Input;
import com.wut.format.Token.PURPOSE;
import com.wut.format.TokenSet;
import com.wut.model.Context;
import com.wut.model.Data;
import com.wut.model.Model;
import com.wut.model.ModelHelper;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.ScalarModel;
import com.wut.support.StreamWriter;

public class MapModel implements Model<MappedData> {
	private static MapModel singleton = new MapModel();
	
	private MapModel() {
		
	}
	
	public static Model<MappedData> create() {
		return singleton;
	}
	
	@Override
	public MappedData parse(TokenSet tokens, Input input) {
		MappedData map = new MappedData();
		input.eat(tokens.getToken(this, PURPOSE.OPEN));
		
		if (!input.checkNext(tokens.getToken(this, PURPOSE.CLOSE))) {
			// FIRST KEY
			//ScalarData firstKey = (ScalarData) parseScalar(input);
			ScalarData firstKey = ScalarModel.create().parse(tokens, input);
			
			
			// FIRST VALUE
			input.eat(tokens.getToken(this, PURPOSE.ITEM_DELIMINTER));
			//Data firstValue = parse(input);
			Data firstValue = input.parse();
			map.put(firstKey, firstValue);
			
			// REST OF MAP
			while (input.checkNext(tokens.getToken(this, PURPOSE.DELIMINTER))) {
				// KEY
				input.eat(tokens.getToken(this, PURPOSE.DELIMINTER));
				ScalarModel scalarModel = ScalarModel.create();
				ScalarData key = scalarModel.parse(tokens, input);
				
				input.eat(tokens.getToken(this, PURPOSE.ITEM_DELIMINTER));
				
				// VALUE
				Data value = input.parse();
				map.put(key, value);
			}
		}
		
		input.eat(tokens.getToken(this, PURPOSE.CLOSE));
		
		//System.out.println("Found " + map);
		return map;
	}
	
	@Override
	public void format(TokenSet tokens, StreamWriter stream, MappedData data, Context context) {
		ModelHelper characterSet = new ModelHelper(tokens);
		
		Map<ScalarData, Data> m = data.getMap();
		ModelHelper.write(stream, characterSet.getMapOpen());
		Iterator<ScalarData> keys = m.keySet().iterator();
		boolean fistMapElement = true;
		
		while (keys.hasNext()) {
			ScalarData key = keys.next();
			Data value = m.get(key);
			
			if (value != null) {
				// no delimeter if this is the last key in the map
				if (fistMapElement) {
					fistMapElement = false;
				} else {
					ModelHelper.write(stream, characterSet.getMapItemDelimiter());
				}
				ModelHelper.write(stream, characterSet.getMapKeyOpen());
				
				// TREAT LIKE SCALAR
				ModelHelper.write(stream, characterSet.getScalarOpen());
				String jsonKey = key.toRawString().replaceAll(" ", "_"); // TODO hacky
				ModelHelper.write(stream, jsonKey);
				ModelHelper.write(stream, characterSet.getScalarClose());
				
				ModelHelper.write(stream, characterSet.getMapKeyClose());
				ModelHelper.write(stream, characterSet.getMapKeyValueDelimiter());

				ModelHelper.write(stream, characterSet.getMapValueOpen());
				ModelHelper.format(tokens, stream, value, context);
				ModelHelper.write(stream, characterSet.getMapValueClose());

			}
			
		}
		
		ModelHelper.write(stream, characterSet.getMapClose());
		
	}


}
