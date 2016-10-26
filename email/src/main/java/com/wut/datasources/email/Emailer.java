package com.wut.datasources.email;

//import com.wut.resources.common.MissingParameterException;

public interface Emailer {
	public void send(String customerId, String from, String to, String subject, String body) throws MailException;
	public void send(String customerId, String from, String to, String cc, String bcc, String subject, String body) throws MailException;
}
