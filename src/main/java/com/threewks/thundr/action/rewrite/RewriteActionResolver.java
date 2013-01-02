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
package com.threewks.thundr.action.rewrite;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;
import com.threewks.thundr.route.RouteType;
import com.threewks.thundr.route.Routes;

public class RewriteActionResolver implements ActionResolver<RewriteAction> {
	private static final Pattern ActionNamePattern = Pattern.compile("^(?i)rewrite:(.+)");

	private Routes routes;

	public RewriteActionResolver(Routes routes) {
		this.routes = routes;
	}

	@Override
	public RewriteAction createActionIfPossible(String actionName) {
		Matcher matcher = ActionNamePattern.matcher(actionName);
		if (matcher.matches()) {
			return new RewriteAction(matcher.group(1));
		}
		return null;
	}

	@Override
	public Object resolve(RewriteAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException {
		String rewriteTo = action.getRewriteTo(pathVars);
		return routes.invoke(rewriteTo, routeType, req, resp);
	}
}
