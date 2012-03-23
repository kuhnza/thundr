package com.atomicleopard.webFramework.view;

public class JsonViewResult {
	private Object output;

	public JsonViewResult(Object output) {
		super();
		this.output = output;
	}

	public Object getOutput() {
		return output;
	}
}
