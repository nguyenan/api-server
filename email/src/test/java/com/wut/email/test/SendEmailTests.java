package com.wut.email.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wut.datasources.email.MailException;
import com.wut.datasources.email.SendGridEmailer;

public class SendEmailTests {

	@Test
	public void testSendNormalEmail() throws MailException {
		new SendGridEmailer().send("www.tend.ag", "test@tend.ag", "an.nguyenhoang@spiraledge.com", "this is a test",
				"testing 1 2 3 6");
	}

	@Test
	public void testSendFromNonMatchingDomain() throws MailException {
		try {
			new SendGridEmailer().send("www.tend.ag", "test@test.ag", "an.nguyenhoang@spiraledge.com", "this is a test",
					"testing 1 2 3 6");
			fail("Sent from different domain");
		}
		catch(Exception e){
				assert(true);
		}
		
	}
}
