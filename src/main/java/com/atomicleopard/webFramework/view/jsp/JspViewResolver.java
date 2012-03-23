package com.atomicleopard.webFramework.view.jsp;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class JspViewResolver implements ViewResolver<JspViewResult> {
	/*
	 * private ServletContext servletContext;
	 * 
	 * public JspViewResolver(ServletContext servletContext) {
	 * super();
	 * this.servletContext = servletContext;
	 * }
	 */

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, JspViewResult viewResult) {
		try {
			Map<String, Object> model = viewResult.getModel();
			for (Map.Entry<String, Object> modelEntry : model.entrySet()) {
				req.setAttribute(modelEntry.getKey(), modelEntry.getValue());
			}
			RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewResult.getView());
			requestDispatcher.include(req, resp);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to resolve JSP view %s", viewResult);
		}
	}
}
