package com.wut.resources.payments;
/*
import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CreditCard;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;

// API Login ID 2h3jmGaV2e
// Transaction Key 97ZbX74nBT68me9y
// Your Payment Gateway ID is: 370600

public class AuthorizeDotNetSource extends ObsoleteCrudResource {
	private static final long serialVersionUID = 41233413214123L;

	@Override
	public String getName() {
		return "payment";
	}
	
	
	@Override
	public Data update(WutRequest ri) throws MissingParameterException {
//		StringData firstName = ri.getParameter("firstName"); // ex: Rich
//		StringData lastName = ri.getParameter("lastName"); // ex: Fuhler
//		StringData cardNumber = ri.getParameter("cardNumber"); // ex: "4111 1111 1111 1111"
//		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex: "05"
//		StringData expirationYear = ri.getParameter("expirationYear"); // ex: "2009"
//		StringData amount = ri.getParameter("amount"); // ex: "100.00"
		
		String apiLoginID = "2h3jmGaV2e";
		String transactionKey = "97ZbX74nBT68me9y";
		Merchant merchant = Merchant.createMerchant(Environment.SANDBOX,
				apiLoginID, transactionKey);

		// create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber("4111 1111 1111 1111");
		creditCard.setExpirationMonth("12");
		creditCard.setExpirationYear("2015");

		// create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(
				TransactionType.AUTH_CAPTURE, new BigDecimal(1.99));
		authCaptureTransaction.setCreditCard(creditCard);

		@SuppressWarnings("unchecked")
		Result<Transaction> result = (Result<Transaction>) merchant
				.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			System.out.println("Approved!");
			System.out.println("Transaction Id: "
					+ result.getTarget().getTransactionId());
		} else if (result.isDeclined()) {
			System.out.println("Declined.");
			System.out.println(result.getReasonResponseCode() + " : "
					+ result.getResponseText());
		} else {
			System.out.println("Error.");
			System.out.println(result.getReasonResponseCode() + " : "
					+ result.getResponseText());
		}
		
		return MessageData.success();
	}
}
*/