package com.threewks.thundr.view.jsp;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class SpecificJspViewResolver<T> implements ViewResolver<T> {
	private String jsp;

	public SpecificJspViewResolver(String jsp) {
		this.jsp = jsp;
	}

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, T viewResult) {
		try {
			req.setAttribute("value", viewResult);
			String url = resp.encodeRedirectURL(jspPath());
			RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
			requestDispatcher.include(req, resp);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to resolve specific JSP view %s", jsp);
		}
	}

	public String jspPath() {
		return jsp.startsWith("/") ? jsp : "/WEB-INF/jsp/" + jsp;
	}
}
