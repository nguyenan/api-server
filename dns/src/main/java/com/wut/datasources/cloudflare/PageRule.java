package com.wut.datasources.cloudflare;

import com.google.gson.JsonObject;
import com.wut.model.Data;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.IntegerData;
import com.wut.model.scalar.ScalarData;

public class PageRule {
	public PageRule(String urlPattern, Action[] actions) {
		super();
		this.urlPattern = urlPattern;
		this.actions = actions;
	}

	String urlPattern;

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	Action[] actions;

	public Action[] getActions() {
		return actions;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}
}

class Action {
	public Action(String id, Data value) {
		super();
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String id;
	Data value;

	public Data getValue() {
		return value;
	}

	public void setValue(Data value) {
		this.value = value;
	}

	public JsonObject toJsonObject() {
		JsonObject action = new JsonObject();
		action.addProperty("id", id.toString());
		if (value.getClass().getSimpleName().equals("IntegerData"))
			action.addProperty("value", ((IntegerData) value).getInteger());
		else if (value.getClass().getSimpleName().equals("MappedData")) {
			JsonObject jsonObject = new JsonObject();
			MappedData ret = (MappedData) value;
			for (ScalarData key : ret.keys()) {
				Data value = ret.get(key);
				if (value.getClass().getSimpleName().equals("IntegerData")) {
					jsonObject.addProperty(key.toString(), ((IntegerData) value).getInteger());
				} else
					jsonObject.addProperty(key.toString(), value.toString());
			}
			action.add("value", jsonObject);
		} else
			action.addProperty("value", value.toString());
		return action;
	}
}