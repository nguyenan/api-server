package com.wut.resources.file;

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
import com.wut.support.Defaults;
import com.wut.support.Language;
import com.wut.support.domain.DomainUtils;

public class FileResource extends CrudResource {
	
	public FileResource() {
		super("file", null); // TODO fix null here -- use a provider!
	}

	private static final long serialVersionUID = -1678486712182811729L;
//	private DataSourceManager dsm = DataSourceManager.create();
//	private ScalarSource store = dsm.getScalarSource();
	//private static final String TABLE = "_scalars_";
	private static FileProvider provider = new DefaultFileProvider(new S3FileSource());
	
	// TODO change resource name to table and attribute table to type (or
	// schema)

	@Override
	public String getName() {
		return "file";
	}

	// TODO not using scope properly (USERS SHARE DATA!!!!)

	@Override
	public Data read(WutRequest request) throws MissingParameterException {
		Data d = provider.read(getBucket(request), getFolder(request), getFile(request)); 
		
		if (d == null) {
			return MessageData.NO_DATA_FOUND;
		}
		
		return d;
	}

	@Override
	public Data delete(WutRequest request) throws MissingParameterException {
		BooleanData wasDeleted = provider.delete(getBucket(request), getFolder(request), getFile(request));
		return MessageData.successOrFailure(wasDeleted); 
	}

	@Override
	public Data update(WutRequest request) throws MissingParameterException {
		StringData data = request.getParameter("data");
		
		BooleanData wasUpdated = provider.update(getBucket(request), getFolder(request), getFile(request), new StreamData(data.toRawString()));
		
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