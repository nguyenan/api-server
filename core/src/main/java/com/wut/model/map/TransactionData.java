package com.wut.model.map;

//import java.util.HashMap;
//import java.util.Map;
//
//import com.wut.model.Data;
//import com.wut.model.scalar.BooleanData;
//import com.wut.model.scalar.IntegerData;
//import com.wut.model.scalar.ScalarData;

public class TransactionData extends MappedData {
	public enum STATUS {
		declined,
		authorized,
		settled,
		voided,
		expired,
		refunded,
		error,
		disputed
	}
	
//	private static final long serialVersionUID = -5373420936202243031L;
	
	// TODO use real types here
	public TransactionData(String id, String created, String firstName, String lastName, String amount,String currency, STATUS status, String orderId) {
		// TODO translate real types to WUT types
		put("id", id);
		put("created", created);
		put("firstName", firstName);
		put("lastName", lastName);
		put("amount", amount);
		put("currency", "usd");
		put("status", status.name());
		put("order", orderId);
	}

	// TODO remove this constructor
	public TransactionData(String firstName, String lastName, String cardNumber, String expirationMonth, String expirationYear, String amount, String cvc) {
		//int amoutInCents = (int) (Double.parseDouble(amount) * 100.00d);
		//put("amount", amoutInCents);
		put("amount", amount);
		put("currency", "usd");
		put("number", cardNumber);
//		put("exp_month", Integer.parseInt(expirationMonth));
//		put("exp_year", Integer.parseInt(expirationYear));
		put("exp_month", expirationMonth);
		put("exp_year", expirationYear);
		put("cvc", cvc);
		put("name", firstName + " " + lastName);
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
	}
	
}
