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
package com.threewks.thundr.action.method.bind;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.method.bind.http.ParameterBinder;
import com.threewks.thundr.introspection.ParameterDescription;

/**
 * Used to control how one or more parameters have data bound to them on controller methods.
 * 
 * 
 * An {@link ActionMethodBinder} can be considered a source of data, for example a request, cookies, the session,
 * or the binary content of the request stream.
 * 
 * This is distinct from the {@link ParameterBinder}, which allows conversion of data to a specific argument.
 */
public interface ActionMethodBinder {
	/**
	 * When invoked, implementors can bind any of the given bindings whose value is null (that is those parameters that are not already bound).
	 * Data available to be bound can be retrieved from the request, the response or the pathVariables parameter.
	 * 
	 * @param bindings
	 * @param req
	 * @param resp
	 * @param pathVariables
	 */
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables);
}
