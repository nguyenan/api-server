package com.wut.resources.email;

import com.wut.datasources.email.MailException;
import com.wut.datasources.email.SendGridEmailer;
//import com.wut.datasources.email.javaemailer.JavaEmailer;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ResourceGroupAnnotation;

@ResourceGroupAnnotation(name = "mail", group = "email", desc = "retrieve and send email")
public class EmailResource extends CrudResource {
	private static final long serialVersionUID = 3301682262046459168L;

	public EmailResource() {
		super("email", null); // TODO fix null here -- use a provider!
	}
	
	@Override
	public String getName() {
		return "email";
	}
	
	@Override
	public Data create(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data read(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data update(WutRequest ri)
			throws MissingParameterException {
		String subject = ri.getStringParameter("subject");
		String body = ri.getStringParameter("body");
		String recipients = ri.getStringParameter("to");
		String cc = ri.getOptionalParameterAsString("cc");
		String bcc = ri.getOptionalParameterAsString("bcc");
		String customer = ri.getCustomer();

		String from = ri.getOptionalParameterAsString("from");
		String fromName = ri.getOptionalParameterAsString("fromName");
		
		SendGridEmailer emailer = new SendGridEmailer();
		try {
			emailer.send(customer, from, fromName, recipients, cc, bcc, subject, body);
		} catch (MailException e) {
			return MessageData.EMAIL_SEND_PROBLEM.context(e.getMessage());
		}
		
		return MessageData.SUCCESS;
	}
	
	@Override
	public Data delete(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}
	
	public static void main(String[] args) throws MissingParameterException {
		//JavaEmailer emailer = JavaEmailer.create();
		//emailer.send(null, "from@gmail.com", "to@gmail.com", "testing", "this is a test");
	}

}
