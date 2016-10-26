package com.wut.model.stream;

//import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
//import java.net.URLEncoder;

public class OutputStreamAdapter extends OutputStream {
	private ByteStreamData byteStream;

	public OutputStreamAdapter(ByteStreamData stream) {
		this.byteStream = stream;
	}
	
	@Override
	public void write(int integer) throws IOException {
		byteStream.write(integer);
	}

	@Override
	public void write(byte[] b, int offset, int length) throws IOException {
		byteStream.write(b, offset, length);
	}

	@Override
	public void write(byte[] b) throws IOException {
		byteStream.write(b);
	}
	
	public ByteStreamData getByteStream() {
		return byteStream;
	}
	
}
