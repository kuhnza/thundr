package com.atomicleopard.webFramework.bind;

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
import com.atomicleopard.webFramework.bind.http.PathMap;
import com.atomicleopard.webFramework.collection.factory.SimpleCollectionFactory;
import com.atomicleopard.webFramework.collection.factory.SimpleMapFactory;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class Binders {
	private static List<ParameterBinder<?>> sharedBinders = binderMap();
	private List<ParameterBinder<?>> binders = new ArrayList<ParameterBinder<?>>();

	public Binders addBinder(ParameterBinder<?> binder) {
		binders.add(binder);
		return this;
	}

	public Binders addDefaultBinders() {
		binders.addAll(sharedBinders);
		return this;
	}

	public List<Object> createFor(List<ParameterDescription> parameterDescriptions, PathMap pathMap) {
		List<Object> parameters = new ArrayList<Object>(parameterDescriptions.size());
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			parameters.add(createFor(parameterDescription, pathMap));
		}
		return parameters;
	}

	public Object createFor(ParameterDescription parameterDescription, PathMap pathMap) {
		for (ParameterBinder<?> binder : binders) {
			if (binder.willBind(parameterDescription)) {
				return binder.bind(this, parameterDescription, pathMap);
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<ParameterBinder<?>> binderMap() {
		List<ParameterBinder<?>> list = new ArrayList<ParameterBinder<?>>();
		list.add(new StringParameterBinder());
		list.add(new ArrayParameterBinder());
		list.add(new CollectionParameterBinder<ArrayList<Object>>(new SimpleCollectionFactory(List.class, ArrayList.class)));
		list.add(new CollectionParameterBinder<LinkedList<Object>>(new SimpleCollectionFactory(List.class, ArrayList.class)));
		list.add(new CollectionParameterBinder<EList<Object>>(new SimpleCollectionFactory(EList.class, EListImpl.class)));
		list.add(new CollectionParameterBinder<EListImpl<Object>>(new SimpleCollectionFactory(EList.class, EListImpl.class)));
		list.add(new CollectionParameterBinder<TreeSet<Object>>(new SimpleCollectionFactory(TreeSet.class, TreeSet.class)));
		list.add(new CollectionParameterBinder<HashSet<Object>>(new SimpleCollectionFactory(Set.class, HashSet.class)));
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
		list.add(new ObjectParameterBinder());
		return list;
	}
}
