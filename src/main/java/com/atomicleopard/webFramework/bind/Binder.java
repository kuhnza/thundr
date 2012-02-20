package com.atomicleopard.webFramework.bind;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.bean.BeanTool;
import jodd.bean.BeanUtil;

import com.atomicleopard.webFramework.routes.ActionMethod;
import com.atomicleopard.webFramework.routes.ActionParameter;

public class Binder {

	public void bind(ActionMethod actionMethod, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		for (ActionParameter parameter : actionMethod.parameters.values()) {
			parameters.put(parameter.name, newInstance(parameter.type));
		}

		Map<String, String> requestParameters = request.getParameterMap();
		for (String requestParameter : BeanTool.resolveProperties(requestParameters, false)) {
			Object value = requestParameters.get(requestParameter);
			BeanUtil.setPropertyForcedSilent(parameters, requestParameter, value);
		}
	}

	private Object newInstance(Type type) {
		try {
			return type.getClass().newInstance();
		} catch (Exception e) {
			throw new BindException(e, "Failed to bind parameter, could not instantiate '%s': %s", type, e.getMessage());
		}
	}
}
