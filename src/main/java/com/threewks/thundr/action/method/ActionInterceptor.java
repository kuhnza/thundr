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
package com.threewks.thundr.action.method;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface allows the definition of a simple interceptor strategy for invocation of controller methods.
 * 
 * @param <A>
 */
public interface ActionInterceptor<A extends Annotation> {
	/**
	 * Invoked before the controller method is called, and before binding happens for the controller method. Returning null from this method allows normal execution to continue,
	 * returning anything else results in the controller not being invoked and the returned view being resolved.
	 * 
	 * @param annotation the annotation marking the controller method
	 * @param req
	 * @param resp
	 * @return the view to resolve if this interceptor wishes to resolve the view and prevent the controller being invoked, null otherwise
	 */
	public <T> T before(A annotation, HttpServletRequest req, HttpServletResponse resp);

	/**
	 * Invoked after the controller method is called. Returning null from this method will result in the given view object being resolved as normal, any other
	 * return value will be resolved as the view instead of the controller's returned view.
	 * 
	 * @param annotation the annotation marking the controller method
	 * @param view the view the controller returned after execution
	 * @param req
	 * @param resp
	 * @return the view to resolve instead of the controllers result, or null to use the result of the controller execution
	 */
	public <T> T after(A annotation, Object view, HttpServletRequest req, HttpServletResponse resp);

	/**
	 * Invoked if the controller method execution throws an exception. This method can return a view which will be resolved instead of the exception, or null
	 * if the exception should be resolved to a view normally.
	 * 
	 * @param annotation the annotation marking the controller method
	 * @param e the exception thrown from the controller method
	 * @param req
	 * @param resp
	 * @return the view to resolve instead of the exception, or null to resolve as normal
	 */
	public <T> T exception(A annotation, Exception e, HttpServletRequest req, HttpServletResponse resp);
}
