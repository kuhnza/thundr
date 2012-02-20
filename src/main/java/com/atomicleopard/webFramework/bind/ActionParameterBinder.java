package com.atomicleopard.webFramework.bind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public interface ActionParameterBinder<T> {
	public T bind(ActionParameter parameter, HttpServletRequest req, HttpServletResponse resp) throws BindException;
}
