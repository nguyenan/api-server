package com.wut.resources.photos;

import java.util.ArrayList;
import java.util.Random;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.raw.RawData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ObsoleteCrudResource;
import com.wut.resources.common.ResourceGroupAnnotation;

@ResourceGroupAnnotation(name = "single", group = "photos", desc = "data store for session data")
public class SinglePhotoResource extends ObsoleteCrudResource {

	private static final long serialVersionUID = 7563899849244315413L;

	@Override
	public Data read(WutRequest ri) throws MissingParameterException {
		FlickrSource flickrSrc = new FlickrSource();
		ArrayList<MappedData> flickrUrls = flickrSrc.getFlickrUrls(ri
				.getStringParameter("tags"), "50", "1");
		Random r = new Random();
		int randomIndex = Math.abs(r.nextInt()) % flickrUrls.size();
		String uri = flickrUrls.get(randomIndex).get("url").toString();
		return new RawData(uri);
	}

}
