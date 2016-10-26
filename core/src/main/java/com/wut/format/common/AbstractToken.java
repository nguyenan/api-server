package com.wut.format.common;

import com.wut.format.Token;
import com.wut.model.Data;
import com.wut.model.Model;

// TODO rename GenericToken or BaseToken or SimpleToken -- class isnt abstract
public class AbstractToken implements Token {

	private Model<? extends Data> model;
	private PURPOSE purpose;
	private String data;
	private boolean optional;

	// TODO this isnt ok!!!
	public AbstractToken(String data) {
		this.model = null;
		this.purpose = null;
		this.data = data;
	}

	public AbstractToken(Model<? extends Data> model, PURPOSE p, String data) {
		this(model, p, data, false);
	}

	// TODO optional not yet supported -- I think it is
	public AbstractToken(Model<? extends Data> model, PURPOSE p, String data,
			boolean optional) {
		this.model = model;
		this.purpose = p;
		this.data = data;
		this.optional = optional;
	}

	@Override
	public Model<? extends Data> getModel() {
		return model;
	}

	@Override
	public void setModel(Model<? extends Data> model) {
		this.model = model;
	}

	@Override
	public PURPOSE getPurpose() {
		return purpose;
	}

	@Override
	public void setPurpose(PURPOSE purpose) {
		this.purpose = purpose;
	}

	@Override
	public String getData() {
		return data;
	}

	@Override
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public boolean matchesBuffer(String buffer, int startPosition) {
		return buffer.startsWith(getData(), startPosition);
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public String toString() {
		return data + " (" + purpose + ")";
	}
}
