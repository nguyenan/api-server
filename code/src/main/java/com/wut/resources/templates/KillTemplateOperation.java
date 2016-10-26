package com.wut.resources.templates;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;
import com.wut.threading.WutProcessExecuter;
import com.wut.threading.WutSystemCommandProcess;

public class KillTemplateOperation extends TemplateOperation {

	@Override
	public String getName() {
		return "kill";
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		// TODO get the path out of this command
		// this was done because plain "killall" was not working -- resulted in a process not found error
		WutSystemCommandProcess killall = new WutSystemCommandProcess("/usr/bin/killall", new String[] { "phantomjs" });
		int returnCode = WutProcessExecuter.execute(killall);
		return MessageData.returnCode(returnCode);
	}
	
}
