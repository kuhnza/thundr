package com.threewks.thundr.action.method.bind.http;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.EList;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;

public class HttpBinder implements ActionMethodBinder {
	public static EList<ContentType> supportedContentTypes = list(ContentType.ApplicationFormUrlEncoded, ContentType.TextHtml, ContentType.TextPlain, ContentType.Null);

	public HttpBinder() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (ContentType.anyMatch(supportedContentTypes, req.getContentType())) {
			Map<String, String[]> parameterMap = req.getParameterMap();
			bind(bindings, req, resp, parameterMap);
		}
	}

	public void bind(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String[]> parameterMap) {
		HttpPostDataMap pathMap = new HttpPostDataMap(parameterMap);
		ParameterBinderSet binders = binders(req, resp);
		for (ParameterDescription parameterDescription : bindings.keySet()) {
			if (bindings.get(parameterDescription) == null) {
				Object value = binders.createFor(parameterDescription, pathMap);
				bindings.put(parameterDescription, value);
			}
		}
	}

	private ParameterBinderSet binders(HttpServletRequest req, HttpServletResponse resp) {
		ParameterBinderSet binders = new ParameterBinderSet();
		binders.addDefaultBinders();
		return binders;
	}
}
