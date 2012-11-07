package com.threewks.thundr.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementations of {@link ViewResolver} determine what content to place in the response for a given request for a given
 * view result.
 * 
 * When a controller returns a view result, the matching {@link ViewResolver} is invoked with that view result. The view resolver
 * is then responsible for populating the response with content, headers etc.
 * 
 * {@link ViewResolver}s are registered in the {@link ViewResolverRegistry} against a view result type.
 * 
 * @param <T>
 * @see ViewResolverRegistry#addResolver(Class, ViewResolver)
 */
public interface ViewResolver<T> {
	public void resolve(HttpServletRequest req, HttpServletResponse resp, T viewResult);
}
