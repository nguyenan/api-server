package com.wut.resources.payments;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;

// test url:
// http://localhost:8888/api/hybrid/payment?token='public'&id='payment'&customerId='public'&userId='public'&operation='update'&format='js'&visibility='owner'&callback=jQuery1708800740744918585_1350352847830&application='wut'&parameters='{%22firstName%22:%22Russell%22,%20%22lastName%22:%22Dude%22}'&_=1350352848295


// examples:
// https://github.com/stripe/stripe-java/blob/master/src/test/java/com/stripe/StripeTest.java

@Deprecated
public class StripePaymentResource extends ObsoleteCrudResource {
	
	private static final long serialVersionUID = 613058216550835983L;

	static {
		@SuppressWarnings("unused")
		FakeSecurity fixStripeBadCertificateProblem = new FakeSecurity();
	}

	@Override
	public String getName() {
		return "payment-stripe";
	}
	
//	private boolean isTestCard(String cardNumber) {
//		return getTestCardNumbers().contains(cardNumber);
//	}

	@Override
	public Data update(WutRequest ri) throws MissingParameterException {
		StringData firstName = ri.getParameter("firstName");
		StringData lastName = ri.getParameter("lastName");
		StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
		StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
		StringData amount = ri.getParameter("amount"); // ex: "100.00"
		int amoutInCents = (int) (Double.parseDouble(amount.toRawString()) * 100.00d);

		StringData cvc = ri.getParameter("cvc"); // ex: "123"
		
		Stripe.apiKey = "sk_live_UM2tc23B5s9TSWPeD3XJ55sS";
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", amoutInCents);
		chargeMap.put("currency", "usd");

		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", cardNumber.toRawString());
		cardMap.put("exp_month", Integer.parseInt(expirationMonth.toRawString()));
		cardMap.put("exp_year", Integer.parseInt(expirationYear.toRawString()));
		
//		defaultCardParams.put("number", "4242424242424242");
//		defaultCardParams.put("exp_month", 12);
//		defaultCardParams.put("exp_year", 2015);
		cardMap.put("cvc", cvc.toRawString());
		cardMap.put("name", firstName + " " + lastName);
//		defaultCardParams.put("address_line1", "522 Ramona St");
//		defaultCardParams.put("address_line2", "Palo Alto");
//		defaultCardParams.put("address_zip", "94301");
//		defaultCardParams.put("address_state", "CA");
//		defaultCardParams.put("address_country", "USA");
		
		chargeMap.put("card", cardMap);

		try {
			Charge charge = Charge.create(chargeMap);
			System.out.println(charge);
			boolean refund = charge.getRefunded() != false;
			boolean correctAmount = charge.getAmount().equals(Integer.valueOf(amoutInCents));
			boolean wasPaid = charge.getPaid();
			if (!refund && correctAmount && wasPaid) {
				return MessageData.success();
			} else {
				return MessageData.PAYMENT_PROBLEM;
			}
		} catch (StripeException e) {
			e.printStackTrace();
			return MessageData.CARD_DECLINED;
		}
	}
	
	
//	private Set<String> getTestCardNumbers() {
//		Set<String> testCards = new HashSet<String>();
//		testCards.add("4242424242424242");
//		testCards.add("4012888888881881");
//		testCards.add("5555555555554444");
//		testCards.add("5105105105105100");
//		testCards.add("378282246310005");
//		testCards.add("371449635398431");
//		return testCards;
//	}
	
}


