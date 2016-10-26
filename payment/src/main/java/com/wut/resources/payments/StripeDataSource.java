package com.wut.resources.payments;

/*
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Token;
import com.wut.model.list.ListData;
import com.wut.model.map.TransactionData;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.support.ErrorHandler;

public class StripeDataSource implements PaymentProvider {
	private String apiKey;
	final static int CENTS_PER_DOLLAR = 100;
	final static int MAX_CHARGES_PER_REQUEST = 100;
	
	static {
		// TODO fix this -- bad hack!!
		@SuppressWarnings("unused")
		FakeSecurity fixStripeBadCertificateProblem = new FakeSecurity();
	}

	public StripeDataSource(String apiKey) {
		this.apiKey = apiKey;
	}
	
	
	private Map<String, Object> getCardMap(String firstName, String lastName, String cardNumber,
			String expirationMonth, String expirationYear,
			String streetAddress, String extendedAddress, String city,
			String state, String postalCode, COUNTRY country,
			String cvc) {
		
		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", cardNumber);
		cardMap.put("exp_month", Integer.parseInt(expirationMonth));
		cardMap.put("exp_year", Integer.parseInt(expirationYear));
		cardMap.put("cvc", cvc);
		cardMap.put("name", firstName + " " + lastName);
		
//		defaultCardParams.put("address_line1", "522 Ramona St");
//		defaultCardParams.put("address_line2", "Palo Alto");
//		defaultCardParams.put("address_zip", "94301");
//		defaultCardParams.put("address_state", "CA");
//		defaultCardParams.put("address_country", "USA");
		
//		cardMap.put("address_line1", streetAddress);
//		cardMap.put("address_line2", city);
//		cardMap.put("address_zip", zipCode);
//		cardMap.put("address_state", state);
//		cardMap.put("address_country", country);
		
		return cardMap;
	}
	
	
	private Map<String, Object> getChargeMap(String firstName, String lastName, String cardNumber,
			String expirationMonth, String expirationYear,
			String streetAddress, String extendedAddress, String city,
			String state, String postalCode, COUNTRY country,
			CURRENCY currency, BigDecimal amount, String cvc, String orderId) {
		Map<String, Object> chargeMap = new HashMap<String, Object>();
	
		String amountStr = amount.toPlainString();
		int amoutInCents = (int) (Double.parseDouble(amountStr) * 100.00d);
		chargeMap.put("amount", amoutInCents);
		chargeMap.put("currency", "usd");

		Map<String, Object> cardMap = getCardMap(firstName, lastName, cardNumber, expirationMonth, expirationYear, streetAddress, extendedAddress, city, state, postalCode, country, cvc);
		
		chargeMap.put("card", cardMap);
		
		return chargeMap;
	}
	
	private Map<String, Object> getChargeMap(String cardId, CURRENCY currency,
			BigDecimal amount, String orderId) {

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		
		String amountStr = amount.toPlainString();
		int amoutInCents = (int) (Double.parseDouble(amountStr) * 100.00d);
		chargeParams.put("amount", amoutInCents);
		chargeParams.put("currency", "usd");
		chargeParams.put("card", cardId);
		chargeParams.put("description", orderId);
		
		return chargeParams;
	}
	
	public PaymentResponseData processPayment(Map<String, Object> chargeMap) {
		
		try {
			Charge charge = Charge.create(chargeMap, apiKey);
			System.out.println(charge);
			boolean refund = charge.getRefunded() != false;
			// TODO remove amountInCents duplication here
			String amount = chargeMap.get("amount").toString();
			int amoutInCents = (int) (Double.parseDouble(amount) * 100.00d);
			boolean correctAmount = charge.getAmount().equals(Integer.valueOf(amoutInCents));
			boolean wasPaid = charge.getPaid();
			if (!refund && correctAmount && wasPaid) {
				String chargeId = charge.getId();
				return PaymentResponseData.success(chargeId);
			} else {
				return PaymentResponseData.GENERIC_FAILURE;
			}
		} catch (StripeException e) {
			ErrorHandler.userError("problem with payment", e);
			return PaymentResponseData.CARD_DECLINED;
		}
	}

	@Override
	public PaymentResponseData charge(String firstName, String lastName, String cardNumber,
			String expirationMonth, String expirationYear,
			String streetAddress, String extendedAddress, String city,
			String state, String postalCode, COUNTRY country,
			CURRENCY currency, BigDecimal amount, String cvc, String orderId) {
		
		Map<String, Object> chargeMap = getChargeMap( firstName,  lastName,  cardNumber,
				 expirationMonth,  expirationYear,
				 streetAddress,  extendedAddress,  city,
				 state,  postalCode,  country,
				 currency,  amount,  cvc,  orderId);

		PaymentResponseData response = processPayment(chargeMap);
		
		return response;
	}
	
	@Override
	public PaymentResponseData charge(String cardId, CURRENCY currency,
			BigDecimal amount, String orderId) {
		
		Map<String, Object> chargeMap = getChargeMap(cardId,
				 currency,  amount, orderId);
		
		PaymentResponseData response = processPayment(chargeMap);

		return response;
	}

	

	@Override
	public boolean refund(String paymentId, BigDecimal amount) {
		try {
			Charge charge = Charge.retrieve(paymentId, apiKey);
			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", amount);
			chargeMap.put("currency", "usd");
			charge.refund(chargeMap);
			return true;
		} catch (StripeException e) {
			return false;
		}
	}

	@Override
	public boolean voidify(String paymentId) {
		return false;
	}

	@Override
	public boolean settle(String paymentId, BigDecimal amount) {
//		try {
//			Charge charge = Charge.retrieve(paymentId, apiKey);
//			//charge.se
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return false;
	}

	@Override
	public boolean verify(String paymentId, BigDecimal amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String store(String customerId, String firstName, String lastName,
			String cardNumber, String expirationMonth, String expirationYear,
			String cvv, String streetAddress, String extendedAddress,
			String city, String state, String postalCode, COUNTRY country) {
		
		Map<String, Object> tokenParams = new HashMap<String, Object>();
		
		Map<String, Object> cardMap = getCardMap(firstName, lastName, cardNumber, expirationMonth, expirationYear, streetAddress, extendedAddress, city, state, postalCode, country, cvv);
		
		tokenParams.put("card", cardMap);

		try {
			Token paymentToken = Token.create(tokenParams);
			
			return paymentToken.getId();
		} catch (StripeException e) {
			return null;
		}
	}

	@Override
	public ListData search(DateData start, DateData end) {
		
		ListData transactionList = new ListData();
		
		try {
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			
			chargeParams.put("count", MAX_CHARGES_PER_REQUEST);
			
			ChargeCollection charges = Charge.all(chargeParams);
			
			for (Charge charge : charges.getData()) {
				//charge.
				String name = charge.getCard().getName();
				String firstNameStr = null;
				String lastNameStr = null;
				String[] nameParts = name.split(" ");
				if (nameParts.length == 2) {
					firstNameStr = nameParts[0];
					lastNameStr = nameParts[1];
				}
				
				Integer amount = charge.getAmount();
				String amountStr = (amount.intValue() / CENTS_PER_DOLLAR) + "." + (amount.intValue() % CENTS_PER_DOLLAR);
				
				String orderId = charge.getDescription();

				//String amountStr = charge.getAmount().toString();
				
				String created = charge.getCreated().toString();
				
				String id = charge.getId();
				
				//TransactionData transaction = new TransactionData(id, created, firstNameStr, lastNameStr, amountStr, "usd", );
				
				TransactionData tansactionData = new TransactionData(id,  created,  firstNameStr,  lastNameStr,  amountStr, "usd",  "charge",  orderId);

				transactionList.add(tansactionData);
			}
			
			return transactionList;
		} catch (StripeException e) {
			e.printStackTrace();
		}
		
		return null;
	}


//	@Override
//	public List<Map<String, String>> search(Date start, Date end,
//			String firstName, String lastName, String orderId) {
//		return null;
//	}

	
	@Override
	public String store(StringData username, StringData firstName,
			StringData lastName, StringData cardNumber,
			StringData expirationMonth, StringData expirationYear,
			StringData cvv, StringData streetAddress,
			StringData extendedAddress, StringData city, StringData state,
			StringData zipCode, CURRENCY currency) {
		
		return store(String.valueOf(username), String.valueOf(firstName),
				String.valueOf(lastName), String.valueOf(cardNumber),
				String.valueOf(expirationMonth), String.valueOf(expirationYear),
				String.valueOf(cvv), String.valueOf(streetAddress),
				String.valueOf(extendedAddress), String.valueOf(city),
				String.valueOf(state), String.valueOf(zipCode),
				COUNTRY.US
				);
		
	}


	@Override
	public TransactionData read(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
*/
