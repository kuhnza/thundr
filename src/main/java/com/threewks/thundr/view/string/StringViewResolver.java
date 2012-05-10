package com.threewks.thundr.view.string;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class StringViewResolver implements ViewResolver<StringView> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, StringView viewResult) {
		try {
			PrintWriter writer = resp.getWriter();
			writer.print(viewResult.content());
			writer.flush();
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to write String view result: %s", e.getMessage());
		}
	}
}
