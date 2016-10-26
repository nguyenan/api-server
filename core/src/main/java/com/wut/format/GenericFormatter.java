package com.wut.format;

import java.io.ByteArrayOutputStream;

import com.wut.format.Token.PURPOSE;
import com.wut.model.Context;
import com.wut.model.Data;
import com.wut.model.DocumentModel;
import com.wut.model.ModelHelper;
import com.wut.pipeline.WutRequestInterface;
import com.wut.support.StreamWriter;

// TODO rename GenericEncoder
public class GenericFormatter implements Formatter {
	
//	public static class Context {
//		public enum PositionInfo { FIRST, MIDDLE, LAST }
//		public PositionInfo info = PositionInfo.FIRST;
//		public int depth = 1;
//		public int position = 0;
//		//public void reset() { depth=1;position=0;info = PositionInfo.FIRST; }
//	}

	private TokenSet tokens;
	private Token end;
	private Token start;
	
	public GenericFormatter(TokenSet tokens) {
		this.tokens = tokens;
	}
	
	@Override
	public void start(StreamWriter stream) {
		Token start = tokens.getToken(DocumentModel.create(), PURPOSE.OPEN);
		if (start != null) {
			ModelHelper.write(stream, start.getData());
		}
	}

	@Override
	public void end(StreamWriter stream) {
		Token end = tokens.getToken(DocumentModel.create(), PURPOSE.CLOSE);
		if (end != null) {
			ModelHelper.write(stream, end.getData());
		}
	}

	@Override
	public void format(Data data, StreamWriter stream) {
		start(stream);
		format(stream, data, new Context());
		end(stream);
	}
	
	@Override
	public String format(Data data) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		StreamWriter streamWriter = StreamWriter.create(stream);
		start(streamWriter);
		format(streamWriter, data, new Context());
		end(streamWriter);
		
		String encodedData = new String(stream.toByteArray());
		return encodedData;
	}
	
	
	public void format(StreamWriter stream, Data data, Context context) {
		ModelHelper.format(tokens, stream, data, context);
	}
	
	protected TokenSet getTokens() {
		return tokens;
	}
	
	protected Token getStartToken() {
		return start;
	}
	
	protected Token getEndToken() {
		return end;
	}
	
}
