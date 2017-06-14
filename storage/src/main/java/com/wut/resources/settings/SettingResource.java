
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

	public SettingResource() {
		// TODO pass a provider in here
		super("setting", null);
	}
	
	@Override
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[]{"theme"});
	}
	
	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[]{"theme"});
	}

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		String setting = request.getParameter("id").toString();
		List<String> readableSettings = getReadableSettings();
		if (!readableSettings.contains(setting))
			return ErrorMessage.INVALID_SETTING;
		setting = (setting.equals("theme")) ? "template.git.branch" : setting;
		String clientSettings = SettingsManager.getClientSettings(customer, setting);
		if (clientSettings.isEmpty())
			return MessageData.NO_DATA_FOUND;
		return new StringData(clientSettings);
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		String setting = request.getParameter("id").toString();
		String value = request.getParameter("value").toString();
		List<String> writeableSettings = getWriteableSettings();
		if (!writeableSettings.contains(setting))
			return ErrorMessage.INVALID_SETTING;
		
		setting = (setting.equals("theme")) ? "template.git.branch" : setting;
		Boolean updateCustomerSettings = SettingsManager.setClientSettings(customer, setting, value);
		return MessageData.successOrFailure(updateCustomerSettings);
	}
	
	@Override
	public Data create(WutRequest request) throws MissingParameterException {
		String customerDomain = request.getCustomer();
		Boolean wasSucessful = SettingsManager.initClientSettings(customerDomain);
		return MessageData.successOrFailure(wasSucessful);
	}
	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		return ErrorMessage.NOT_IMPLEMENTED;	
	} 
}
