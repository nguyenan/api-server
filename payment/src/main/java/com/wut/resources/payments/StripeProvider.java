package com.wut.resources.payments;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCardCollection;
import com.stripe.model.Refund;
import com.wut.model.list.ListData;
import com.wut.model.map.TransactionData;
import com.wut.model.map.TransactionData.STATUS;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.support.ErrorHandler;
import com.wut.support.StringHelper;

public class StripeProvider implements PaymentProvider {
	private String apiKey;
	//private final static int CENTS_PER_DOLLAR = 100;
	private final static int MAX_CHARGES_PER_REQUEST = 100;
	private static final String CARD_TOKEN_PREFIX = "st:";
	
	static {
		// TODO fix this -- bad hack!!
		//@SuppressWarnings("unused")
		//FakeSecurity fixStripeBadCertificateProblem = new FakeSecurity();
	}

	public StripeProvider(String apiKey) {
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
	
//		String amountStr = amount.toPlainString();
//		int amoutInCents = (int) (Double.parseDouble(amountStr) * 100.00d);
		
		String amountInCentsStr = getAmountAsInteger(amount).toString();

		chargeMap.put("amount", amountInCentsStr);
		chargeMap.put("currency", "usd");

		Map<String, Object> cardMap = getCardMap(firstName, lastName, cardNumber, expirationMonth, expirationYear, streetAddress, extendedAddress, city, state, postalCode, country, cvc);
		
		chargeMap.put("card", cardMap);
		
		return chargeMap;
	}
	
	private Map<String, Object> getChargeMap(String customerId, String cardId, CURRENCY currency,
			BigDecimal amount, String orderId) {

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		
		//String amountStr = amount.toPlainString();
		//int amoutInCents = (int) (Double.parseDouble(amountStr) * 100.00d);
		
		String amountInCentsStr = getAmountAsInteger(amount).toString();
		
		chargeParams.put("amount", amountInCentsStr);
		chargeParams.put("currency", "usd");
		chargeParams.put("customer", customerId);
		chargeParams.put("card", cardId);
		chargeParams.put("description", orderId);
		
		return chargeParams;
	}
	
//	private String getAmountAsString(BigDecimal amount) {
//		BigDecimal amountInCents = new BigDecimal(amount.toPlainString());
//		amountInCents = amountInCents.movePointRight(2);
//		String amountInCentsStr = amountInCents.toPlainString();
//		return amountInCentsStr;
//	}
	
	private Integer getAmountAsInteger(BigDecimal amount) {
		BigDecimal amountInCents = new BigDecimal(amount.toPlainString());
		amountInCents = amountInCents.movePointRight(2);
		return amountInCents.intValue();
	}
	
	public PaymentResponseData processPayment(Map<String, Object> chargeMap) {
		
		try {
			Charge charge = Charge.create(chargeMap, apiKey);
			System.out.println(charge);
			boolean refund = charge.getRefunded() != false;
			// TODO remove amountInCents duplication here
			String amount = chargeMap.get("amount").toString();
			//int amoutInCents = (int) (Double.parseDouble(amount) * 100.00d);
			boolean correctAmount = charge.getAmount().equals(Integer.valueOf(amount));
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
	public PaymentResponseData charge(String paymentId, CURRENCY currency,
			BigDecimal amount, String orderId) {
		
		String customerId = getCustomerIdFromPaymentId(paymentId);
		
		String cardId = getCardIdFromPaymentId(paymentId);

		//String realCardId = StringHelper.removePrefix(CARD_TOKEN_PREFIX, cardId); 
		
		Map<String, Object> chargeMap = getChargeMap(customerId, cardId,
				 currency,  amount, orderId);
		
		PaymentResponseData response = processPayment(chargeMap);

		return response;
	}

	

	@Override
	public boolean refund(String paymentId, BigDecimal amount) {
		try {
			Charge charge = Charge.retrieve(paymentId, apiKey);
			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", amount.movePointRight(2).intValue());
			//chargeMap.put("currency", "usd");
			charge.refund(chargeMap, apiKey);
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
//			//Charge charge = Charge.retrieve(paymentId, apiKey);
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

	///// THESE ARE NO LONGER NEEDED //////
	private static String getPaymentId(String customerId, String cardId) {
		String paymentToken = CARD_TOKEN_PREFIX + customerId + ":" + cardId;
		return paymentToken;
	}
	
	private static String getCustomerIdFromPaymentId(String paymentId) {
		String paymentTokenWithoutPrefix = StringHelper.removePrefix(CARD_TOKEN_PREFIX, paymentId);
		String[] customerAndCard = paymentTokenWithoutPrefix.split(":");
		if (customerAndCard.length != 2) {
			throw new RuntimeException("invalid payment token");
		}
		String customer = customerAndCard[0];
		return customer;
	}
	private static String getCardIdFromPaymentId(String paymentId) {
		String paymentTokenWithoutPrefix = StringHelper.removePrefix(CARD_TOKEN_PREFIX, paymentId);
		String[] customerAndCard = paymentTokenWithoutPrefix.split(":");
		if (customerAndCard.length != 2) {
			throw new RuntimeException("invalid payment token");
		}
		String card = customerAndCard[1];
		return card;
	}
	/////////////////////////
	
	@Override
	public String store(String customerId, String firstName, String lastName,
			String cardNumber, String expirationMonth, String expirationYear,
			String cvv, String streetAddress, String extendedAddress,
			String city, String state, String postalCode, COUNTRY country) {
		
		Map<String, Object> tokenParams = new HashMap<String, Object>();
		
		Map<String, Object> cardMap = getCardMap(firstName, lastName, cardNumber, expirationMonth, expirationYear, streetAddress, extendedAddress, city, state, postalCode, country, cvv);
		
		tokenParams.put("card", cardMap);

		try {
			// create token
			//Token paymentToken = Token.create(tokenParams, apiKey);
			
			// create customer
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("description", "Customer for " + customerId);
			customerParams.put("card", cardMap);
			
			Customer customer = Customer.create(customerParams, apiKey);
			String newCustomerRecordId = customer.getId();
			
			if (customer.getCards().getCount() > 0) {
				Card customersCard = customer.getCards().getData().get(0);
				String customersCardId = customersCard.getId();

				String paymentToken = getPaymentId(newCustomerRecordId, customersCardId);

				//String prefixedCustomersCardId = StringHelper.addPrefix(CARD_TOKEN_PREFIX, customersCardId);
				
				return paymentToken;
			}
			
			ErrorHandler.userError("failed storage of customer or card");
			
			return null;
		} catch (StripeException e) {
			ErrorHandler.userError("failed storage of card", e);
			return null;
		}
	}
	
	@Override
	public TransactionData read(String id) {
		try {
			Charge payment = Charge.retrieve(id, apiKey);
			TransactionData transaction = chargeToChargeData(payment);
			return transaction;
		} catch (StripeException e) {
			ErrorHandler.userError("reading a payment", e);
			return null;
		}
	}
	
	@Override
	public ListData search(DateData start, DateData end) {
		// TODO make return more than 100 transactions
		
		// TODO parameratize ListData
		ListData transactions = new ListData();
		
		try {
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			
			Map<String, Object> createdParams = new HashMap<String, Object>();
			
			BigDecimal startDecimal = new BigDecimal(start.toRawString());
			startDecimal = startDecimal.movePointLeft(3);
			BigDecimal endDecimal = new BigDecimal(end.toRawString());
			endDecimal = endDecimal.movePointLeft(3);
			
			createdParams.put("gt", startDecimal.toPlainString());
			createdParams.put("lt", endDecimal.toPlainString());
			chargeParams.put("count", MAX_CHARGES_PER_REQUEST);
			chargeParams.put("created", createdParams);
			
			ChargeCollection charges = Charge.all(chargeParams, apiKey);
			
			for (Charge charge : charges.getData()) {

				TransactionData transaction = chargeToChargeData(charge);
				
				transactions.add(transaction);
				
				// NOTE: add refunds as separate transactions
				if (charge.getRefunded()) {
					for (Refund refund : charge.getRefunds()) {
						TransactionData refundtransaction = chargeToChargeData(charge);
						refundtransaction.put("status", "refunded");
						Integer refundAmountInCents = refund.getAmount();
						BigDecimal refundAmountInDollars = new BigDecimal(refundAmountInCents).movePointLeft(2);
						refundtransaction.put("amount", refundAmountInDollars.toPlainString());
						transactions.add(refundtransaction);
					}
				}
			}
		} catch (StripeException e) {
			return null;
		}
		
		return transactions;
	}

//	@Override
//	public List<Map<String, String>> search(Date start, Date end,
//			String firstName, String lastName, String orderId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	private TransactionData chargeToChargeData(Charge charge) {
		try {
			String chargeId = charge.getId();
			final Long createdInSeconds = charge.getCreated();
			final Long createdInMillisecs = createdInSeconds * 1000;
			String created = String.valueOf(createdInMillisecs);
			
			// parse name
			String name = charge.getCard().getName();
			String firstName = null;
			String lastName = null;
			if (name != null) {
				String[] nameParts = name.split(" ");
				if (nameParts.length == 2) {
					firstName = nameParts[0];
					lastName = nameParts[1];
				}
			}
			
			Integer amount = charge.getAmount();
			BigDecimal amountDecimal = new BigDecimal(amount);
			amountDecimal = amountDecimal.movePointLeft(2);
			
	//		String amountStr = (amount.intValue() / CENTS_PER_DOLLAR) + "." + (amount.intValue() % CENTS_PER_DOLLAR);
			String amountStr = amountDecimal.toPlainString();
			
	
			STATUS status = charge.getPaid() ? TransactionData.STATUS.settled : Boolean.TRUE.equals(charge.getDisputed()) ? TransactionData.STATUS.disputed : TransactionData.STATUS.authorized;
			
			String currency = charge.getCurrency();
			
			String orderId = charge.getDescription();
			
			TransactionData transaction = new TransactionData(chargeId, created, firstName, lastName, amountStr, currency, status, orderId);
			
			return transaction;
		} catch (Exception e) {
			String chargeId = charge.getId();
			STATUS status = TransactionData.STATUS.error;
			TransactionData transaction = new TransactionData(chargeId, null, null, null, null, null, status, null);
			return transaction;
		}
	}
	
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
	
	public static void main(String[] args) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		StripeProvider provider = new StripeProvider("sk_test_q6jpEB3qjQvSYVgns7lOwq2J");
		TransactionData transaction = provider.read("ch_4JSmmZYwtd2vwM");
		System.out.println("Transaction = " + transaction);
	}
	
	public static void main2(String[] args) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		//StripeProvider stripe = new StripeProvider("sk_live_rdEyvXYbqIhELQmB0nUWdDi8");
		
		Stripe.apiKey = "sk_live_dAZbPmgrZps7KNj1YOKam0RT";
		
		String tokens[] = new String[] { 
				"tok_103m732TV7CyZXYX9EYqIzSI",
				"tok_103lpJ2TV7CyZXYX2vE1A6pe",
				"tok_103llb2TV7CyZXYXJ7MkzCwK",
				"tok_103l8e2TV7CyZXYXwkqp6Kwr",
				"tok_103l7C2TV7CyZXYXC9A3N9lZ",
				"tok_103kt22TV7CyZXYXIYnrsniY",
				"tok_103jHW2TV7CyZXYXHdTUmFyK",
				"tok_103hxt2TV7CyZXYXzonRXt7q",
				"tok_103hiA2TV7CyZXYXjuX1APLj",
				"tok_103gKs2TV7CyZXYXvT2OnmyQ",
				"tok_103eqM2TV7CyZXYXOvtCxyT2",
				"tok_103eef2TV7CyZXYXe6ai2bWv",
				"tok_103dWI2TV7CyZXYXdTbpeWQr",
				"tok_103d6H2TV7CyZXYXYfTpEbnZ",
				"tok_103cRP2TV7CyZXYXmnYGLDN5",
				"tok_103dBl2TV7CyZXYXx3vGbnAw",
				"tok_103dgR2TV7CyZXYXJQDVxjua",
				"tok_103e272TV7CyZXYX915zqHt6",
				"tok_103hEx2TV7CyZXYXTRWW5ATC",
				"tok_103kUx2TV7CyZXYXS0vmdA0y",
				"tok_103rpk2TV7CyZXYXWe7YeZdO",
				"tok_103rm92TV7CyZXYXuMhE1xh1",
				"tok_103rjk2TV7CyZXYXT8n60iLU",
				"tok_103rX82TV7CyZXYXgbSFB698",
				"tok_103rFl2TV7CyZXYXj5oa4XvT",
				"tok_103r1V2TV7CyZXYXUK9XWadt",
				"tok_103qtQ2TV7CyZXYXxaYIMsyj",
				"tok_103qiP2TV7CyZXYXwSGAqEjF",
				"tok_103qhU2TV7CyZXYXFSiuotMs",
				"tok_103qVi2TV7CyZXYXJBLPRrDq",
				"tok_103qIr2TV7CyZXYXEsmBeTsW",
				"tok_103pwO2TV7CyZXYXf2eu1kgn",
				"tok_103pc12TV7CyZXYXVU6SJ9z0",
				"tok_103pVB2TV7CyZXYXCcMTP7mc",
				"tok_103oSD2TV7CyZXYXkmuNPaz9",
				"tok_103o6r2TV7CyZXYXnGruTFrT",
				"tok_103o1E2TV7CyZXYXcpArlF9m",
				"tok_103nhd2TV7CyZXYXtfnqQnRY",
				"tok_103ngC2TV7CyZXYXCMIsrgb3",
				"tok_103nV12TV7CyZXYXeT9GA5Ct",
				"tok_103n9t2TV7CyZXYX7XdSEM5Z"
						};
	
		System.out.println("Id\tToken\tCustomer\tCard:");

		int counter = 20;
		for (String token : tokens) {
			counter++;
			
			try {
				Map<String, Object> customerParams = new HashMap<String, Object>();
				customerParams.put("description", "Subscription Customer");
				customerParams.put("card", token);

				Customer customer = Customer.create(customerParams);
				
				String newCustomerId = customer.getId();
				
				System.out.print(counter);
				System.out.print("\t" + token);
				System.out.print("\t" + newCustomerId);
				
				CustomerCardCollection cards = customer.getCards();
				if (cards != null) {
					for (Card card : cards.getData()) {
						System.out.print("\t" + card.getId());
					}
				}
				
				System.out.println("");
			} catch (Exception e) {
				System.out.println(counter + "\t" + e.getMessage());
			}
			
		}
		
		
	}

	
}
