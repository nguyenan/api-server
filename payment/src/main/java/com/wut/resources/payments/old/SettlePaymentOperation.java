package com.wut.resources.payments.old;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.ScalarModel;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.resources.payments.PaymentOperationHelper;

public class SettlePaymentOperation extends ParameteredOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();
	
	public SettlePaymentOperation() {
		addParameter(OperationParameter.create("payment", ScalarModel.create()));
		addParameter(OperationParameter.create("amount", ScalarModel.create()));
	}

	@Override
	public String getName() {
		return "settle";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		
		String paymentId = ri.getParameter("payment");
		String amount = ri.getParameter("amount");
		BigDecimal amountBigDec = new BigDecimal(Double.parseDouble(amount));
		
		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		paymentProvider.refund(paymentId, amountBigDec);
		
		return MessageData.success();
	}
	
	
}

