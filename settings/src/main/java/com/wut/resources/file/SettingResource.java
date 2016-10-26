package com.wut.resources.file;

import java.util.Arrays;
import java.util.List;

import com.wut.datasources.aws.SettingsSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultSettingProvider;
import com.wut.provider.file.SettingProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.Defaults;
import com.wut.support.Language;
import com.wut.support.domain.DomainUtils;

public class SettingResource extends CrudResource {
	
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
	
	public SettingResource() {
		super("setting", null); // TODO fix null here -- use a provider!
	}

	private static final long serialVersionUID = -1678486712182811729L;
	private static SettingProvider provider = new DefaultSettingProvider(new SettingsSource());
	
	@Override
	public String getName() {
		return "setting";
	}

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		StringData settingName = request.getParameter("setting");
		
		// TODO if setting is global setting, first check globals
		
		Data d = provider.read(new IdData(customer), settingName); 
		
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		}
		
		return d;
	}

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		//BooleanData wasDeleted = provider.delete(getBucket(request), getFolder(request), getFile(request));
		return MessageData.error("delete not supported");
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		StringData settingName = request.getParameter("setting");
		StringData data = request.getParameter("value");
		
		BooleanData wasUpdated = BooleanData.TRUE; //provider.update(getBucket(request), getFolder(request), getFile(request), new StreamData(data.toRawString()));
		
		if (wasUpdated == null) {
			return MessageData.NO_DATA_FOUND;
		}
		
		return MessageData.successOrFailure(wasUpdated); 
	}
	
	private IdData getBucket(WutRequest request) {
		String client = request.getCustomer();
		String realDomain = DomainUtils.getRealDomain(client);
		return new IdData(realDomain);
	}
	
	private IdData getFolder(WutRequest request) throws MissingParameterException {
		String application = request.getApplication();
		StringData folder = request.getParameter("bucket", true);
		if (Defaults.isDefaultApplication(application)) {
			return folder != null ? new IdData(folder.toRawString()) : null;
		} else {
			if (!Language.isBlank(folder)) {
				return new IdData(application + "/" + folder.toRawString());
			} else {
				return new IdData(application);
			}
		}
	}
	
	private StringData getFile(WutRequest request) {
		String id = request.getId();
		return new StringData(id);
	}

}