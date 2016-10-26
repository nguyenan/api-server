package com.wut.resources.javascript;

import java.util.ArrayList;
import java.util.Collection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.wut.resources.common.AbstractResource;
import com.wut.resources.common.WutOperation;
import com.wut.resources.templates.CopyTemplateOperation;
import com.wut.resources.templates.InitializeTemplateOperation;
import com.wut.resources.templates.KillTemplateOperation;
import com.wut.resources.templates.ReadTemplateOperation;
import com.wut.resources.templates.RefreshTemplateOperation;
import com.wut.resources.templates.RenderTemplateOperation;

public class JavaScriptResource extends AbstractResource {
	public JavaScriptResource() {
		super("javascript");
	}

	public static void main(String[] args) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.eval("function rules(colour,quantity){var i\n" + 
				"for(i=0;i<quantity;i++)\n" + 
				"print(\"<hr color='\"+colour+\"'>\")}\n" + 
				"print(\"<h2> A function with parameters definition and call</h2>\")\n" + 
				"rules('RED',300)\n" + 
				"print(\"<p>3 red lines and 4 blue ones</p>\")\n" + 
				"rules('BLUE',4)");
	}
	
	@Override
	public Collection<WutOperation> getOperations() {
		ArrayList<WutOperation> operations = new ArrayList<WutOperation>();
		operations.add(new ExecuteJavaScriptOperation());
		return operations;
	}
}
