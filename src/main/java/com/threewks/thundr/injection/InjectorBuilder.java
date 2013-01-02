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

public class InjectorBuilder<T> {
	private InjectionContextImpl injector;
	private Class<T> type;
	private String name;
	private T instance;

	protected InjectorBuilder(InjectionContextImpl injector, Class<T> type) {
		this.type = type;
		this.injector = injector;
	}

	protected InjectorBuilder(InjectionContextImpl injector, T instance) {
		this.injector = injector;
		this.instance = instance;
	}

	public InjectorBuilder<T> named(String name) {
		this.name = name;
		return this;
	}

	public void as(Class<? super T> interfaceType) {
		if (type != null) {
			injector.addType(interfaceType, name, type);
		} else {
			injector.addInstance(interfaceType, name, instance);
		}
	}
}
