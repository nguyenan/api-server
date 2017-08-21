package com.wut.resources.users;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
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

import org.apache.commons.lang3.StringEscapeUtils;

import com.wut.datasources.CrudSource;
//import com.wut.datasources.email.Emailer;
//import com.wut.datasources.email.SendGridEmailer;
import com.wut.model.Data;
//import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.UserStore;
import com.wut.pipeline.WutRequest;
import com.wut.pipeline.WutRequestBuilder;
import com.wut.support.Language;
import com.wut.support.domain.DomainUtils;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;

@SuppressWarnings("unused")
public class ResetUserOperation extends UserOperation {
	private static SystemSettings sysSettings = SystemSettings.getInstance();
	private SecureRandom random = new SecureRandom();
	private static String adminCustomerId = SystemSettings.getInstance().getSetting("admin.customerid"); 

	public ResetUserOperation(CrudSource source) {
		super(source);
	}

	@Override
	public String getName() {
		return "reset";
	}
	
	// TODO use this everywhere
	private boolean isSuperAdmin(String requestingCustomer, String requestingUser) {
		boolean isAdmin = requestingUser.equals("admin");
		boolean isFromRetailKit = requestingCustomer.equals("izkit");
		boolean isSuperAdmin = isAdmin && isFromRetailKit;
		return isSuperAdmin;
	}
	
	private boolean isDomainAdmin(String requestingCustomer, String requestingUser, String affectedCustomer, String affectedUser) {
		boolean isSameCustomer = requestingCustomer.equals(affectedCustomer);
		boolean isAdmin = requestingUser.equals("admin");
		return isSameCustomer && isAdmin;
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username").toLowerCase();
		StringData toPasswordData = ri.getParameter("password", true);
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId().toLowerCase();
		
		boolean isSuperAdmin = isSuperAdmin(requestingCustomer, requestingUser);
		boolean isDomainAdmin = isDomainAdmin(requestingCustomer, requestingUser, affectedCustomer, affectedUser);
		boolean isForSameUser = affectedCustomer.equals(requestingCustomer) && affectedUser.equals(requestingUser);
		boolean providedPassword = toPasswordData != null;
		
		String domain = SettingsManager.getClientSettings(affectedCustomer, "user.domain");
		String topLevelDomain = DomainUtils.getTopLevelDomain(domain);
		
		String isGlobalReset =  ri.getOptionalParameterAsString("globalReset");
		String isRefreshSettings =  ri.getOptionalParameterAsString("refreshSettings");
		if ((isSuperAdmin || isDomainAdmin || isForSameUser) && providedPassword) {
			String newPassword = toPasswordData.toRawString();
			String subject = "Password Reset";
			String body =  sysSettings.getSetting("password-reset-success") + sysSettings.getSetting("password-reset-footer");
			
			sendEmail(affectedCustomer, "support@"+ topLevelDomain, affectedUser, subject, body);
			newToken(affectedCustomer, affectedUser, newPassword, true);
			// MAKE SURE OLD TOKEN FROM RESET GETS REMOVED -- THIS HAPPENS WITH ONLY 1 TOKEN
		} else if (requestingCustomer.equals(affectedCustomer)) {
			String newPassword = new BigInteger(130, random).toString(32);
			StringData newToken = newToken(affectedCustomer, affectedUser, newPassword, true);
			String resetLink = String.format("https://%s/account.html?", DomainUtils.getRealDomain(domain));
			
			if (isGlobalReset != null && isGlobalReset.equals("true")){
				resetLink = String.format(sysSettings.getSetting("password-reset-link"), affectedCustomer);
			}
			String newTokenEncoded = URLEncoder.encode(newToken.toRawString(), "UTF-8");
			resetLink += "username=" + affectedUser + "&token=" + newTokenEncoded + "&reset=true";
			String subject = "Password Reset Request";
			String body = String.format(sysSettings.getSetting("password-reset-request"), resetLink) + sysSettings.getSetting("password-reset-footer");
		
			sendEmail(affectedCustomer, "support@"+ topLevelDomain, affectedUser, subject, body);
		} else {
			return MessageData.INVALID_PERMISSIONS;
		}		
		return MessageData.success();
	}
	
	public void sendEmail(String customerId, String from, String to, String subject, String body) {
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			
			String smtpHost = SettingsManager.getClientSettings(customerId, "user.email-smtp-host");
			String smtpPort = SettingsManager.getClientSettings(customerId, "user.email-smtp-port");

			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", "true");

			String username = SettingsManager.getClientSettings(customerId, "user.email-username");
			String password = SettingsManager.getClientSettings(customerId, "user.email-password");
			Authenticator auth = new SMTPAuthenticator(username, password);
			
			Session mailSession = Session.getDefaultInstance(props, auth);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);

			Multipart multipart = new MimeMultipart("alternative");

			BodyPart part1 = new MimeBodyPart();
			part1.setText(body); // NOT HTML

			String bodyHtmlDecoded = StringEscapeUtils.unescapeHtml4(body);
			
			BodyPart part2 = new MimeBodyPart();
			part2.setContent(bodyHtmlDecoded, "text/html"); // WITH HTML

			multipart.addBodyPart(part1);
			multipart.addBodyPart(part2);
			
			message.setContent(multipart);
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);

			//List<Address> messageAddresses = new ArrayList<Address>();
			if (Language.isNotBlank(to)) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(to));
			}

			transport.connect();
			Transport.send(message);
			transport.close();
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		private String username;
		private String password;
		
		public SMTPAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.username, this.password);
		}
	}
	
	public static void main(String[] args) throws Exception {
		// CODE TO MANUALLY RESET A USER PASSWORD
		WutRequestBuilder request = new WutRequestBuilder();
		request.resource("user")
				.operation("reset")
				.setUserId("admin")
				.setCustomerId("izkit")
				.parameters("{'customer':'myvegas.com','username':'REPLACE_ME@gmail.com','password':'REPLACE_ME'}");
		WutRequest r = request.build();
		
		UserStore userStore = new UserStore();

		ResetUserOperation resetOp = new ResetUserOperation(userStore);
		
		resetOp.perform(r);
		
	}	
}

