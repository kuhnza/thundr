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
