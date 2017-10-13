package com.wut.provider.payment;

import java.math.BigDecimal;

import com.wut.datasources.square.SquareSource;
import com.wut.model.list.ListData;
import com.wut.model.map.TransactionData;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.StringData;
import com.wut.provider.creditcard.PaymentProvider;
 
public class SquareProvider implements PaymentProvider {
	private SquareSource squareSource;
	@Override
	public PaymentResponseData charge(String firstName, String lastName, String cardNumber, String expirationMonth,
			String expirationYear, String streetAddress, String extendedAddress, String city, String state,
			String postalCode, COUNTRY country, CURRENCY currency, BigDecimal amount, String cvc, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResponseData charge(String cardId, CURRENCY currency, BigDecimal amount, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean refund(String paymentId, BigDecimal amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean voidify(String paymentId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean settle(String paymentId, BigDecimal amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verify(String paymentId, BigDecimal amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ListData search(DateData start, DateData end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String store(String customerId, String firstName, String lastName, String cardNumber, String expirationMonth,
			String expirationYear, String cvv, String streetAddress, String extendedAddress, String city, String state,
			String zipCode, COUNTRY country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String store(StringData username, StringData firstName, StringData lastName, StringData cardNumber,
			StringData expirationMonth, StringData expirationYear, StringData cvv, StringData streetAddress,
			StringData extendedAddress, StringData city, StringData state, StringData zipCode, CURRENCY us) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionData read(String id) {
		// TODO Auto-generated method stub
		return null;
	}
 
}
