package com.wut.resources.payments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.wut.datasources.square.SquareSource;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;

public class PaymentResource extends CrudResource {

	private static final long serialVersionUID = 4975749783643071769L;

	public PaymentResource() {
		super("payment", null);
	}

	@Override
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[] { 
				"payment.payment-processor",
				"payment.braintree-mechant-id",
				"payment.braintree-public-key",
				SquareSource.AUTHORIZE_CODE_SETTING,
				SquareSource.ACCESS_TOKEN_SETTING });
	}

	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[] { 
				"payment.payment-processor", 
				"payment.braintree-mechant-id",
				"payment.braintree-public-key",
				"payment.braintree-private-key",
				SquareSource.AUTHORIZE_CODE_SETTING,
				SquareSource.ACCESS_TOKEN_SETTING });
	}

	@Override
	public UpdatePaymentOperation getUpdateOperation() {
		return new UpdatePaymentOperation();
	}

	@Override
	public CreatePaymentOperation getCreateOperation() {
		return new CreatePaymentOperation();
	}

	@Override
	public ReadPaymentOperation getReadOperation() {
		return new ReadPaymentOperation();
	}

	public RefundPaymentOperation getRefundOperation() {
		return new RefundPaymentOperation();
	}

	public SettlePaymentOperation getSettleOperation() {
		return new SettlePaymentOperation();
	}

	public VoidPaymentOperation getVoidOperation() {
		return new VoidPaymentOperation();
	}

	public StoreCardOperation getStoreOperation() {
		return new StoreCardOperation();
	}

	@Override
	public VoidPaymentOperation getDeleteOperation() {
		return new VoidPaymentOperation();
	}

	/* Square */

	public ExchangeTokenOperation ExchangeTokenOperation() {
		return new ExchangeTokenOperation();
	}

	public ChargeOperation ChargeOperation() {
		return new ChargeOperation();
	}

	@Override
	public Collection<WutOperation> getOperations() {
		Collection<WutOperation> operationList = super.getOperations();
		ArrayList<WutOperation> operationListCopy = new ArrayList<WutOperation>(operationList);

		operationListCopy.add(getVoidOperation());
		operationListCopy.add(getSettleOperation());
		operationListCopy.add(getStoreOperation());

		operationListCopy.add(new GetSettingOperation());
		operationListCopy.add(new SetSettingOperation());

		/* Braintree + Square */
		operationListCopy.add(getRefundOperation());

		/* Square */
		operationListCopy.add(ExchangeTokenOperation());
		operationListCopy.add(ChargeOperation());
		return operationListCopy;
	}

}
