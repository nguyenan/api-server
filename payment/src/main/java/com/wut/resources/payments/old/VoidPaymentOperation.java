package com.wut.resources.payments.old;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.resources.payments.PaymentOperationHelper;

public class VoidPaymentOperation extends ParameteredOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	public VoidPaymentOperation() {
		addParameter(OperationParameter.id("payment"));
	}

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

