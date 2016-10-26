package com.wut.pipeline;

import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
import com.wut.support.Language;

// TODO is this really needed? can we just do this at the time of executing the
// request
public class FormatProcessor extends AbstractProcessor {

	@Override
	public boolean process(WutRequest request, WutResponse response) {
		Formatter f = getFormat(request);
		response.setFormat(f);
		
		if (f == null) {
			f = FormatFactory.getDefaultFormatter();
		}
		
		return true;
	}

	// TODO inline this method
	private Formatter getFormat(WutRequest request) {
		String format = request.getFormat();
		Formatter formatter = null;
		if (!Language.isBlank(format)) {
			formatter = FormatFactory.getInstance().getFormatter(format);
		}
		return formatter;
	}
}
