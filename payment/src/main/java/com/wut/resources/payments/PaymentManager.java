package com.wut.resources.payments;

import com.google.common.base.Strings;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.provider.payment.BraintreeProvider;
import com.wut.provider.payment.SquareProvider;
import com.wut.support.settings.SettingNotFoundException;
import com.wut.support.settings.SettingsManager;
import com.wut.support.settings.SystemSettings;

public class PaymentManager {

	private static final String adminCustId = SystemSettings.getInstance().getSetting("admin.customerid");

	public PaymentProvider getPaymentProcessor(String processorName, String customer) {
		String defaultPaymentProvider = SettingsManager.getClientSettings(adminCustId, "payment.payment-processor");
		// currently Square is the default payment provider for all customers
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
