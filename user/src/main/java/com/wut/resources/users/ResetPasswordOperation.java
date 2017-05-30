package com.wut.resources.users;

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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringEscapeUtils;

import com.wut.datasources.CrudSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.support.Language;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/user")
@Api(value = "user", tags = "user")
@Produces({ MediaType.APPLICATION_JSON })
public class ResetPasswordOperation extends UserOperation {
	private SecureRandom random = new SecureRandom();
	private static String adminCustomerId = SystemSettings.getInstance().getSetting("admin.customerid"); 

	public ResetPasswordOperation(CrudSource source) {
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
	
	@POST
	@Path("/operation=reset")
	  @ApiOperation(value = "Reset user's password", 
	    notes = "Returns a Mapped Data", 
	    response = Data.class
	  )
	  @ApiResponses(value = { @ApiResponse(code = 180, message = "invalid permissions")})
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String affectedCustomer = ri.getStringParameter("customer");
		String affectedUser = ri.getStringParameter("username");
		StringData toPasswordData = ri.getParameter("password", true);
		String requestingCustomer = ri.getCustomer();
		String requestingUser = ri.getUserId();
		
		boolean isSuperAdmin = isSuperAdmin(requestingCustomer, requestingUser);
		boolean isDomainAdmin = isDomainAdmin(requestingCustomer, requestingUser, affectedCustomer, affectedUser);
		boolean isForSameUser = affectedCustomer.equals(requestingCustomer) && affectedUser.equals(requestingUser);
		boolean providedPassword = toPasswordData != null;
		
		String isGlobalReset =  ri.getOptionalParameterAsString("globalReset");
		String isRefreshSettings =  ri.getOptionalParameterAsString("refreshSettings");
		if ((isSuperAdmin || isDomainAdmin || isForSameUser) && providedPassword) {
			String newPassword = toPasswordData.toRawString();
			String subject = "Password Reset";
			String body = "You password has been reset.<br><br>";
			
			sendEmail(affectedCustomer, "support@" + (affectedCustomer.equals(adminCustomerId)? "www.tend.ag": affectedCustomer), affectedUser, subject, body);

			newToken(affectedCustomer, affectedUser, newPassword);
			// MAKE SURE OLD TOKEN FROM RESET GETS REMOVED -- THIS HAPPENS WITH ONLY 1 TOKEN
		} else if (requestingCustomer.equals(affectedCustomer)) {
			String newPassword = new BigInteger(130, random).toString(32);
			StringData newToken = newToken(affectedCustomer, affectedUser, newPassword);
			String link = "";
			if (isRefreshSettings != null && isRefreshSettings.equals("true")){
				link = SettingsManager.getCustomerSettings(requestingCustomer, "password-reset-link", true);
			}
			else {
				link = SettingsManager.getCustomerSettings(requestingCustomer, "password-reset-link");	
			}
			if (isGlobalReset != null && isGlobalReset.equals("true")){
				link = String.format(SystemSettings.getInstance().getSetting("password-reset-link"), affectedCustomer);
			}
			String newTokenEncoded = URLEncoder.encode(newToken.toRawString(), "UTF-8");
			link += "username=" + affectedUser + "&token=" + newTokenEncoded + "&reset=true";
			String subject = "Password Reset Request";
			String body = "We have received a request for your password to be reset. Click <a href=\"" + link + "\">here</a> to set your new password. If this request was not made by you, please ignore this email.<br><br>";
			
			sendEmail(affectedCustomer, "support@" + (affectedCustomer.equals(adminCustomerId)? "www.tend.ag": affectedCustomer), affectedUser, subject, body);
		} else {
			return MessageData.INVALID_PERMISSIONS;
		}
		
		return MessageData.success();
	}
	
	public void sendEmail(String customerId, String from, String to, String subject, String body) {
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			
			String smtpHost = SettingsManager.getCustomerSettings(customerId, "email-smtp-host");
			String smtpPort = SettingsManager.getCustomerSettings(customerId, "email-smtp-port");

			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", "true");

			String username = SettingsManager.getCustomerSettings(customerId, "email-username");
			String password = SettingsManager.getCustomerSettings(customerId, "email-password");
			
			String topLevelDomain = SettingsManager.getCustomerSettings(customerId, "top-level-domain");
			
			Authenticator auth = new SMTPAuthenticator(username, password);
			
			Session mailSession = Session.getDefaultInstance(props, auth);
			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);

			Multipart multipart = new MimeMultipart("alternative");

			BodyPart part1 = new MimeBodyPart();
			part1.setText(body); // NOT HTML

			//String bodyHtmlDecoded = WutDecoder.html(body);
			String bodyHtmlDecoded = StringEscapeUtils.unescapeHtml4(body);
			
			BodyPart part2 = new MimeBodyPart();
			part2.setContent(bodyHtmlDecoded, "text/html"); // WITH HTML

			multipart.addBodyPart(part1);
			multipart.addBodyPart(part2);
			
			message.setContent(multipart);
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);

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
}