package com.wut.resources.payments;

import com.wut.provider.creditcard.PaymentProvider;
import com.wut.support.settings.SettingsManager;

// TODO rename all "Managers" to "Factory" as that's the real name of this pattern
public class PaymentManager {
	
//	private PaymentProvider stripe = new StripeProvider();
//	private PaymentProvider braintree = new StripeProvider(); // TODO make BrainTreeProvider();
	
	public PaymentProvider getPaymentProcessor(String processorName, String customer) {
		if (processorName.equals("stripe")) {
			String apiKey = SettingsManager.getCustomerSettings(customer, "stripe-api-key");
			return new StripeProvider(apiKey);
		} else if (processorName.equals("braintree")) {
			String merchantId = SettingsManager.getCustomerSettings(customer, "braintree-mechant-id");
			String publicKey = SettingsManager.getCustomerSettings(customer, "braintree-public-key");
			String privateKey = SettingsManager.getCustomerSettings(customer, "braintree-private-key");
			return new BraintreeProvider(merchantId, publicKey, privateKey);
		} else {
			throw new RuntimeException("no payment provider found under name " + processorName);
		}
	}
	
}
