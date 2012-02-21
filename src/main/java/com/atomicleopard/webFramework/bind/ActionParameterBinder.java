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

import com.atomicleopard.webFramework.routes.ActionMethod;
import com.atomicleopard.webFramework.routes.ActionParameter;

public class ActionParameterBinder {

	@SuppressWarnings("unchecked")
	public List<?> bind(ActionMethod actionMethod, HttpServletRequest request, HttpServletResponse response) {
		List<ActionParameterBinderStrategy> strategies = list(new ActionParameterBeanBinderStrategy(request.getParameterMap()), new ActionParameterTypeValueBinderStrategy<ServletRequest>(
				ServletRequest.class, request), new ActionParameterTypeValueBinderStrategy<ServletResponse>(ServletResponse.class, response));
		Map<String, Object> bindings = new LinkedHashMap<String, Object>();
		for (ActionParameter parameter : actionMethod.parameters) {
			bindings.put(parameter.name, null);
		}
		for (ActionParameter actionParameter : actionMethod.parameters) {
			for (ActionParameterBinderStrategy actionParameterBinderStrategy : strategies) {
				actionParameterBinderStrategy.bind(actionParameter, bindings);
			}
		}
		return new ArrayList<Object>(bindings.values());
	}
}
