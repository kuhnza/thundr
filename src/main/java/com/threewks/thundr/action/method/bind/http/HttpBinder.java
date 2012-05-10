package com.threewks.thundr.action.method.bind.http;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;

public class HttpBinder implements ActionMethodBinder {
	private PathVariableBinder pathVariableBinder;
	private List<ContentType> supportedContentTypes = Arrays.asList(ContentType.ApplicationFormUrlEncoded, ContentType.TextHtml, ContentType.TextPlain);

	public HttpBinder(PathVariableBinder pathVariableBinder) {
		this.pathVariableBinder = pathVariableBinder;
	}

	@Override
	public boolean canBind(String contentType) {
		// TODO - it seems like this binder should just return true and always be the last attempt
		if (contentType == null) {
			return true;
		}
		for (ContentType supported : supportedContentTypes) {
			if (supported.value().equalsIgnoreCase(contentType)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> bindAll(List<ParameterDescription> parameterDescriptions, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		List<Object> boundVariables = pathVariableBinder.bindAll(parameterDescriptions, req, resp, pathVariables);
		if (!parameterDescriptions.isEmpty()) {
			HttpPostDataMap pathMap = new HttpPostDataMap(req.getParameterMap());
			ParameterBinderSet binders = binders(req, resp);
			for (int index = 0; index < parameterDescriptions.size(); index++) {
				if (boundVariables.get(index) == null) {
					ParameterDescription parameterDescription = parameterDescriptions.get(index);
					Object value = binders.createFor(parameterDescription, pathMap);
					boundVariables.set(index, value);
				}
			}
		}
		return boundVariables;
	}

	private ParameterBinderSet binders(HttpServletRequest req, HttpServletResponse resp) {
		ParameterBinderSet binders = new ParameterBinderSet();
		InstanceParameterBinder requestBinder = new InstanceParameterBinder(req);
		InstanceParameterBinder responseBinder = new InstanceParameterBinder(resp);
		InstanceParameterBinder sessionBinder = new InstanceParameterBinder(req == null ? null : req.getSession());

		binders.addBinder(sessionBinder);
		binders.addBinder(requestBinder);
		binders.addBinder(responseBinder);
		binders.addBinder(requestBinder);
		binders.addBinder(responseBinder);
		binders.addDefaultBinders();
		return binders;
	}
}
