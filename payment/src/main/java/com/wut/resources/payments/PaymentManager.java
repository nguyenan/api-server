package com.wut.resources.payments;

import com.google.common.base.Strings;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.provider.payment.BraintreeProvider;
import com.wut.provider.payment.SquareProvider;
import com.wut.support.settings.SettingNotFoundException;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;

// TODO rename all "Managers" to "Factory" as that's the real name of this pattern
public class PaymentManager {

	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");
	// private PaymentProvider stripe = new StripeProvider();
	// private PaymentProvider braintree = new StripeProvider(); // TODO make
	// BrainTreeProvider();

	public PaymentProvider getPaymentProcessor(String processorName, String customer) {
		String defaultPaymentProvider = SettingsManager.getClientSettings(adminCustId, "payment.payment-processor");
		if (defaultPaymentProvider.equals("square") || processorName.equals("square")) {
			String accessToken = SettingsManager.getClientSettings(customer, "payment.square.access-token", true);
			if (Strings.isNullOrEmpty(accessToken)) {
				throw new SettingNotFoundException("payment.square.access-token");
			}
			return new SquareProvider(accessToken);
		} else if (processorName.equals("braintree")) {
			String merchantId = SettingsManager.getClientSettings(customer, "payment.braintree-mechant-id");
			String publicKey = SettingsManager.getClientSettings(customer, "payment.braintree-public-key");
			String privateKey = SettingsManager.getClientSettings(customer, "payment.braintree-private-key");
			return new BraintreeProvider(merchantId, publicKey, privateKey);
		} else {
			throw new RuntimeException("no payment provider found under name " + processorName);
		}
	}

}
