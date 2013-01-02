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
package com.threewks.thundr.action.method.bind.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.EListImpl;
import com.threewks.thundr.collection.factory.SimpleCollectionFactory;
import com.threewks.thundr.collection.factory.SimpleMapFactory;
import com.threewks.thundr.introspection.ParameterDescription;

public class ParameterBinderSet {
	private static List<ParameterBinder<?>> intrinsicBinders = binderMap();
	private static List<ParameterBinder<?>> registeredBinders = new ArrayList<ParameterBinder<?>>();

	/**
	 * Allows consumer code to introduce binding for defined/user types
	 * 
	 * @param binder
	 */
	public static <T> void registerGlobalBinder(ParameterBinder<T> binder) {
		registeredBinders.add(binder);
	}

	/**
	 * Removes the given binder which was previously registered. Requires the given object to be equal to the previously registered
	 * binder, so either the same instance or you need to implement equality
	 * 
	 * @param binder
	 */
	public static <T> void unregisterGlobalBinder(ParameterBinder<T> binder) {
		registeredBinders.remove(binder);
	}

	private List<ParameterBinder<?>> binders = new ArrayList<ParameterBinder<?>>();

	public ParameterBinderSet addBinder(ParameterBinder<?> binder) {
		binders.add(binder);
		return this;
	}

	public ParameterBinderSet addDefaultBinders() {
		binders.addAll(registeredBinders);
		binders.addAll(intrinsicBinders);
		return this;
	}

	public List<Object> createFor(List<ParameterDescription> parameterDescriptions, HttpPostDataMap pathMap) {
		List<Object> parameters = new ArrayList<Object>(parameterDescriptions.size());
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			parameters.add(createFor(parameterDescription, pathMap));
		}
		return parameters;
	}

	public Object createFor(ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		for (ParameterBinder<?> binder : binders) {
			if (binder.willBind(parameterDescription)) {
				// return the first non-null object
				Object result = binder.bind(this, parameterDescription, pathMap);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<ParameterBinder<?>> binderMap() {
		List<ParameterBinder<?>> list = new ArrayList<ParameterBinder<?>>();
		list.add(new StringParameterBinder());
		list.add(new ArrayParameterBinder());
		list.add(new CollectionParameterBinder<ArrayList<Object>>(new SimpleCollectionFactory(ArrayList.class, ArrayList.class)));
		list.add(new CollectionParameterBinder<LinkedList<Object>>(new SimpleCollectionFactory(LinkedList.class, LinkedList.class)));
		list.add(new CollectionParameterBinder<EList<Object>>(new SimpleCollectionFactory(EList.class, EListImpl.class)));
		list.add(new CollectionParameterBinder<EListImpl<Object>>(new SimpleCollectionFactory(EListImpl.class, EListImpl.class)));
		list.add(new CollectionParameterBinder<TreeSet<Object>>(new SimpleCollectionFactory(TreeSet.class, TreeSet.class)));
		list.add(new CollectionParameterBinder<HashSet<Object>>(new SimpleCollectionFactory(HashSet.class, HashSet.class)));
		list.add(new CollectionParameterBinder<TreeSet<Object>>(new SimpleCollectionFactory(TreeSet.class, TreeSet.class)));
		list.add(new CollectionParameterBinder<SortedSet<Object>>(new SimpleCollectionFactory(SortedSet.class, TreeSet.class)));
		list.add(new CollectionParameterBinder<Set<Object>>(new SimpleCollectionFactory(Set.class, HashSet.class)));
		list.add(new CollectionParameterBinder<List<Object>>(new SimpleCollectionFactory(List.class, ArrayList.class)));
		list.add(new MapParameterBinder<Map<Object, Object>>(new SimpleMapFactory(HashMap.class, HashMap.class)));
		list.add(new MapParameterBinder<Map<Object, Object>>(new SimpleMapFactory(LinkedHashMap.class, HashMap.class)));
		list.add(new MapParameterBinder<Map<Object, Object>>(new SimpleMapFactory(TreeMap.class, TreeMap.class)));
		list.add(new MapParameterBinder<Map<Object, Object>>(new SimpleMapFactory(SortedMap.class, TreeMap.class)));
		list.add(new MapParameterBinder<Map<Object, Object>>(new SimpleMapFactory(Map.class, HashMap.class)));
		list.add(new CollectionParameterBinder<Collection<Object>>(new SimpleCollectionFactory(Collection.class, ArrayList.class)));
		list.add(new JavaBeanParameterBinder());
		list.add(new BasicTypesParameterBinder());
		list.add(new EnumParameterBinder());
		list.add(new ObjectParameterBinder());
		return list;
	}
}
