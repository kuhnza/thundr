package com.threewks.thundr.view.json;

import com.threewks.thundr.view.View;

public class JsonView implements View {
	private Object output;

	public JsonView(Object output) {
		super();
		this.output = output;
	}

	public Object getOutput() {
		return output;
	}
}
