package com.wut.resources.payments;
/*
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.ResponseField;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CreditCard;
import net.authorize.sim.Fingerprint;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;

// API Login ID 2h3jmGaV2e
// Transaction Key 97ZbX74nBT68me9y
// Your Payment Gateway ID is: 370600

// http://localhost:8888/api/hybrid/payment?token='public'&id='payment'&customerId='public'&userId='public'&operation='update'&format='js'&visibility='owner'&callback=jQuery1708800740744918585_1350352847830&application='wut'&parameters='{%22firstName%22:%22Russell%22,%20%22lastName%22:%22Dude%22}'&_=1350352848295

public class AuthorizeDotNetResource extends ObsoleteCrudResource {

	private static final long serialVersionUID = 4457336741461349094L;
	private static String apiLoginID = "8Hym89T2Smg";
	private static String transactionKey = "27m38ewPmD6q72A9";
//	private static String apiLoginID = "2h3jmGaV2e";
//	private static String transactionKey = "97ZbX74nBT68me9y";
	private static Merchant merchant = Merchant.createMerchant(
			Environment.SANDBOX, apiLoginID, transactionKey);

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String MD5HashKey = "1e9c32b7ba6fefa76773d5c79bcdaad0";

		String relayResponseUrl = "http://MERCHANT_HOST/relay_response.jsp";
		String amount = "1.99";
		Fingerprint fingerprint = Fingerprint.createFingerprint(apiLoginID,
				transactionKey, 1234567890, "69" + System.currentTimeMillis()
						+ (new Random()).nextLong() + "3"); // / random sequence
															// used for the
															// fingerprint
															// amount
		long x_fp_sequence = fingerprint.getSequence();
		long x_fp_timestamp = fingerprint.getTimeStamp();
		String x_fp_hash = fingerprint.getFingerprintHash();

		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("x_card_num", new String[] { "4111111111111111" });
		params.put("x_exp_date", new String[] { "091214" }); // 6
		params.put("x_amount", new String[] { "2.99" });
		params.put("x_invoice_num", new String[] { System.currentTimeMillis()
				+ "" });
		params.put("x_relay_url",
				new String[] { "http://www.google.com/relay" });
		params.put("x_login", new String[] { apiLoginID });
		params.put("x_fp_sequence", new String[] { x_fp_sequence + "" });
		params.put("x_fp_timestamp", new String[] { x_fp_timestamp + "" });
		params.put("x_fp_hash", new String[] { x_fp_hash });
		params.put("x_version", new String[] { "3.1" });
		params.put("x_method", new String[] { "CC" });
		params.put("x_type", new String[] { "AUTH_CAPTURE" });
		params.put("x_amount", new String[] { "3.99" });
		params.put("x_test_request", new String[] { "FALSE" });
		params.put("notes", new String[] { "extra hot please" });

		net.authorize.sim.Result result = net.authorize.sim.Result
				.createResult(apiLoginID, MD5HashKey, params);

		//String responseCode = result.getResponseCode().getCode();
		// perform Java server side processing...
		// ...
		// build receipt url buffer
		StringBuffer receiptUrlBuffer = new StringBuffer("");
		if (result != null) {
			receiptUrlBuffer.append("?");
			receiptUrlBuffer.append(ResponseField.RESPONSE_CODE.getFieldName())
					.append("=");
			receiptUrlBuffer.append(result.getResponseCode().getCode());
			receiptUrlBuffer.append("&");
			receiptUrlBuffer.append(
					ResponseField.RESPONSE_REASON_CODE.getFieldName()).append(
					"=");
			receiptUrlBuffer.append(result.getReasonResponseCode()
					.getResponseReasonCode());
			receiptUrlBuffer.append("&");
			receiptUrlBuffer.append(
					ResponseField.RESPONSE_REASON_TEXT.getFieldName()).append(
					"=");
			receiptUrlBuffer.append(result.getResponseMap().get(
					ResponseField.RESPONSE_REASON_TEXT.getFieldName()));
			if (result.isApproved()) {
				receiptUrlBuffer.append("&")
						.append(ResponseField.TRANSACTION_ID.getFieldName())
						.append("=");
				receiptUrlBuffer.append(result.getResponseMap().get(
						ResponseField.TRANSACTION_ID.getFieldName()));
			}
			
			System.out.println(receiptUrlBuffer);
		} else {
			System.out.println("problem!!!");
		}

	}

	@Override
	public String getName() {
		return "payment";
	}

	@SuppressWarnings("unused")
	@Override
	public Data update(WutRequest ri) throws MissingParameterException {
		StringData firstName = ri.getParameter("firstName");
		StringData lastName = ri.getParameter("lastName");
		StringData cardNumber = ri.getParameter("cardNumber"); // ex:
																// "4111 1111 1111 1111"
		StringData expirationMonth = ri.getParameter("expirationMonth"); // ex:
																			// "05"
		StringData expirationYear = ri.getParameter("expirationYear"); // ex:
																		// "2009"
		StringData amount = ri.getParameter("amount"); // ex: "100.00"

		// create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber(cardNumber.toRawString());
		creditCard.setExpirationMonth(expirationMonth.toRawString());
		creditCard.setExpirationYear(expirationYear.toRawString());

		// create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(
				TransactionType.AUTH_CAPTURE,
				new BigDecimal(amount.toRawString()));
		authCaptureTransaction.setCreditCard(creditCard);

		@SuppressWarnings("unchecked")
		Result<Transaction> result = (Result<Transaction>) merchant
				.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			System.out.println("Approved!");
			System.out.println("Transaction Id: "
					+ result.getTarget().getTransactionId());
			return MessageData.success();
		} else if (result.isDeclined()) {
			System.out.println("Card Declined.");
			System.out.println(result.getReasonResponseCode() + " : "
					+ result.getResponseText());
			return MessageData.CARD_DECLINED;
		} else {
			System.out.println("Error.");
			System.out.println(result.getReasonResponseCode() + " : "
					+ result.getResponseText());
			return ErrorData.error(result.getReasonResponseCode() + " : "
					+ result.getResponseText());
		}
	}
}
*/