package com.wut.datasources.gateway;

//import java.math.BigDecimal;

//import net.authorize.Environment;
//import net.authorize.Merchant;
//import net.authorize.TransactionType;
//import net.authorize.aim.Result;
//import net.authorize.aim.Transaction;
//import net.authorize.data.creditcard.CreditCard;

// API Login ID 2h3jmGaV2e
// Transaction Key 97ZbX74nBT68me9y
// Your Payment Gateway ID is: 370600

public class AuthorizeDotNetSource {
//	public static void main(String[] args) {
//		//double amount = 1.99;
//		
//		String apiLoginID = "2h3jmGaV2e";
//		String transactionKey = "97ZbX74nBT68me9y";
//		Merchant merchant = Merchant.createMerchant(Environment.SANDBOX,
//				apiLoginID, transactionKey);
//
//		// create credit card
//		CreditCard creditCard = CreditCard.createCreditCard();
//		creditCard.setCreditCardNumber("4111 1111 1111 1111");
//		creditCard.setExpirationMonth("12");
//		creditCard.setExpirationYear("2015");
//
//		// create transaction
//		Transaction authCaptureTransaction = merchant.createAIMTransaction(
//				TransactionType.AUTH_CAPTURE, new BigDecimal(1.99));
//		authCaptureTransaction.setCreditCard(creditCard);
//
//		Result<Transaction> result = (Result<Transaction>) merchant
//				.postTransaction(authCaptureTransaction);
//
//		if (result.isApproved()) {
//			System.out.println("Approved!</br>");
//			System.out.println("Transaction Id: "
//					+ result.getTarget().getTransactionId());
//		} else if (result.isDeclined()) {
//			System.out.println("Declined.</br>");
//			System.out.println(result.getReasonResponseCode() + " : "
//					+ result.getResponseText());
//		} else {
//			System.out.println("Error.</br>");
//			System.out.println(result.getReasonResponseCode() + " : "
//					+ result.getResponseText());
//		}
//	}
}
