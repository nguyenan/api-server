package com.wut.datasources.email;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.wut.support.ErrorHandler;
import com.wut.support.Language;
import com.wut.support.domain.DomainUtils;
import com.wut.support.settings.SettingsManager;

//import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringEscapeUtils;

// TODO no longer just SendGridEmailers
// TODO make create() function and use instead of contructor
public class SendGridEmailer implements Emailer {

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		private String username;
		private String password;
		
		public SMTPAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public PasswordAuthentication getPasswordAuthentication() {
			//String username = SMTP_AUTH_USER;
			//String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(this.username, this.password);
		}
	}

	public void send(String customerId, String from, String to, String subject, String body)
			throws MailException {
		send(customerId, from, to, null, null, subject, body);
	}	


	@Override
	public void send(String customerId, String from, String to, String cc, String bcc, String subject, String body)
			throws MailException {
		send(customerId, from, null, to, cc, bcc, subject, body);
	}
	
	public void send(String customerId, String from, String fromName, String to, String cc, String bcc, String subject,
			String body) throws MailException {
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			
			// TODO bad coupling to settings --- too far down the stack
			String smtpHost = SettingsManager.getClientSettings(customerId, "email.email-smtp-host");
			String smtpPort = SettingsManager.getClientSettings(customerId, "email.email-smtp-port");

			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", "true");

			// TODO bad coupling to settings --- too far down the stack
			String username = SettingsManager.getClientSettings(customerId, "email.email-username");
			String password = SettingsManager.getClientSettings(customerId, "email.email-password");
			
			String topLevelDomain = DomainUtils.getTopLevelDomain(SettingsManager.getClientSettings(customerId, "email.domain"));
			String fromAddress = "support@" + topLevelDomain;
						
			String fromEmail;
			if (from == null) {
				fromEmail = fromAddress;
			} else {
				fromEmail = from;
			}
			
			Authenticator auth = new SMTPAuthenticator(username, password);
			
			Session mailSession = Session.getInstance(props, auth);
			// uncomment for debugging infos to stdout
			// mailSession.setDebug(true);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);

			Multipart multipart = new MimeMultipart("alternative");

			BodyPart part1 = new MimeBodyPart();
			part1.setText(body); // NOT HTML

			//String bodyHtmlDecoded = WutDecoder.html(body);
			String bodyHtmlDecoded = StringEscapeUtils.unescapeHtml4(body);
			
			BodyPart part2 = new MimeBodyPart();
			part2.setContent(bodyHtmlDecoded, // WITH HTML
					"text/html");

			multipart.addBodyPart(part1);
			multipart.addBodyPart(part2);
			
			// TODO add the ability to have attachments 
			// if has attachment
			if (false) {
				// create the second message part
			    MimeBodyPart part3 = new MimeBodyPart();

			    try {
				    String str = "SomeMoreTextIsHere"; // attachment
		            File newTextFile = new File("C:/thetextfile.txt");
		            FileWriter fw = new FileWriter(newTextFile);
		            fw.write(str);
		            fw.close();
		            
				    // attach the file to the message
				    part3.attachFile(newTextFile);
				    
					multipart.addBodyPart(part3);
			    } catch (Exception e) {
			    	throw new MailException(e);
			    }
			}

			message.setContent(multipart);
			if (Language.isNotBlank(fromEmail)) {
				if (fromName != null)
					message.setFrom(new InternetAddress(fromEmail, fromName));
				else 
					message.setFrom(new InternetAddress(fromEmail));
			} else {
				message.setFrom(new InternetAddress("system@retailkit.com"));
			}
			message.setSubject(subject);

			//List<Address> messageAddresses = new ArrayList<Address>();
			if (Language.isNotBlank(to)) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(to));
			}

			if (Language.isNotBlank(cc)) {
				message.addRecipient(Message.RecipientType.CC,
						new InternetAddress(cc));
			}

			if (Language.isNotBlank(bcc)) {
				message.addRecipient(Message.RecipientType.BCC,
						new InternetAddress(bcc));
			}

			// TODO must have to, cc, or bcc

			transport.connect();
			Transport.send(message);
			transport.close();
		} catch (NoSuchProviderException e) {
			throw new MailException(e);
		} catch (MessagingException e) {
			throw new MailException(e);
		}catch (UnsupportedEncodingException e) {
		 	final String msg = "Error when encoding sender";
 			ErrorHandler.userError(null, msg, e);
			throw new MailException(e);
  		}
		
	}
}
