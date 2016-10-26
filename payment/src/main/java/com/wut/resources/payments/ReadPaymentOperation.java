 package com.wut.resources.payments;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MessageData;
import com.wut.model.map.TransactionData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.resources.operations.ReadOperation;
import com.wut.support.settings.SettingsManager;

public class ReadPaymentOperation extends ReadOperation {
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	@Override
	public String getName() {
		return "read";
	}
	
	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		//String apiKey = SettingsManager.getCustomerSettings(customer, "stripe-api-key");
		//PaymentProvider paymentProvider = new StripeProvider(apiKey);
		
		//PaymentProvider paymentProvider = getPaymentProvider(customer);
		
		StringData idStr = request.getParameter("id", true);
		
		if (idStr != null) {
			IdData id = new IdData(idStr.toRawString());
			TransactionData trans = paymentProvider.read(id.toRawString());
			return trans;
		} else {
			StringData start = request.getParameter("start");
			StringData end = request.getParameter("end");
			
			DateData startDate = new DateData(start.toRawString());
			DateData endDate = new DateData(end.toRawString());
			
			ListData search = paymentProvider.search(startDate, endDate);
			
			if (search != null) {
				return search;
			}
			
			return MessageData.NO_DATA_FOUND;
		}
	}
	
}

