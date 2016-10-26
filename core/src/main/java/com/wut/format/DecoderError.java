package com.wut.format;

public class DecoderError extends RuntimeException {
	private static final long serialVersionUID = -3776909271119285003L;

	public DecoderError(String string) {
		super(string);
	}
}
