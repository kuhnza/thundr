package com.atomicleopard.webFramework.bind;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.EList;

public class ParameterBinderFinder {

	private Map<Class<?>, ActionParameterBinder<?>> binders = new HashMap<Class<?>, ActionParameterBinder<?>>();
	private List<Class<?>> registeredBinders = new ArrayList<Class<?>>();
	private WeakHashMap<Class<?>, ActionParameterBinder<?>> foundBinderCache = new WeakHashMap<Class<?>, ActionParameterBinder<?>>();

	public void registerBasicBinders() {
		// java bean
		register(Object.class, null);

		// collections
		register(Collection.class, null);
		register(Set.class, null);
		register(HashSet.class, null);
		register(List.class, null);
		register(LinkedHashSet.class, null);
		register(LinkedList.class, null);
		register(ArrayList.class, null);
		register(EList.class, null);

		// non-basic types
		register(Number.class, null);
		register(Integer.class, null);
		register(BigDecimal.class, null);
		register(BigInteger.class, null);
		// basic types
		register(Float.class, null);
		register(float.class, null);
		register(Double.class, null);
		register(double.class, null);
		register(Integer.class, null);
		register(int.class, null);
		register(String.class, null);

		// Web requests
		register(ServletRequest.class, null);
		register(HttpServletRequest.class, null);
		register(ServletResponse.class, null);
		register(HttpServletResponse.class, null);
	}

	public void register(Class<?> c, ActionParameterBinder<?> binder) {
		binders.put(c, binder);
		registeredBinders.add(c);
	}

	public void unregister(Class<?> c) {
		binders.remove(c);
		registeredBinders.removeAll(Collections.singletonList(c));
	}

	public ActionParameterBinder<?> findBinder(Class<?> type) {
		ActionParameterBinder<?> result = getCachedBinder(type);
		if (result == null) {
			result = findBinderInternal(type);
			result = cacheBinder(type, result);
		}
		return result;
	}

	private ActionParameterBinder<?> getCachedBinder(Class<?> type) {
		synchronized (foundBinderCache) {
			return foundBinderCache.get(type);
		}
	}

	private ActionParameterBinder<?> cacheBinder(Class<?> type, ActionParameterBinder<?> binder) {
		synchronized (foundBinderCache) {
			if (binder != null) {
				foundBinderCache.put(type, binder);
			}
		}
		return binder;
	}

	private ActionParameterBinder<?> findBinderInternal(Class<?> type) {
		for (int i = registeredBinders.size() - 1; i >= 0; i--) {
			Class<?> c = registeredBinders.get(i);
			if (c.isAssignableFrom(type)) {
				return binders.get(c);
			}
		}
		return null;
	}
}
