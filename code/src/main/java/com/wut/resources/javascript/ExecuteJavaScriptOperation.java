package com.wut.resources.javascript;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringEscapeUtils;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.scalar.StringData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.common.AbstractOperation;

public class ExecuteJavaScriptOperation extends AbstractOperation {

	public ExecuteJavaScriptOperation() {
		super("execute");
	}

	@Override
	public Data perform(WutRequest request) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    
	    StringData code = request.getParameter("code");
	    //String unescapedCode = StringEscapeUtils.unescapeHtml4(code.toRawString());
	    String unescapedCode = StringEscapeUtils.unescapeJson(code.toRawString());
	    
		Object runResult = engine.eval(unescapedCode);
		
		Invocable invocable = (Invocable) engine;

		Object result = invocable.invokeFunction("main");
		
		String resultStr = String.valueOf(result);
		
		return new StringData(resultStr);
	}

}
