package com.threewks.thundr.action.method.bind.json;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;

public class GsonBinder implements ActionMethodBinder {

	public GsonBinder() {
	}

	public boolean canBind(String contentType) {
		return ContentType.ApplicationJson.value().equalsIgnoreCase(contentType);
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (!bindings.isEmpty()) {
			String sanitisedContentType = ContentType.cleanContentType(req.getContentType());
			if (canBind(sanitisedContentType)) {
				ParameterDescription jsonParameterDescription = findParameterDescriptionForJsonParameter(bindings);
				if (jsonParameterDescription != null) {
					try {
						Object converted = new Gson().fromJson(req.getReader(), jsonParameterDescription.type());
						bindings.put(jsonParameterDescription, converted);
					} catch (IOException e) {
						throw new BindException(e, "Failed to bind %s %s using JSON - can only bind the first object parameter", jsonParameterDescription.type(), jsonParameterDescription.name());
					}
				}
			}
		}
	}

	private ParameterDescription findParameterDescriptionForJsonParameter(Map<ParameterDescription, Object> bindings) {
		for (Map.Entry<ParameterDescription, Object> bindingEntry : bindings.entrySet()) {
			ParameterDescription parameterDescription = bindingEntry.getKey();
			if (bindingEntry.getValue() == null && !PathVariableBinder.PathVariableTypes.contains(parameterDescription.type())) {
				return parameterDescription;
			}
		}
		return null;
	}
}
