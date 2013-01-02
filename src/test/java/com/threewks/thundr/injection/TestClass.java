/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.injection;

import javax.inject.Inject;

public class TestClass {
	private String arg1;
	private String arg2;
	private int constructorCalled;

	@Inject
	private String injectedArg;

	private String settableArg;

	public TestClass() {
		constructorCalled = 0;

	}

	public TestClass(String arg1) {
		super();
		constructorCalled = 1;
		this.arg1 = arg1;
	}

	public TestClass(String arg1, String arg2) {
		super();
		constructorCalled = 2;
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

	public int getConstructorCalled() {
		return constructorCalled;
	}

}
