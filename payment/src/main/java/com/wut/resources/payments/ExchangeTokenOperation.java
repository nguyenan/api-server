package com.wut.resources.payments;

import com.wut.backgroundjob.payment.BackgroundJobResource;
import com.wut.datasources.square.SquareSource;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.operations.ParameteredOperation;
import com.wut.support.settings.SettingsManager;

public class ExchangeTokenOperation extends ParameteredOperation {
	private static SquareSource square = new SquareSource();

	@Override
	public String getName() {
		return "exchange-token";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		String customer = request.getCustomer();
		StringData authorizationCode = request.getParameter("authorization-code");

		MappedData result = square.getAccessToken(customer, authorizationCode);
		Integer code = ((IntegerData) result.get("code")).getInteger();
		if (code.equals(MessageData.SUCCESS.getCode())) {
			String accessToken = result.get("access_token").toString();

			// save settings
			SettingsManager.setClientSettings(customer, SquareSource.AUTHORIZE_CODE_SETTING,
					authorizationCode.toRawString());
			SettingsManager.setClientSettings(customer, SquareSource.ACCESS_TOKEN_SETTING, accessToken);
			SettingsManager.setClientSettings(customer, "payment.payment-processor", "square");

			// put to job table
			BackgroundJobResource.pushRenewTokenJob(customer, accessToken);
			return MessageData.SUCCESS;
		} else
			return result;
	}
}
