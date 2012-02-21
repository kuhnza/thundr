package com.atomicleopard.webFramework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.atomicleopard.webFramework.view.ViewResolver;
import com.atomicleopard.webFramework.view.ViewResult;

public class ViewResolverRegistry {
	private Map<Class<?>, ViewResolver<ViewResult>> resolvers = new HashMap<Class<?>, ViewResolver<ViewResult>>();
	private List<Class<?>> resolversOrder = new ArrayList<Class<?>>();
	private Map<Class<?>, ViewResolver<ViewResult>> resolversCache = new WeakHashMap<Class<?>, ViewResolver<ViewResult>>();

	public ViewResolverRegistry() {

	}

	@SuppressWarnings("unchecked")
	public <T extends ViewResult> void addResolver(Class<T> viewResult, ViewResolver<T> resolver) {
		resolvers.put(viewResult, (ViewResolver<ViewResult>) resolver);
		resolversOrder.add(viewResult);
	}

	public void removeResolver(Class<? extends ViewResult> viewResult) {
		resolvers.remove(viewResult);
		resolversOrder.removeAll(Collections.singletonList(viewResult));
		clearResolversCache();
	}

	private void clearResolversCache() {
		synchronized (resolversCache) {
			resolversCache.clear();
		}
	}

	public ViewResolver<ViewResult> findViewResolver(ViewResult viewResult) {
		ViewResolver<ViewResult> viewResolver = findViewResolverInCache(viewResult);
		if (viewResolver == null) {
			viewResolver = createAndCacheResolver(viewResult);
		}
		return viewResolver;
	}

	protected ViewResolver<ViewResult> createAndCacheResolver(ViewResult viewResult) {
		synchronized (resolversCache) {
			for (int i = resolversOrder.size() - 1; i >= 0; i--) {
				Class<?> resolverClass = resolversOrder.get(i);
				Class<? extends ViewResult> viewResultClass = viewResult.getClass();
				if (resolverClass.isAssignableFrom(viewResultClass)) {
					ViewResolver<ViewResult> viewResolver = resolvers.get(resolverClass);
					resolversCache.put(viewResultClass, viewResolver);
					return viewResolver;
				}
			}
			return null;
		}
	}

	protected ViewResolver<ViewResult> findViewResolverInCache(ViewResult viewResult) {
		synchronized (resolversCache) {
			return (ViewResolver<ViewResult>) resolversCache.get(viewResult);
		}
	}
}
