package com.wut.resources.file;

import com.wut.datasources.CrudSource;
import com.wut.datasources.aws.S3FileSource;
import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.pipeline.WutRequest;
import com.wut.provider.file.DefaultFileProvider;
import com.wut.provider.file.FileProvider;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;
import com.wut.support.Language;
import com.wut.support.settings.SettingsManager;

public class FileResource extends CrudResource {
	
	public FileResource() {
		super("queue", null); // TODO fix null here -- use a provider!
	}

	private static final long serialVersionUID = -1678486712182811729L;
	//private static FileProvider provider = new DefaultFileProvider(new S3FileSource());
	
	@Override
	public String getName() {
		return "queue";
	}


	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		return null;

	}

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		return null;
		
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		return null;
	}
	

	

}