package com.wut.resources.payments;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.ScalarModel;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.OperationParameter;
import com.wut.resources.operations.ParameteredOperation;

public class RefundPaymentOperation extends ParameteredOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	public RefundPaymentOperation() {
		addParameter(OperationParameter.create("payment", ScalarModel.create()));
		addParameter(OperationParameter.create("amount", ScalarModel.create()));
	}
	
	@Override
	public String getName() {
		return "refund";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		

		StringData paymentTransactionId = ri.getParameter("paymentTransactionId");
		StringData tenderId = ri.getParameter("tenderId");

		StringData amount = ri.getParameter("amount");
		BigDecimal amountBigDec = new BigDecimal(amount.toRawString());
		
		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		
		PaymentResponseData refund = paymentProvider.refund(paymentTransactionId.toRawString(), amountBigDec, tenderId.toRawString());
		
		return refund;
	}

}

