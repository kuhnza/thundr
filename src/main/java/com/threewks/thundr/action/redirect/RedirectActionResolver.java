package com.threewks.thundr.action.redirect;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;
import com.threewks.thundr.route.RouteType;

public class RedirectActionResolver implements ActionResolver<RedirectAction> {
	private static final Pattern ActionNamePattern = Pattern.compile("^(?i)redirect:(.+)");

	@Override
	public RedirectAction createActionIfPossible(String actionName) {
		Matcher matcher = ActionNamePattern.matcher(actionName);
		if (matcher.matches()) {
			return new RedirectAction(matcher.group(1));
		}
		return null;
	}

	@Override
	public Object resolve(RedirectAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException {
		String redirectTo = action.getRedirectTo(pathVars);
		try {
			resp.sendRedirect(redirectTo);
			return null;
		} catch (Exception e) {
			throw new ActionException(e, "Failed to redirect %s to %s: %s", req.getRequestURI(), redirectTo, e.getMessage());
		}
	}
}
