package com.wut.email.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.wut.datasources.email.MailException;
import com.wut.datasources.email.SendGridEmailer;

public class SendEmailTest {

	String emailTo;

	@BeforeClass
	public void initialize() {
		emailTo = "apitest@tendtest.com";
	}

	@Test
	public void testSendEmailWithFromName() throws MailException {
		new SendGridEmailer().send("www.tend.ag", "test@tend.ag", "Mapiii", emailTo, null, null, "TendAPIs",
				"testSendEmailWithFromName Mapiii");
	}

	@Test
	public void testSendEmailWithCC() throws MailException {
		new SendGridEmailer().send("www.tend.ag", "test@tend.ag", emailTo, "testcc@tend.ag", null, "TendAPIs",
				"testSendEmailWithFromName Mapiii");
	}

	@Test
	public void testSendEmailWithoutFrom() throws MailException {
		new SendGridEmailer().send("www.tend.ag", null, emailTo, "TendAPIs", "testSendEmailWithFromName null");
	}

	@Test
	public void testSendEmail() throws MailException {
		new SendGridEmailer().send("www.tend.ag", "test@tend.ag", emailTo, "TendAPIs",
				"testSendEmailWithFromName null");
	}

	@Test
	public void testSendEmailWithFromNameEmpty() throws MailException {
		new SendGridEmailer().send("www.tend.ag", "test@tend.ag", "", emailTo, null, null, "TendAPIs",
				"testSendEmailWithFromName empty");
	}

	@Test
	public void testSendFromNonMatchingDomain() throws MailException {
		try {
			new SendGridEmailer().send("www.tend.ag", "test@test.ag", emailTo, "TendAPIs",
					"Sent from different domain");
			fail("Sent from different domain");
		} catch (Exception e) {
			assert (true);
		}

	}

	@Test
	public void testSendFromNonTopLevelDomain() throws MailException {
		try {
			new SendGridEmailer().send("www.tend.ag", "test@test.tend.ag", emailTo, "TendAPIs",
					"Sent from Non TopLevel domain");
			fail("Sent from Non TopLevel domain");
		} catch (Exception e) {
			assert (true);
		}

	}
}