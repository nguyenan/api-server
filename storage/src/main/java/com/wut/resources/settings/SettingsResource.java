/*
package com.wut.resources.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wut.datasources.aws.DynamoDb;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorMessage;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.row.RowProvider;
import com.wut.provider.table.DefaultTableProvider;
import com.wut.provider.table.FlatTableProvider;
import com.wut.provider.table.TableProvider;
import com.wut.provider.table.TableResourceProvider;
import com.wut.provider.table.UniqueRowProvider;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.RudResource;
import com.wut.support.date.DateUtils;

public class SettingsResource extends RudResource {
	
	private static final long serialVersionUID = 1210271770140843757L;
	private TableResourceProvider provider = RowProvider.getRowProvider();
	private static IdData tableScope = new IdData("_settings");
	private static Map<String, Long> customers = new HashMap<String,Long>();
	public static final Long ONE_DAY_IN_MILLISECONDS = 24l * 60l * 60l * 1000l;
	
	private static final List<String> POSSIBLE_SETTINGS = Arrays.asList(new String[] {
		// stripe
		"stripe-live-api-key",
		"stripe-api-key",

		// braintree
		"braintree-mechant-id",
		"braintree-public-key",
		"braintree-private-key",
		
		// email
		"email-from-address",
		"email-domain",
		"email-smtp-host",
		"email-smtp-port",
		"email-username",
		"email-password",
		"password-reset-link",
		"domain",
		"top-level-domain",
		
		// git
		"git.repository",
		"git.branch"
	});
	
			
//	static {
//		// setup provider
//		DynamoDb dynamoDbSource = new DynamoDb();
//		DefaultTableProvider defaultTableProvider = new DefaultTableProvider(dynamoDbSource);
//		FlatTableProvider flatTableProvider = new FlatTableProvider(defaultTableProvider, "flat2");
//		UniqueRowProvider uniqueRowProvider = new UniqueRowProvider(flatTableProvider);
//		provider = uniqueRowProvider;
//	}
	
	
	public SettingsResource() {
		// TODO pass a provider in here
		super("settings", null);
	}
	

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		IdData setting = getSettingIdScope(request);
		if (!POSSIBLE_SETTINGS.contains(setting.toRawString())) {
			return ErrorMessage.INVALID_SETTING;
		} else {
			String customer = request.getCustomer();
			Long lastPulledFromDatabase = customers.get(customer);
			Long now = DateUtils.getEpocheInUTC();
			
			// if its less than a day old
			if (now - lastPulledFromDatabase > ONE_DAY_IN_MILLISECONDS) {
				Data settingValue = getSetting(setting);
				return settingValue;
			}
			
			return ErrorMessage.CACHE_OUT_OF_DATE;
		}
		
	}


	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		IdData setting = getSettingIdScope(request);
		String customer = request.getCustomer();
		String application = request.getApplication();
		String username = request.getUser().getUsername();
		MappedData settingsMap = getSettingMap(request);
		
		BooleanData updateSucceded = (BooleanData) provider.update(new IdData(application), new IdData(customer), new IdData(username), tableScope, setting, settingsMap); 
		
		// TODO fix this method
		return MessageData.successOrFailure(updateSucceded);
	}
	

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
//		IdData settingId = getSettingIdScope(request);
		//BooleanData deleteSucceded = provider.deleteRow(tableScope, settingId);
//		return MessageData.successOrFailure(deleteSucceded);
		return null;
	}

	//// support methods ////

	private static Data getSetting(IdData settingId)
			throws MissingParameterException {
		//MappedData row = provider.getRow(tableScope, settingId);
//		if (row == null) {
//			return MessageData.NO_DATA_FOUND;
//		}
//		StringData settingValue = getSettingValueFrom(row);
//		return settingValue;
		return null;
	}
	
	
	private static IdData getSettingIdScope(WutRequest request) throws MissingParameterException {
		String scopedId = request.getCustomerScopeId();
		//String id = request.getParameter("id");
		return new IdData(scopedId);
	}

	private static MappedData getSettingMap(WutRequest request) throws MissingParameterException {
		StringData settingValue = request.getParameter("value");
		MappedData settingsMap = new MappedData();
		settingsMap.put("value", settingValue);
		return settingsMap;
	}
	
	private static StringData getSettingValueFrom(MappedData settingsMap) throws MissingParameterException {
		StringData settingValue = (StringData) settingsMap.get("value");
		return settingValue;
	}
	
	private static void pullSettings(String client) throws MissingParameterException {
		for (String setting : POSSIBLE_SETTINGS) {
			Data settingValue = getSetting(new IdData(client + ":" + setting));
		}
	}
	
}
*/