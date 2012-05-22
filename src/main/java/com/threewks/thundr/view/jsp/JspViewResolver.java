package com.threewks.thundr.view.jsp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class JspViewResolver implements ViewResolver<JspView> {
	/**
	 * These values will be available in the model for every jsp resolved by this resolver.
	 * An individual JspView will override the values in this map if it contains an entry with the same key.
	 * 
	 * This can be useful for setting values that apply to every page, or are extremely common. For example,
	 * application environment, domain or version number.
	 */
	private Map<String, Object> globalModel = new HashMap<String, Object>();

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, JspView viewResult) {
		try {
			Map<String, Object> model = viewResult.getModel();
			for (Map.Entry<String, Object> modelEntry : globalModel.entrySet()) {
				req.setAttribute(modelEntry.getKey(), modelEntry.getValue());
			}
			for (Map.Entry<String, Object> modelEntry : model.entrySet()) {
				req.setAttribute(modelEntry.getKey(), modelEntry.getValue());
			}
			String url = resp.encodeRedirectURL(viewResult.getView());
			RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
			requestDispatcher.include(req, resp);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to resolve JSP view %s", viewResult);
		}
	}

	public Object addToGlobalModel(String key, Object value) {
		return globalModel.put(key, value);
	}

	public Object removeFromGlobalModel(String key) {
		return globalModel.remove(key);
	}

	public void addAllToGlobalModel(Map<String, Object> entries) {
		globalModel.putAll(entries);
	}
}
