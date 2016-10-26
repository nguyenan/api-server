package com.wut.pipeline;

import java.util.HashMap;
import java.util.Map;

import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.MissingParameterException;

// TODO move this to a more generic location
public class FieldSet {
	private final String[] fields;

	public FieldSet(String[] fields) {
		this.fields = fields;
	}

	
	public Map<String,String> getMap(WutRequest request) throws MissingParameterException {
		Map<String,String> data = new HashMap<String, String>();
		for (String requiredField : fields) {
			StringData param = request.getParameter(requiredField);
			data.put(requiredField, param.toRawString());
		}
		return data;
	}
}
