package com.wut.provider.creditcard;

import java.math.BigDecimal;

import com.wut.model.list.ListData;
import com.wut.model.map.TransactionData;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;

public interface PaymentProvider {
	
	public enum RESPONSE { 
		SUCCESS, UNKNOWN_PROBLEM, CARD_DECLINED;
		private final String transactionId;
		RESPONSE() {
			this.transactionId = null;
		}
		RESPONSE(String transactionId) {
			this.transactionId = transactionId;
		}
		public String getTransactionId() {
			return transactionId;
		}
    } 
	
	public enum CURRENCY { 
		USD
    } 
	
	public enum COUNTRY { 
		US
    } 
	
	// TODO why is amount a string ???
	
	// TODO add currency and order (order id)
	public PaymentResponseData charge(String firstName, String lastName, String cardNumber, String expirationMonth, String expirationYear, String streetAddress, String extendedAddress, String city, String state, String postalCode, COUNTRY country, CURRENCY currency, BigDecimal amount, String cvc, String orderId);
	
	public PaymentResponseData charge(String cardId, CURRENCY currency, BigDecimal amount, String orderId);
	
	public boolean refund(String paymentId, BigDecimal amount);
	
	public PaymentResponseData refund(String paymentId, BigDecimal amount, String tenderId); 
	
	public boolean voidify(String paymentId); 
	
	public boolean settle(String paymentId, BigDecimal amount);
	
	public boolean verify(String paymentId, BigDecimal amount);	
	
	//public List<Map<String, String>> search(Date start, Date end, String firstName, String lastName, String orderId);
	
	public ListData search(DateData start, DateData end);
	
	

	// NOTE: city = locality, state = province = region
	
	public String store(String customerId, String firstName, String lastName, String cardNumber,
			String expirationMonth, String expirationYear, String cvv,
			String streetAddress, String extendedAddress, String city,
			String state, String zipCode, COUNTRY country
			);

	public String store(StringData username, StringData firstName,
			StringData lastName, StringData cardNumber,
			StringData expirationMonth, StringData expirationYear,
			StringData cvv, StringData streetAddress,
			StringData extendedAddress, StringData city, StringData state,
			StringData zipCode, CURRENCY us);

	public TransactionData read(String id);
	
}
