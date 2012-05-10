package com.threewks.thundr.action.method.bind;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.introspection.ParameterDescription;

/**
 * Implementors bind all of the parameters to an action method.
 */
public interface ActionMethodBinder {
	public boolean canBind(String contentType);

	public List<Object> bindAll(List<ParameterDescription> parameterDescriptions, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables);
}
