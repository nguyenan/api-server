package com.wut.datasources.email.javaemailer;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.net.ssl.internal.ssl.Provider;
import com.wut.datasources.email.Emailer;
import com.wut.datasources.email.MailException;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.pipeline.WutRequest;
import com.wut.support.ErrorHandler;
import com.wut.support.settings.SystemSettings;

// TODO no longer used. now we use sendgrid emailer
public class JavaEmailer implements Emailer {

	public static JavaEmailer create() {
		return new JavaEmailer();
	}

	public void send(String customerId, String from, String to, String subject, String body) {
		doSend("noreply@webutilitykit.com", from, to, null, null, subject, body, true);
	}
	
	public void send(String customerId, String from, String to, String cc, String bcc, String subject, String body)
			throws MailException {
		doSend("noreply@webutilitykit.com", from, to, cc, bcc, subject, body, true);
	}


	public Data read(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

	private Session session = getEmailSession();


	public synchronized Data doSend(String sender, String from, String toRecipients, String ccRecipients,
			String bbcRecipients, String subject, String body,
			boolean htmlContent) {
		// String subject = ri.getParameter("subject");
		// String body = ri.getParameter("body");
		// String recipients = ri.getParameter("to");
		// String sender = ri.getUser().getUsername(); // TODO make sure it's an
		// email address

		try {
			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			
			if (htmlContent) {
				message.setContent(body, "text/html");
			} else {
				message.setContent(body, "text/plain");
			}

			if (toRecipients.indexOf(',') > 0) {
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toRecipients));
			} else {
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(toRecipients));
			}

			if (bbcRecipients != null) {
				if (bbcRecipients.indexOf(',') > 0) {
					message.setRecipients(Message.RecipientType.BCC,
							InternetAddress.parse(bbcRecipients));
				} else {
					message.setRecipient(Message.RecipientType.BCC,
							new InternetAddress(bbcRecipients));
				}
			}
			
			if (ccRecipients != null) {
				if (ccRecipients.indexOf(',') > 0) {
					message.setRecipients(Message.RecipientType.CC,
							InternetAddress.parse(ccRecipients));
				} else {
					message.setRecipient(Message.RecipientType.CC,
							new InternetAddress(ccRecipients));
				}
			}
			
			Transport.send(message);
		} catch (AddressException e) {
			final String msg = "Invalid email address specified";
			ErrorHandler.userError(null, msg, e);
			return new ErrorData(msg);
		} catch (MessagingException e) {
			final String msg = "Unable to send email";
			ErrorHandler.userError(null, msg, e);
			return new ErrorData(msg);
		}

		return MessageData.SUCCESS;
	}

	public static Session getEmailSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		String mailhost = "smtp.gmail.com";
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		Session session = null;

		Security.addProvider(new Provider());

		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						String username = SystemSettings.getInstance()
								.getSetting("email.username");
						String password = SystemSettings.getInstance()
								.getSetting("email.password");
						return new PasswordAuthentication(username, password);
					}
				});

		return session;
	}

}
