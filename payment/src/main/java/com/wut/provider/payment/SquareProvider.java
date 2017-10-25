package com.wut.provider.payment;

import java.math.BigDecimal;

import com.wut.datasources.square.SquareSource;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.map.TransactionData;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.DateData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StringData;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.support.logging.WutLogger;

public class SquareProvider implements PaymentProvider {

	private SquareSource square;
	private String accessToken;
	private static WutLogger logger = WutLogger.create(SquareProvider.class);

	public SquareProvider(String accessToken) {
		this.accessToken = accessToken;
		square = new SquareSource();

	}

	@Override
	public TransactionData read(String id) {
		return null;
	}

	// Read list transaction directly from Square - not in-use
	@Override
	public ListData search(DateData start, DateData end) {
		return null;
	}

	@Override
	public PaymentResponseData charge(String cardNonce, CURRENCY currency, BigDecimal amount, String orderId) {
		MappedData result = square.charge(accessToken, cardNonce, Integer.valueOf((amount.intValue())));
		return verifyTransaction(result);
	}

	@Override
	public PaymentResponseData charge(String firstName, String lastName, String cardNumber, String expirationMonth,
			String expirationYear, String streetAddress, String extendedAddress, String city, String state,
			String postalCode, COUNTRY country, CURRENCY currency, BigDecimal amount, String cvc, String orderId) {

		return null;

	}

	private PaymentResponseData verifyTransaction(MappedData result) {
		Integer code = ((IntegerData) result.get("code")).getInteger();
		if (code.equals(MessageData.SUCCESS.getCode())) {
			String transactionId = result.get("id").toString();
			return PaymentResponseData.success(transactionId);
		} else
			return new PaymentResponseData((MessageData) result);
	}

	@Override
	public boolean refund(String paymentId, BigDecimal amount) {
		return false;
	}

	@Override
	public boolean voidify(String paymentId) {
		return false;
	}

	@Override
	public boolean settle(String paymentId, BigDecimal amount) {
		return false;
	}

	@Override
	public boolean verify(String paymentId, BigDecimal amount) {
		return false;
	}

	@Override
	public String store(String customerId, String firstName, String lastName, String cardNumber, String expirationMonth,
			String expirationYear, String cvv, String streetAddress, String extendedAddress, String city, String state,
			String zipCode, COUNTRY country) {

		return null;

	}

	@Override
	public String store(StringData username, StringData firstName, StringData lastName, StringData cardNumber,
			StringData expirationMonth, StringData expirationYear, StringData cvv, StringData streetAddress,
			StringData extendedAddress, StringData city, StringData state, StringData zipCode, CURRENCY currency) {

		return null;
	}

}
