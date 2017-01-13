
package com.wut.resources.settings;

import java.util.Arrays;
import java.util.List;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorMessage;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.settings.SettingsManager;

public class SettingResource extends CrudResource {

	private static final long serialVersionUID = 1210271770140843757L;

	private static final List<String> POSSIBLE_READ_SETTINGS = Arrays.asList(new String[] {
			// braintree
			"braintree-mechant-id", "braintree-public-key",

			// email
			"email-from-address", "email-domain", "email-smtp-host", "email-smtp-port", "email-username",
			"password-reset-link", "domain", "top-level-domain",
			"theme" });
	
	private static final List<String> POSSIBLE_UPDATE_SETTINGS = Arrays.asList(new String[] {
			// braintree
			"braintree-mechant-id", "braintree-public-key", "braintree-private-key",

			// email
			"email-from-address", "email-domain", "email-smtp-host", "email-smtp-port", "email-username",
			"email-password",
			"theme" });


	public SettingResource() {
		// TODO pass a provider in here
		super("setting", null);
	}

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		String setting = request.getParameter("id").toString();
		if (!POSSIBLE_READ_SETTINGS.contains(setting))
			return ErrorMessage.INVALID_SETTING;

		setting = (setting.equals("theme")) ? "git.branch" : setting;
		String customer = request.getCustomer();
		String settingValue = SettingsManager.getCustomerSettings(customer, setting);
		if (settingValue.isEmpty()) {
			return ErrorMessage.INVALID_SETTING;
		} else { 
			return new StringData(settingValue);
		}
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		String setting = request.getParameter("id").toString();
		String value = request.getParameter("value").toString();
		if (!POSSIBLE_UPDATE_SETTINGS.contains(setting))
			return ErrorMessage.INVALID_SETTING;

		setting = (setting.equals("theme")) ? "git.branch" : setting;
		String customer = request.getCustomer();
		Boolean wasSucessful = SettingsManager.updateCustomerSettings(customer, setting, value);
		return MessageData.successOrFailure(wasSucessful);
	}
	
	@Override
	public Data create(WutRequest request) throws MissingParameterException {
		String customerDomain = request.getCustomer();
		Boolean wasSucessful = SettingsManager.initCustomerSettings(customerDomain);
		return MessageData.successOrFailure(wasSucessful);
	}
	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		return null;		
	} 
}
