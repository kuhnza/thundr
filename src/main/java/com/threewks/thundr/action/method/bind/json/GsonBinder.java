package com.threewks.thundr.action.method.bind.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.google.gson.Gson;

public class GsonBinder implements ActionMethodBinder {
	private PathVariableBinder pathVariableBinder;

	public GsonBinder(PathVariableBinder pathVariableBinder) {
		this.pathVariableBinder = pathVariableBinder;
	}

	@Override
	public boolean canBind(String contentType) {
		return ContentType.ApplicationJson.value().equalsIgnoreCase(contentType);
	}

	@Override
	public List<Object> bindAll(List<ParameterDescription> parameterDescriptions, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		List<Object> boundVariables = pathVariableBinder.bindAll(parameterDescriptions, req, resp, pathVariables);
		ParameterDescription jsonParameterDescription = findParameterDescriptionForJsonParameter(parameterDescriptions);
		if (jsonParameterDescription != null) {
			int index = parameterDescriptions.indexOf(jsonParameterDescription);
			try {
				Object converted = new Gson().fromJson(req.getReader(), jsonParameterDescription.type());
				boundVariables.set(index, converted);
			} catch (IOException e) {
				throw new BindException(e, "Failed to bind %s %s using JSON - can only bind the first object parameter", jsonParameterDescription.type(), jsonParameterDescription.name());
			}
		}
		return boundVariables;
	}

	private ParameterDescription findParameterDescriptionForJsonParameter(List<ParameterDescription> parameterDescriptions) {
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			if (!PathVariableBinder.PathVariableTypes.contains(parameterDescription.type())) {
				return parameterDescription;
			}
		}
		return null;
	}
}
