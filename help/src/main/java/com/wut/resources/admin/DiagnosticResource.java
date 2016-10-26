package com.wut.resources.admin;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.message.ErrorMessage;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.ResourceGroupAnnotation;
import com.wut.support.ErrorHandler;
import com.wut.support.Language;
import com.wut.support.settings.SettingsManager;

// TODO this resource should include services running, memory usage
// TODO this resource should not be available to the general public. needs to be restricted.

@ResourceGroupAnnotation(name = "diagnostic", group = "admin", desc = "diagnostic infomation")
public class DiagnosticResource extends CrudResource {

	public DiagnosticResource() {
		super("diagnostic", null); // TODO pass in something not null
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		return "diagnostic";
	}
	
	@Override
	public Data read(WutRequest ri) throws MissingParameterException {
		try {
			MappedData data = new MappedData();
			
			String textToEcho = ri.getOptionalParameterAsString("echo");
			if (!Language.isBlank(textToEcho)) {
				data.put("Echo", textToEcho);
			}

			// request information
			//data.put("Referrer", ri.getReferrer());
			data.put("User", ri.getUser().getId());
			// TODO fix end point (now stored as metric???)
			//data.put("End Point", ri.getURI());
			//data.put("Client Agent", ri.getAgent());

			// echo request body
			InputStream content = ri.getBodyData();
			if (content != null) {
				StringBuilder sob = new StringBuilder();
				int c;
				while ((c = content.read()) != -1) {
					sob.append(new Character((char)c));
				}
				data.put("Request Content", sob.toString());
			} else {
				data.put("Request Content", "[empty]");
			}

			// system information
			data.put("Operating System", System.getProperty("os.name"));
			data.put("OS Version", System.getProperty("os.version"));
			data.put("OS Arch", System.getProperty("os.arch"));
			data.put("JVM Name", System.getProperty("java.vm.name"));
			data.put("JVM Vendor", System.getProperty("java.vm.vendor"));
			Date dateTime = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String date = dateFormat.format(dateTime);
			data.put("Current Date", date);
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			String time = timeFormat.format(dateTime);
			data.put("Current Time", time);

			data.put("Phantomjs Version", "1.9.0"); // update to 2.1 on phantom2 branch

			data.put("Web Utility Kit Version", "5.0.6.3");

			data.put("Analytics Base Directory", SettingsManager.getSystemSetting("analytics.base.directory"));

			// application information
			// TODO get this information from somewhere
//		data.put("Application Host", ri.getApplication().getSettings()
//				.getSetting("host"));
//		data.put("Application Port", ri.getApplication().getSettings()
//				.getSetting("port"));

			return data;
		} catch (IOException e) {
			ErrorHandler.systemError(e, "error reading request body");
			return ErrorMessage.FAILURE;
		}
	}

//	@Override
//	public Data create(WutRequest request) throws Exception {
//		return read(request);
//	}
//
//	@Override
//	public Data delete(WutRequest request) throws Exception {
//		return read(request);
//	}
//
//	@Override
//	public Data update(WutRequest request) throws Exception {
//		return read(request);
//	}

}
