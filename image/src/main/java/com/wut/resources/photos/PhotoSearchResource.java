package com.wut.resources.photos;

import com.wut.model.Data;
import com.wut.model.list.ListData;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.support.Language;

// TODO change name to be action. change group to be resource.
@ResourceGroupAnnotation(name = "search", group = "photos", desc = "data store for session data")
public class PhotoSearchResource extends ObsoleteCrudResource {
	
	private static final long serialVersionUID = -7867221872088871290L;

	@Override
	public Data read(WutRequest ri) throws MissingParameterException {
		String perpage = Language.ifNullThenDefault(ri
				.getOptionalParameterAsString("perpage"), "10");
		// TODO if perpage == 1 then return data for single photo
		String page = Language.ifNullThenDefault(ri
				.getOptionalParameterAsString("page"), "1");
		String tags = ri.getStringParameter("term");
		FlickrSource flickrSrc = new FlickrSource();
		return new ListData(flickrSrc.getFlickrUrls(tags, perpage, page));
	}

	@Override
	public Data update(WutRequest ri) {
		return MessageData.NOT_IMPLEMENTED;
	}

}
