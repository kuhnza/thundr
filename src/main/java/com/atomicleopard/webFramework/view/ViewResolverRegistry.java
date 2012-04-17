package com.atomicleopard.webFramework.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.atomicleopard.webFramework.logger.Logger;

public class ViewResolverRegistry {
	private Map<Class<?>, ViewResolver<?>> resolvers = new HashMap<Class<?>, ViewResolver<?>>();
	private List<Class<?>> resolversOrder = new ArrayList<Class<?>>();
	private Map<Class<?>, ViewResolver<?>> resolversCache = new WeakHashMap<Class<?>, ViewResolver<?>>();

	public <T> void addResolver(Class<T> viewResult, ViewResolver<T> resolver) {
		resolvers.put(viewResult, (ViewResolver<T>) resolver);
		resolversOrder.add(viewResult);
		clearResolversCache();
		Logger.info("Added ViewResolver %s for views of %s", resolver, viewResult);
	}

	public void removeResolver(Class<?> viewResult) {
		resolvers.remove(viewResult);
		resolversOrder.removeAll(Collections.singletonList(viewResult));
		clearResolversCache();
	}

	private void clearResolversCache() {
		synchronized (resolversCache) {
			resolversCache.clear();
		}
	}

	public <T> ViewResolver<T> findViewResolver(T viewResult) {
		ViewResolver<T> viewResolver = findViewResolverInCache(viewResult);
		if (viewResolver == null) {
			viewResolver = createAndCacheResolver(viewResult);
		}
		return viewResolver;
	}

	@SuppressWarnings("unchecked")
	protected <T> ViewResolver<T> createAndCacheResolver(T viewResult) {
		synchronized (resolversCache) {
			for (int i = resolversOrder.size() - 1; i >= 0; i--) {
				Class<?> resolverClass = resolversOrder.get(i);
				Class<? extends Object> viewResultClass = viewResult.getClass();
				if (resolverClass.isAssignableFrom(viewResultClass)) {
					ViewResolver<T> viewResolver = (ViewResolver<T>) resolvers.get(resolverClass);
					resolversCache.put(viewResultClass, viewResolver);
					return viewResolver;
				}
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> ViewResolver<T> findViewResolverInCache(T viewResult) {
		synchronized (resolversCache) {
			return (ViewResolver<T>) resolversCache.get(viewResult);
		}
	}
}
