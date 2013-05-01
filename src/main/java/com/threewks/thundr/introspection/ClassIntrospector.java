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
package com.threewks.thundr.introspection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.ClassUtils;

import jodd.introspector.ClassDescriptor;
import jodd.util.ReflectUtil;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;

public class ClassIntrospector {
	public static final boolean supportsInjection = classExists("javax.inject.Inject");

	@SuppressWarnings({ "rawtypes" })
	public <T> List<Constructor<T>> listConstructors(Class<T> type) {
		ClassDescriptor classDescriptor = new ClassDescriptor(type, true);
		List<Constructor<T>> ctors = Expressive.Transformers.transformAllUsing(ClassIntrospector.<Constructor, Constructor<T>> castTransformer()).to(classDescriptor.getAllCtors(true));
		Collections.sort(ctors, new Comparator<Constructor<T>>() {
			@Override
			public int compare(Constructor<T> o1, Constructor<T> o2) {
				Class<?>[] types1 = o1.getParameterTypes();
				Class<?>[] types2 = o2.getParameterTypes();
				int compare = new Integer(types1.length).compareTo(types2.length);
				if (compare == 0) {
					// to keep the outcome consistent, we want to deterministically sort
					for (int i = 0; compare == 0 && i < types1.length; i++) {
						compare = types1[i].getName().compareTo(types2[i].getName());
					}
				}
				return compare;
			}
		});
		return ctors;
	}

	public <T> List<Method> listSetters(Class<T> type) {
		Method[] methods = ReflectUtil.getSupportedMethods(type);
		List<Method> setters = new ArrayList<Method>();
		for (Method method : methods) {
			if (ReflectUtil.getBeanPropertySetterName(method) != null) {
				setters.add(method);
			}
		}
		return setters;
	}

	public <T> List<Field> listInjectionFields(Class<T> type) {
		List<Field> injectionFields = new ArrayList<Field>();
		if (supportsInjection) {
			Field[] fields = ReflectUtil.getSupportedFields(type);
			for (Field field : fields) {
				boolean shouldInject = field.getAnnotation(Inject.class) != null;
				if (shouldInject) {
					injectionFields.add(field);
				}
			}
		}
		return injectionFields;
	}

	public List<Class<?>> listImplementedTypes(Class<?> type) {
		EList<Class<?>> types = Expressive.<Class<?>>list(type);
		types.addItems(ClassUtils.getAllSuperclasses(type));
		types.addItems(ClassUtils.getAllInterfaces(type));
		return types;
	}

	public static boolean classExists(String name) {
		try {
			Class.forName(name, false, ClassIntrospector.class.getClassLoader());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private static <A, B> ETransformer<A, B> castTransformer() {
		// TODO - This should be migrated to the Expressive library - it looks pretty useful!
		return new ETransformer<A, B>() {
			@Override
			public B from(A from) {
				return (B) from;
			}
		};
	}
}
