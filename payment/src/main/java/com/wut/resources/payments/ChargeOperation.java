package com.wut.resources.payments;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.provider.creditcard.PaymentProvider.CURRENCY;
import com.wut.resources.operations.ReadOperation;

public class ChargeOperation extends ReadOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	@Override
	public String getName() {
		return "charge";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		StringData amount = request.getParameter("amount");
		StringData cardNonce = request.getParameter("cardNonce");
		StringData order = request.getOptionalParameter("order");

		BigDecimal centsToDollars = new BigDecimal("100");
		BigDecimal ammountNumber = new BigDecimal(amount.toRawString());
		PaymentResponseData charge = paymentProvider.charge(
				cardNonce.toRawString(), 
				CURRENCY.USD,
				ammountNumber.multiply(centsToDollars), 
				order == null ? null : String.valueOf(order));
		// save to DB - client do
		return charge;
	}
}
