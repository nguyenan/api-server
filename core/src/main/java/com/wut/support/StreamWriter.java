package com.wut.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;


// TODO name Output
// TODO embed Context in here
// TODO use inheretence instead of this weird output stream or writer pattern 
public class StreamWriter {
	private OutputStream outputStream;
	private Writer writerStream;
	
	public StreamWriter(OutputStream stream) {
		this.outputStream = stream;
	}

	public StreamWriter(Writer stream) {
		this.writerStream = stream;
	}
	
	public void write(String content) throws IOException {
		if (writerStream != null) {
			writerStream.write(content);
		} else if (outputStream != null) {
			outputStream.write(content.getBytes());
		}
		//System.out.println(content);
	}
	
	public static StreamWriter create(OutputStream stream) {
		return new StreamWriter(stream);
	}
	
	public static StreamWriter create(Writer stream) {
		return new StreamWriter(stream);
	}
	
//	@Override
//	public void format(TokenSet tokens, StreamWriter stream, Data data, WutRequestInterface request) {
//		start(tokens, stream, request);
//		
//		format(tokens, stream, data, new Context());
//		
//		end(tokens, stream);
//	}
	


//	public void format(TokenSet tokens, StreamWriter stream, Data data,
//			Context context) {
//		Model<? extends Data> model = data.getModel();
//		model.format(tokens, stream, data, context);
//	}
	
}
