package com.atomicleopard.webFramework.view.redirect;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class RedirectViewResolver implements ViewResolver<RedirectView> {

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, RedirectView viewResult) {
		try {
			resp.sendRedirect(viewResult.getRedirect());
		} catch (IOException e) {
			throw new ViewResolutionException(e, "Failed to redirect to %s: %s", viewResult.getRedirect(), e.getMessage());
		}
	}
}
