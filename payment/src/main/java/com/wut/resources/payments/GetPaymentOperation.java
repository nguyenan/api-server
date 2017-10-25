package com.wut.resources.payments;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.operations.ParameteredOperation;

// TODO make a read operation (use parameter id)
public class GetPaymentOperation extends ParameteredOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	@Override
	public String getName() {
		return "void";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String paymentId = ri.getParameter("payment");
		
		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		paymentProvider.voidify(paymentId);
		
		return MessageData.success();
	}
	
}

