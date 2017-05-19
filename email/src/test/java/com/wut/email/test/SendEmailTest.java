package com.wut.email.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.wut.datasources.email.MailException;
import com.wut.datasources.email.SendGridEmailer;

public class SendEmailTest {

	static String emailFrom;
	static String emailTo;
	static String customerId;
	
	@BeforeClass
	public static void initialize() {
		emailTo = "apitest@tendtest.com";
		emailFrom = "admin@webutilitykit.com";
		customerId = "www.webutilitykit.com";
	}

	@Test
	public void testSendEmailWithFromName() throws MailException {
		new SendGridEmailer().send(customerId, emailFrom, "Webutility Kit", emailTo, null, null, "TendAPIs-SendEmailWithFromName",
				"testSendEmailWithFromName Webutility Kit");
	}

	@Test
	public void testSendEmailWithCC() throws MailException {
		new SendGridEmailer().send(customerId, emailFrom, emailTo, "apitest-cc@tendtest.com", null, "TendAPIs-SendEmailWithCC",
				"testSendEmailWithCC");
	}

	@Test
	public void testSendEmailWithoutFrom() throws MailException {
		new SendGridEmailer().send(customerId, null, emailTo, "TendAPIs-SendEmailWithoutFrom", "testSendEmailWithoutFrom");
	}

	@Test
	public void testSendEmail() throws MailException {
		new SendGridEmailer().send(customerId, emailFrom, emailTo, "TendAPIsSendEmailWithFromNameNull",
				"testSendEmailWithFromName null");
	}

	@Test
	public void testSendEmailWithFromNameEmpty() throws MailException {
		new SendGridEmailer().send(customerId, emailFrom, "", emailTo, null, null, "TendAPIs-SendEmailWithFromNameEmpty",
				"testSendEmailWithFromNameEmpty");
	}

	@Test
	public void testSendFromNonMatchingDomain() throws MailException {
		try {
			new SendGridEmailer().send(customerId, "test@utilitykit.com", emailTo, "TendAPIs-Wrong",
					"Sent from different domain");
			fail("Sent from different domain");
		} catch (Exception e) {
			assert (true);
		}

	}

	@Test
	public void testSendFromNonTopLevelDomain() throws MailException {
		try {
			new SendGridEmailer().send(customerId, "test@www.webutilitykit.com", emailTo, "TendAPIs-WrongCase",
					"Sent from Non TopLevel domain");
			fail("Sent from Non TopLevel domain");
		} catch (Exception e) {
			assert (true);
		}

	}
}
