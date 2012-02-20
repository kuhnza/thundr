package com.atomicleopard.webFramework.bind;

import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.bean.BeanUtil;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class JavaBeanActionParameterBinder implements ActionParameterBinder<String> {
	@SuppressWarnings("unchecked")
	public String bind(ParameterBinderFinder binderFinder, ActionParameter parameter, HttpServletRequest req, HttpServletResponse resp) {
		String name = parameter.name;
		Type type = parameter.type;
		try {
			Map<String, String> parameterMap = req.getParameterMap();
			Object bean = type.getClass().newInstance();
			for (String param : parameterMap.keySet()) {
				if (param.startsWith(name)) {
					String path = param.substring(name.length() + 1);
					BeanUtil.setPropertyForcedSilent(bean, path, parameterMap.get(param));
				}
			}

			return req.getParameter(name);
		} catch (Exception e) {
			throw new BindException(e, "Failed to bind parameter '%s' to an object of type '%s': %s", name, type, e.getMessage());
		}
	}
}
