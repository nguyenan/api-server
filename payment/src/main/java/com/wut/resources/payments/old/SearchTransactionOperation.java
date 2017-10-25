package com.wut.resources.payments.old;

import com.wut.model.Data;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.operations.ReadOperation;
import com.wut.resources.payments.PaymentOperationHelper;

public class SearchTransactionOperation extends ReadOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	@Override
	public String getName() {
		return "search";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		return paymentProvider.search(null, null);
	}
}
