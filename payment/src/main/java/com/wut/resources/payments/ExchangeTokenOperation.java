package com.wut.resources.payments;

import com.wut.datasources.square.SquareSource;
import com.wut.model.Data;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.operations.ReadOperation;

public class ExchangeTokenOperation extends ReadOperation {
	@Override
	public String getName() {
		return "exchange-token";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		StringData authorizationCode = request.getParameter("authorization-code");
		SquareSource square = new SquareSource();
		return square.getAccessToken(customer, authorizationCode);
	}
}
