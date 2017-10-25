package com.wut.resources.payments.old;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.resources.payments.PaymentOperationHelper;

public class UpdatePaymentOperation extends ParameteredOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	public UpdatePaymentOperation() {
		addParameter(OperationParameter.id("firstName"));
		addParameter(OperationParameter.string("lastName"));
		addParameter(OperationParameter.string("lastName"));
		addParameter(OperationParameter.string("cardNumber"));
		addParameter(OperationParameter.string("expirationMonth"));
		addParameter(OperationParameter.string("expirationYear"));
		addParameter(OperationParameter.string("cvc"));
	}
	
	@Override
	public String getName() {
		return "update";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		StringData firstName = ri.getParameter("firstName");
		StringData lastName = ri.getParameter("lastName");
		StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
		StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
		StringData amount = ri.getParameter("amount"); // ex: "100.00"
		StringData cvv = ri.getParameter("cvc"); // ex: "123"

		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);

 		PaymentResponseData response = paymentProvider.charge(
				firstName.toRawString(), 
				lastName.toRawString(),
				cardNumber.toRawString(), 
				expirationMonth.toRawString(),
				expirationYear.toRawString(), 
				null /* street address */, 
				null /* extended address */, 
				null /* city */, 
				null /* state */, 
				null /* zip code */, 
				null /* country */,
				null /* currency */, 
				new BigDecimal(amount.toRawString()),
				cvv.toRawString(), 
				null /* order id */ );
		
		return response;
	}
	
}

