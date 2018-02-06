package com.wut.resources.file;

import java.util.Arrays;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.Defaults;
import com.wut.support.Language;
import com.wut.support.settings.SettingsManager;

public class FileResource extends CrudResource {
	
	public FileResource() {
		super("file", null); 
	}

	private static final long serialVersionUID = -1678486712182811729L; 
	private FileOperationHelper fileHelper = new FileOperationHelper();
	 

	@Override
	public String getName() {
		return "file";
	}

	@Override
	public List<String> getReadableSettings() {
		return Arrays.asList(new String[]{"file.domain", "file.default-s3-account", "file.s3-account"});
	}
	
	@Override
	public List<String> getWriteableSettings() {
		return Arrays.asList(new String[]{"file.domain", "file.default-s3-account"});
	} 
	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		Data d = fileHelper.getFileProvider(getS3Account(customer)).read(getBucket(request), getFolder(request), getFile(request)); 
		
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		}
		
		return d;
	}

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		BooleanData wasDeleted = fileHelper.getFileProvider(getS3Account(customer)).delete(getBucket(request), getFolder(request), getFile(request));
		return MessageData.successOrFailure(wasDeleted); 
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		String customer = request.getCustomer();
		StringData data = request.getParameter("data");
		
		BooleanData wasUpdated = fileHelper.getFileProvider(getS3Account(customer)).update(getBucket(request), getFolder(request), getFile(request), new StreamData(data.toRawString()));
		
		if (wasUpdated == null) {
			return MessageData.NO_DATA_FOUND;
		}
		
		return MessageData.successOrFailure(wasUpdated); 
	}
	
	public IdData getBucket(WutRequest request) {
		String customerDomain = SettingsManager.getClientSettings(request.getCustomer(), "file.domain");
		//String realDomain = DomainUtils.getRealDomain(customerDomain);
		return new IdData(customerDomain);
	}
	
	public String getS3Account(String customer){ 
		String s3Account = SettingsManager.getClientSettings(customer, "file.s3-account");
		if (s3Account.isEmpty())
			s3Account = SettingsManager.getAdminSettings("file.default-s3-account");
		
		return s3Account;
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