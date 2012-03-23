package com.atomicleopard.webFramework.bind;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atomicleopard.webFramework.routes.ActionMethod;
import com.atomicleopard.webFramework.routes.ActionParameter;

public class ActionParameterBinder {

	@SuppressWarnings("unchecked")
	public List<?> bind(ActionMethod actionMethod, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		ActionParameterBeanBinderStrategy beanBinder = new ActionParameterBeanBinderStrategy(req.getParameterMap());
		ActionParameterTypeValueBinderStrategy<ServletRequest> requestTypeBinder = new ActionParameterTypeValueBinderStrategy<ServletRequest>(ServletRequest.class, req);
		ActionParameterTypeValueBinderStrategy<ServletResponse> responseTypeBinder = new ActionParameterTypeValueBinderStrategy<ServletResponse>(ServletResponse.class, resp);
		ActionParameterTypeValueBinderStrategy<HttpSession> sessionTypeBinder = new ActionParameterTypeValueBinderStrategy<HttpSession>(HttpSession.class, req.getSession(false));
		// ActionParameterNameTypeValueBinderStrategy<Map> sessionMapTypeBinder = new ActionParameterNameTypeValueBinderStrategy<Map>(Map.class, "sessionAttributes", req.getSession());

		List<ActionParameterBinderStrategy> strategies = list(beanBinder, requestTypeBinder, responseTypeBinder);
		Map<String, Object> bindings = new LinkedHashMap<String, Object>();
		for (ActionParameter parameter : actionMethod.parameters) {
			bindings.put(parameter.name, pathVars.get(parameter.name)); // TODO - Type conversion required!
		}
		for (ActionParameter actionParameter : actionMethod.parameters) {
			for (ActionParameterBinderStrategy actionParameterBinderStrategy : strategies) {
				actionParameterBinderStrategy.bind(actionParameter, bindings);
			}
		}
		return new ArrayList<Object>(bindings.values());
	}
}
