package com.atomicleopard.webFramework.bind;

import java.util.Map;

import com.atomicleopard.webFramework.routes.ActionParameter;

public interface ActionParameterBinderStrategy {
	public void bind(ActionParameter actionParameter, Map<String, Object> bindings) throws BindException;
}
