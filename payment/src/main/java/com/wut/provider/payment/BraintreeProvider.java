package com.wut.provider.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.TransactionSearchRequest;
import com.braintreegateway.ValidationError;
import com.wut.model.list.ListData;
import com.wut.model.map.TransactionData;
import com.wut.model.map.TransactionData.STATUS;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.support.logging.WutLogger;

// TODO this is actual a DataSource
// TODO call this BrainTreeSource
public class BraintreeProvider implements PaymentProvider {

//	private String privateKey;
//	private String publicKey;
//	private String merchantId;
	private BraintreeGateway gateway;
	private static WutLogger logger = WutLogger.create(BraintreeProvider.class);

	public BraintreeProvider(String merchantId, String publicKey,
			String privateKey) {
		this.gateway = new BraintreeGateway(Environment.PRODUCTION, merchantId,
				publicKey, privateKey);
	}
	
	public Map<String, String> readFromSource(String transactionId) {
		Transaction transaction = gateway.transaction().find(transactionId);
		Map<String, String> transactionMap = convertTransactionToMap(transaction);
		return transactionMap;
	}
	
	private static STATUS getStatusFromString(String statusStr) {
		STATUS status;
		if (statusStr.equalsIgnoreCase("AUTHORIZED")) {
			status = TransactionData.STATUS.authorized;
		} else if (statusStr.equalsIgnoreCase("SETTLED")) {
			status = TransactionData.STATUS.settled;
		} else if (statusStr.equalsIgnoreCase("VOIDED")) {
			status = TransactionData.STATUS.voided;
		} else if (statusStr.equalsIgnoreCase("PROCESSOR_DECLINED")) {
			status = TransactionData.STATUS.declined;
		} else if (statusStr.equalsIgnoreCase("AUTHORIZATION_EXPIRED")) {
			status = TransactionData.STATUS.expired;
		} else if (statusStr.equalsIgnoreCase("GATEWAY_REJECTED")) {
			status = TransactionData.STATUS.declined;
		} else {
			status = TransactionData.STATUS.error;
		}
		return status;
	}
	
	@Override
	public TransactionData read(String id) {
		Map<String, String> transactionMap = readFromSource(id);
		//TransactionData transactionData = TransactionData.convert(transactionMap);
		
		String transactionId = transactionMap.get("id");
		String created = transactionMap.get("created");
		String firstName = transactionMap.get("firstName");
		String lastName = transactionMap.get("lastName");
		String amount = transactionMap.get("amount");
		String currency = transactionMap.get("currency");
		String statusStr = transactionMap.get("status");
		STATUS status = getStatusFromString(statusStr);
		String order = transactionMap.get("order");
		
		TransactionData transactionData = new TransactionData(transactionId, created, firstName, lastName, amount, currency, status, order);
		
		return transactionData;
	}
	
	private void addToMap(Map<String, String> map, String key, String value) {
		if (value != null) {
			map.put(key, value);
		}
	}
	
	private Map<String, String> convertTransactionToMap(Transaction transaction) {
		TreeMap<String, String> transactionMap = new TreeMap<String, String>();

		addToMap(transactionMap, "status", transaction.getStatus().name());
		addToMap(transactionMap, "amount", transaction.getAmount().toString());
		addToMap(transactionMap, "order", transaction.getOrderId());
		addToMap(transactionMap, "id", transaction.getId());
//		if (transaction.getBillingAddress() != null) {
//			addToMap(transactionMap, "extendedAddress", transaction.getBillingAddress().getExtendedAddress());
//			addToMap(transactionMap, "city", transaction.getBillingAddress().getLocality());
//			addToMap(transactionMap, "zipCode", transaction.getBillingAddress().getPostalCode());
//			addToMap(transactionMap, "state", transaction.getBillingAddress().getRegion());
//			addToMap(transactionMap, "address", transaction.getBillingAddress().getStreetAddress());
//		}
		if (transaction.getType() != null) {
			addToMap(transactionMap, "type", transaction.getType().name());
		}
		
		if (transaction.getTaxAmount() != null) {
			addToMap(transactionMap, "tax", transaction.getTaxAmount().toString());
		}
		
		addToMap(transactionMap, "created", String.valueOf(transaction.getCreatedAt().getTimeInMillis()));
		
		if (transaction.getCustomer() != null) {
			addToMap(transactionMap, "firstName", transaction.getCustomer().getFirstName());
			addToMap(transactionMap, "lastName", transaction.getCustomer().getLastName());
		}
		return transactionMap;
	}
	
	private TransactionData convertTransactionToTransactionData(Transaction transaction) {
		String id = transaction.getId();
		
		String created = String.valueOf(transaction.getCreatedAt().getTimeInMillis());
		
		String firstName = null, lastName = null;
		if (transaction.getCustomer() != null) {
			firstName = transaction.getCustomer().getFirstName();
			lastName = transaction.getCustomer().getLastName();
		}
		
		String amount = transaction.getAmount().toString();

		String currency = "usd";
		
		String statusStr = transaction.getStatus().name();
		STATUS status = getStatusFromString(statusStr);

		String orderId = transaction.getOrderId();
		
		TransactionData tansactionData = new TransactionData(id,  created,  firstName,  lastName,  amount, currency,  status,  orderId);

		return tansactionData;
	}

	// TODO this goes inside a *Source (datasource) class
	// TODO a *Provider returns ListData<MappedData>
	public List<Map<String, String>> search(Date start, Date end, String firstName, String lastName, String orderId) {
		
		TransactionSearchRequest searchRequest = new TransactionSearchRequest();
		
		if (start != null) {
			//searchRequest = searchRequest.createdAt().greaterThanOrEqual(start);
		}
		if (end != null) {
			//searchRequest = searchRequest.createdAt().lessThanOrEqual(end);
		}
		if (firstName != null) {
			searchRequest = searchRequest.billingFirstName().is(firstName);
		}
		if (lastName != null) {
			searchRequest = searchRequest.billingLastName().is(lastName);
		}
		if (orderId != null) {
			searchRequest = searchRequest.orderId().is(orderId);
		}
		
		ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
		ArrayList<Map<String,String>> transactions = new ArrayList<Map<String,String>>();
		
		for (Transaction transaction : collection) {
			Map<String, String> transactionMap = convertTransactionToMap(transaction);
			transactions.add(transactionMap);
		}
		
		return transactions;

	}
	
	@Override
	public ListData search(DateData start, DateData end) {
		TransactionSearchRequest searchRequest = new TransactionSearchRequest();
		
		if (start != null) {
			//searchRequest.addRangeCriteria(arg0, arg1);
			//searchRequest = searchRequest.createdAt().greaterThanOrEqual(start);
		}
		if (end != null) {
			//searchRequest = searchRequest.createdAt().lessThanOrEqual(end);
		}
		
		ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
		ListData transactions = new ListData();
		
		for (Transaction transaction : collection) {
			TransactionData transactionData = convertTransactionToTransactionData(transaction);
			transactions.add(transactionData);
		}
		
		return transactions;
	}
	
	@Override
	public PaymentResponseData charge(String cardId, CURRENCY currency,
			BigDecimal amount, String orderId) {
		
		TransactionRequest request = new TransactionRequest()
	    .paymentMethodToken(cardId)
	    .amount(amount)
	    .creditCard()
	      .done();

		Result<Transaction> result = gateway.transaction().sale(request);
		
		return verifyTransaction(result);
	}
	
	@Override
	public PaymentResponseData charge(String firstName, String lastName,
			String cardNumber, String expirationMonth, String expirationYear,
			String streetAddress, String extendedAddress, String city,
			String state, String postalCode, COUNTRY country,
			CURRENCY currency, BigDecimal amount, String cvc, String orderId) {
		
		String expiration = expirationMonth + "/" + expirationYear;

		TransactionRequest request = new TransactionRequest()
				.amount(amount)
				.creditCard()
					.number(cardNumber)
					.expirationDate(expiration)
					.done()
				.options()
			        .submitForSettlement(true)
			        .done();

		// auto-settle
		// done().options().submitForSettlement(true).done();

		Result<Transaction> result = gateway.transaction().sale(request);

		return verifyTransaction(result);
		
		
//		TransactionRequest request = new TransactionRequest()
//	    .amount(new BigDecimal("10.00"))
//	    .orderId("order id")
//	    .merchantAccountId("a_merchant_account_id")
//	    .creditCard()
//	        .number("5105105105105100")
//	        .expirationDate("05/2012")
//	        .cardholderName("The Cardholder")
//	        .cvv("cvv")
//	        .done()
//	    .customer()
//	        .firstName("Drew")
//	        .lastName("Smith")
//	        .company("Braintree")
//	        .phone("312-555-1234")
//	        .fax("312-555-1235")
//	        .website("http://www.example.com")
//	        .email("drew@example.com")
//	        .done()
//	    .billingAddress()
//	        .firstName("Paul")
//	        .lastName("Smith")
//	        .company("Braintree")
//	        .streetAddress("1 E Main St")
//	        .extendedAddress("Suite 403")
//	        .locality("Chicago")
//	        .region("Illinois")
//	        .postalCode("60622")
//	        .countryCodeAlpha2("US")
//	        .done()
//	    .shippingAddress()
//	        .firstName("Jen")
//	        .lastName("Smith")
//	        .company("Braintree")
//	        .streetAddress("1 E 1st St")
//	        .extendedAddress("Suite 403")
//	        .locality("Bartlett")
//	        .region("Illinois")
//	        .postalCode("60103")
//	        .countryCodeAlpha2("US")
//	        .done()
//	    .options()
//	        .submitForSettlement(true)
//	        .done();
//
//	Result<Transaction> result = gateway.transaction().sale(request);
	}

	private PaymentResponseData verifyTransaction(Result<Transaction> result) {
		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			System.out.println("Success!: " + transaction.getId());
			String transactionId = transaction.getId();
			return PaymentResponseData.success(transactionId);
		} else if (result.getTransaction() != null) {
			System.out.println("Message: " + result.getMessage());
			Transaction transaction = result.getTransaction();
			System.out.println("Error processing transaction:");
			System.out.println("  Status: " + transaction.getStatus());
			System.out.println("  Code: "
					+ transaction.getProcessorResponseCode());
			System.out.println("  Text: "
					+ transaction.getProcessorResponseText());
			return PaymentResponseData.GENERIC_FAILURE;
		} else {
			System.out.println("Message: " + result.getMessage());
			for (ValidationError error : result.getErrors()
					.getAllDeepValidationErrors()) {
				System.out.println("Attribute: " + error.getAttribute());
				System.out.println("  Code: " + error.getCode());
				System.out.println("  Message: " + error.getMessage());
			}
			return PaymentResponseData.CARD_DECLINED;
		}
	}

	@Override
	public boolean refund(String paymentId, BigDecimal amount) {
		Result<Transaction> result = gateway.transaction().refund(
				paymentId,
			    amount
			);
		return result.isSuccess();
	}

	@Override
	public boolean voidify(String paymentId) {
		Result<Transaction> result = gateway.transaction().voidTransaction(paymentId);
		if (result.isSuccess()) {
		    return true;
		} else {
		    for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
		    	logger.debug(error.getMessage());
		    }
		    return false;
		}
	}

	@Override
	public boolean settle(String paymentId, BigDecimal amount) {
		Result<Transaction> result = gateway.transaction().submitForSettlement(
				paymentId, amount
			);
		return result.isSuccess();
	}

	@Override
	public boolean verify(String paymentId, BigDecimal amount) {
//		CreditCardRequest request = new CreditCardRequest();
//		request = request.cardholderName("John Doe");
//		request = request.cvv("123");
//		request = request.number("4111111111111111");
//		request = request.expirationDate("05/2012");
//		request = request.options().verifyCard(true).done();
//		//request = request.verificationMerchantAccountId("the_merchant_account_id").done();
//
//		Result<CreditCard> result = gateway.creditCard().create(request);
		return false;
	}

//	@Override
//	public String subscribe(String customerId, String firstName, String lastName,
//			String cardNumber, String expirationMonth, String expirationYear, String cvv,
//			String streetAddress, String extendedAddress, String city,
//			String state, String zipCode, COUNTRY country, BigDecimal amount, int billingDay) {
//		
//		String paymentMethodId = store(customerId, firstName, lastName, cardNumber, expirationMonth, expirationYear, cvv, streetAddress, extendedAddress, city, state, zipCode, country);
//
//		SubscriptionRequest request = new SubscriptionRequest();
//		request = request.paymentMethodToken(paymentMethodId);
//		request = request.price(amount);
//		request = request.billingDayOfMonth(billingDay);
//
//		Result<Subscription> result = gateway.subscription().create(request);
//
//		String status = result.getTarget().getStatus().name();
//
//		Subscription subscription = result.getTarget();
//
//		Transaction transaction = subscription.getTransactions().get(0);
//		
//		return subscription.getId();
//	}

	@Override
	public String store(String customerId, String firstName, String lastName, String cardNumber,
			String expirationMonth, String expirationYear, String cvv,
			String streetAddress, String extendedAddress, String city,
			String state, String zipCode, COUNTRY country
			) {
		
		CustomerRequest request = new CustomerRequest()
		.id(customerId)
	    .firstName(firstName)
	    .lastName(lastName)
	    .creditCard()
	        .cardholderName(firstName + " " + lastName)
	        .number(cardNumber)
	        .expirationDate(expirationMonth + "/" + expirationYear)
	        .cvv(cvv)
	        .billingAddress()
	            .streetAddress(streetAddress)
	            .extendedAddress(extendedAddress)
	            .locality(city)
	            .region(state)
	            .postalCode(zipCode)
	            .countryCodeAlpha2(country.name())
	            .done()
	        .done();
		
		Result<Customer> result = gateway.customer().create(request);

		if (!result.isSuccess()) {
			return null;
		}

		Customer customer = result.getTarget();
		
		if (!customer.getId().equals(customerId)) {
			return null;
		}

		String paymentMethodId = customer.getCreditCards().get(0).getToken();
		return paymentMethodId;

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

	@Override
	public PaymentResponseData refund(String paymentId, BigDecimal amount, String tenderId) {
		// TODO Auto-generated method stub
		return null;
	}

}
