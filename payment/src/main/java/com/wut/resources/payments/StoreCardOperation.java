package com.wut.resources.payments;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.ScalarModel;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.cipher.Cipher;

public class StoreCardOperation extends ParameteredOperation {
	private static final Cipher cipher = new Cipher();
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	
	public StoreCardOperation() {
		addParameter(OperationParameter.id("username"));
		addParameter(OperationParameter.string("firstName"));
		addParameter(OperationParameter.string("lastName"));
		addParameter(OperationParameter.string("cardNumber"));
		addParameter(OperationParameter.string("expirationMonth"));
		addParameter(OperationParameter.string("expirationYear"));
		addParameter(OperationParameter.string("cvc"));
		addParameter(OperationParameter.string("streetAddress").optional());
		addParameter(OperationParameter.string("extendedAddress").optional());
		addParameter(OperationParameter.string("city").optional());
		addParameter(OperationParameter.string("state").optional());
		addParameter(OperationParameter.string("zipcode").optional());
	}

	@Override
	public String getName() {
		return "store";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		
		StringData username = ri.getParameter("username");
		StringData firstName = ri.getParameter("firstName");
		StringData lastName = ri.getParameter("lastName");
		StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
		StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
		StringData cvv = ri.getParameter("cvc"); // ex: "123"
		StringData streetAddress = ri.getParameter("streetAddress", true);
		StringData extendedAddress = ri.getParameter("extendedAddress", true);
		StringData city = ri.getParameter("city", true);
		StringData state = ri.getParameter("state", true);
		StringData zipCode = ri.getParameter("zipcode", true);
		//StringData country = ri.getParameter("country", true);
		
		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		String cardId = paymentProvider.store(username, firstName, lastName, cardNumber, expirationMonth, expirationYear, cvv, streetAddress, extendedAddress, city, state, zipCode, PaymentProvider.CURRENCY.USD);
		
		if (cardId != null) {
			String encryptPaymentId = cipher.encrypt(cardId);
			return new StringData(encryptPaymentId);
			//return new StringData(cardId);
		} else {
			return MessageData.FAILURE;
		}
	}
	
}

