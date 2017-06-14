package com.wut.resources.file;

import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.support.settings.SettingsManager;

public class BucketResource extends CrudResource {
	
	public BucketResource() {
		super("bucket", null); // TODO fix null here -- use a provider!
	}

	private static final long serialVersionUID = -1678486712182811729L;
	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());
	

	@Override
	public String getName() {
		return "bucket";
	}


	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		return MessageData.NOT_IMPLEMENTED;
	}

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		BooleanData wasSucessful = provider.deleteBucket(getBucket(request));
		return MessageData.successOrFailure(wasSucessful);
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		return MessageData.NOT_IMPLEMENTED;
	}
	
	private IdData getBucket(WutRequest request) {
		String customerDomain = SettingsManager.getClientSettings(request.getCustomer(), "file.domain");
		//String realDomain = DomainUtils.getRealDomain(customerDomain);
		return new IdData(customerDomain);
	}
}