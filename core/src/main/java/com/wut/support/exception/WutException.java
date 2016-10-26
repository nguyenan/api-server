package com.wut.support.exception;

import com.wut.model.map.MessageData;

public class WutException extends RuntimeException {
	
	public WutException(MessageData error) {
		super(error.get("message").toString());
	}


}
