package com.threewks.thundr.action.method.bind.request;

import static com.atomicleopard.expressive.Expressive.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class CookieBinder implements ActionMethodBinder {
	private static final ETransformer<Collection<Cookie>, Map<String, List<Cookie>>> toLookup = Expressive.Transformers.<Cookie, String> toBeanLookup("name", Cookie.class);
	private HttpBinder delegate;

	public CookieBinder(HttpBinder delegate) {
		this.delegate = delegate;
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (req.getCookies() != null) {
			Map<String, List<Cookie>> lookup = toLookup.from(list(req.getCookies()));
			for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
				ParameterDescription key = binding.getKey();
				String name = key.name();
				List<Cookie> namedCookies = lookup.get(name);
				Cookie cookie = isNotEmpty(namedCookies) ? namedCookies.get(0) : null;
				if (key.isA(Cookie.class)) {
					bindings.put(key, cookie);
				} else if (binding.getValue() == null && cookie != null) {
					Map<String, String[]> parameterMap = map(name, array(cookie.getValue()));
					delegate.bind(bindings, req, resp, parameterMap);
				}
			}
		}
	}
}
