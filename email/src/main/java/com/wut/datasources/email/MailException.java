package com.wut.datasources.email;


public class MailException extends Exception {

	private static final long serialVersionUID = -429062886358690644L;

	public MailException(Exception e) {
		super(e);
	}

}
