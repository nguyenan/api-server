package com.wut.resources.payments;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.provider.creditcard.PaymentProvider.RESPONSE;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;
import com.wut.support.settings.SettingsManager;

public class OldPaymentResource extends ObsoleteCrudResource {
	
	private static final long serialVersionUID = 1L;

	public OldPaymentResource() {
		super();
	}
	
	@SuppressWarnings("unused")
	private enum FAKECARD {
		GOOD("4242424242424242", RESPONSE.SUCCESS),
		DECLINED("4242424242424242", RESPONSE.CARD_DECLINED),
		AVS_FAIL("4242424242424242", RESPONSE.CARD_DECLINED);
		
		private final String cardNumber;
		private final RESPONSE response;
		FAKECARD(final String cardNumber, final RESPONSE response) {
			this.cardNumber = cardNumber;
			this.response = response;
		}
		public String getCardNumber() {
			return cardNumber;
		} 
	}

	@Override
	public String getName() {
		return "payment";
	} 
	@Override
	public Data read(WutRequest request) throws Exception {
		throw new RuntimeException("read not implemented any more"); 
	}

	@Override
	public Data update(WutRequest ri) throws MissingParameterException {
		StringData firstName = ri.getParameter("firstName");
		StringData lastName = ri.getParameter("lastName");
		StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
		StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
		StringData amount = ri.getParameter("amount"); // ex: "100.00"
		StringData cvv = ri.getParameter("cvc"); // ex: "123"

		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = getPaymentProvider(customer);

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

	protected static PaymentProvider getPaymentProvider(String customer) {
		String paymentProviderStr = SettingsManager.getClientSettings(
				customer, "payment-processor");
		PaymentManager paymentManager = new PaymentManager();
		PaymentProvider paymentProvider = paymentManager.getPaymentProcessor(
				paymentProviderStr, customer);
		return paymentProvider;
	} 

}
