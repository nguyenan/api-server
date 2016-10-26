package com.wut.resources.payments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;
import com.wut.support.cipher.Cipher;

public class PaymentResource extends CrudResource {

	private static final long serialVersionUID = 4975749783643071769L;

	public PaymentResource() {
		super("payment", null);
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

	@Override
	public Collection<WutOperation> getOperations() {
		Collection<WutOperation> operationList = super.getOperations();
		ArrayList<WutOperation> operationListCopy = new ArrayList<WutOperation>(operationList);
		operationListCopy.add(getVoidOperation());
		operationListCopy.add(getRefundOperation());
		operationListCopy.add(getSettleOperation());
		operationListCopy.add(getStoreOperation());
		// TODO this is put here temporarily for GoodMouth Transition
//		operationListCopy.add(new WutOperation() {
//			@Override
//			public Data perform(WutRequest request) throws Exception {
//				StringData token = request.getParameter("token");
//				String decrypted = new Cipher().decrypt(token.toRawString());
//				return new StringData(decrypted);
//			}
//			
//			@Override
//			public List<OperationParameter> getParameters() {
//				return null;
//			}
//			
//			@Override
//			public String getName() {
//				return "decrypt";
//			}
//			
//			@Override
//			public boolean checkPermission(WutRequest request) {
//				return false;
//			}
//		});
		return operationListCopy;
	}

}
