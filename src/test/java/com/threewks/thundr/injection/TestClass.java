package com.threewks.thundr.injection;

import javax.inject.Inject;

public class TestClass {
	private String arg1;
	private String arg2;

	@Inject
	private String injectedArg;

	private String settableArg;

	public TestClass(){
		
	}
	public TestClass(String arg1) {
		super();
		this.arg1 = arg1;
	}

	public TestClass(String arg1, String arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public String getArg1() {
		return arg1;
	}

	public String getArg2() {
		return arg2;
	}

	public String getInjectedArg() {
		return injectedArg;
	}

	public String getSettableArg() {
		return settableArg;
	}

	public void setSettableArg(String settableArg) {
		this.settableArg = settableArg;
	}
}
