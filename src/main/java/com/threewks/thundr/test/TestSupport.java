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
package com.threewks.thundr.test;

import java.lang.reflect.Field;

import jodd.util.ReflectUtil;

public class TestSupport {
	public static <T> T setField(T targetObject, String fieldName, Object fieldValue) {
		if (targetObject != null) {
			Class<? extends Object> class1 = getClass(targetObject);
			try {
				Field field = getField(fieldName, class1);
				field.setAccessible(true);
				field.set(targetObject, fieldValue);
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format("Cannot set %s on %s: %s", fieldName, class1, e.getMessage()), e);
			}
		}
		return targetObject;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getField(Object targetObject, String fieldName) {
		try {
			Field field = getField(fieldName, getClass(targetObject));
			field.setAccessible(true);
			return field == null ? null : (T) field.get(targetObject);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static Field getField(String fieldName, Class<? extends Object> class1) {
		Field[] supportedFields = ReflectUtil.getSupportedFields(class1);
		for (Field field : supportedFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	private static <T> Class<? extends Object> getClass(T targetObject) {
		Class<? extends Object> class1 = targetObject.getClass();
		return class1;
	}
}
