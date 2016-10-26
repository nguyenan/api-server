package com.wut.resources.payments;

import java.math.BigDecimal;

import com.wut.model.Data;
import com.wut.model.message.PaymentResponseData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.creditcard.PaymentProvider;
import com.wut.provider.creditcard.PaymentProvider.COUNTRY;
import com.wut.provider.creditcard.PaymentProvider.CURRENCY;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.cipher.Cipher;

public class CreatePaymentOperation extends ParameteredOperation {
	private static final Cipher cipher = new Cipher();
	private PaymentOperationHelper paymentHelper = new PaymentOperationHelper();

	public CreatePaymentOperation() {
		addParameter(OperationParameter.string("firstName"));
		addParameter(OperationParameter.string("lastName"));
		addParameter(OperationParameter.string("cardNumber"));
		addParameter(OperationParameter.string("expirationMonth"));
		addParameter(OperationParameter.string("expirationYear"));
		addParameter(OperationParameter.string("cvc"));
		addParameter(OperationParameter.string("order").optional());
	}
	
	@Override
	public String getName() {
		return "create";
	}
	
	@Override
	public Data perform(WutRequest ri) throws Exception {
		String customer = ri.getCustomer();
		PaymentProvider paymentProvider = paymentHelper.getPaymentProvider(customer);
		
		StringData amount = ri.getParameter("amount");

		StringData paymentId = ri.getParameter("payment", true);

		if (paymentId == null) {
			StringData firstName = ri.getParameter("firstName");
			StringData lastName = ri.getParameter("lastName");
			StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
			StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
			StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
			StringData cvv = ri.getParameter("cvc"); // ex: "123"
			
			StringData order = ri.getOptionalParameter("order");
			
			StringData address = ri.getOptionalParameter("address");
			StringData extendedAddress = ri.getOptionalParameter("extendedAddress");
			StringData city = ri.getOptionalParameter("city");
			StringData state = ri.getOptionalParameter("state");
			StringData zip = ri.getOptionalParameter("zip");
			StringData country = ri.getOptionalParameter("country");
			//COUNTRY countryData = country != null ? COUNTRY.valueOf(country.toRawString()) : null;
			
			PaymentResponseData response = paymentProvider.charge(
					firstName.toRawString(), 
					lastName.toRawString(),
					cardNumber.toRawString(),
					expirationMonth.toRawString(),
					expirationYear.toRawString(), 
					address == null ? null : address.toRawString(), /* street address */
					extendedAddress == null ? null : extendedAddress.toRawString(), /* extended address */
					city  == null ? null : city.toRawString(), /* city */
					state  == null ? null : state.toRawString(), /* state */
					zip  == null ? null : zip.toRawString(), /* zip code */
					country == null ? null : COUNTRY.valueOf(country.toRawString()), /* country */
					null /* currency */, 
					new BigDecimal(amount.toRawString()),
					cvv.toRawString(), 
					order == null ? null : String.valueOf(order) /* order id */ );
			
			return response;
		} else {
			StringData paymentIdDecoded = new StringData(cipher.decrypt(paymentId.toRawString()));

			// TODO support currency
			StringData order = ri.getParameter("order", true);
			String orderId = order != null ? order.toRawString() : null;

			PaymentResponseData response = paymentProvider.charge(paymentIdDecoded.toRawString(), CURRENCY.USD, new BigDecimal(amount.toRawString()), orderId);
			
			return response;
		}
	}
	
}

