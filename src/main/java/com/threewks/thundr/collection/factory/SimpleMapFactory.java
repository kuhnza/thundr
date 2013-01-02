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
package com.threewks.thundr.collection.factory;

import java.util.Map;

import com.threewks.thundr.exception.BaseException;

@SuppressWarnings("rawtypes")
public class SimpleMapFactory<T extends Map> implements MapFactory<T> {

	private Class<T> type;
	private Class<?> instanceType;

	public SimpleMapFactory(Class<T> type, Class<?> instanceType) {
		this.type = type;
		this.instanceType = instanceType;
	}

	@Override
	public Class<T> forType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T create() {
		try {
			return (T) instanceType.newInstance();
		} catch (Exception e) {
			throw new BaseException(e, "Failed to instantiate a collection of type %s", type);
		}
	}

}
