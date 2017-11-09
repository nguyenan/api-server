package com.wut.model.message;

import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;

@SuppressWarnings("unused")
public class PaymentResponseData extends MessageData {
	private MessageData error;
	private String transactionId;

	public PaymentResponseData(MessageData error) {
		super(error);
		this.error = error;
		put("transaction", "error");
	}

	private PaymentResponseData(String transactionId) {
		super(MessageData.SUCCESS);
		this.error = MessageData.SUCCESS;
		this.transactionId = transactionId;
		put("transaction", transactionId);
	}

	public static PaymentResponseData success(String id) {
		return new PaymentResponseData(id);
	}

	private PaymentResponseData(MappedData result) {
		super(MessageData.SUCCESS);
		this.error = MessageData.SUCCESS;
		this.transactionId = result.get("id").toString();
		put("transaction", result.get("id").toString());

		if (result.get("status") != null)
			put("status", result.get("status").toString());
		if (result.get("tenderId") != null)
			put("tenderId", result.get("tenderId").toString());
	}

	public static PaymentResponseData success(MappedData result) {
		return new PaymentResponseData(result);
	}

	public static PaymentResponseData GENERIC_FAILURE = new PaymentResponseData(MessageData.CARD_DECLINED);
	public static PaymentResponseData CARD_DECLINED = new PaymentResponseData(MessageData.CARD_DECLINED);
	public static PaymentResponseData CARD_NOT_SUPPORTED = new PaymentResponseData(MessageData.CARD_DECLINED);

}
