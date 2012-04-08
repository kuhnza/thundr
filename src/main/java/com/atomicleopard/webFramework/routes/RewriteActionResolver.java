package com.atomicleopard.webFramework.routes;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public Object resolve(RewriteAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		String rewriteTo = action.getRewriteTo(pathVars);
		return routes.invoke(rewriteTo, routeType, req, resp);
	}
}
