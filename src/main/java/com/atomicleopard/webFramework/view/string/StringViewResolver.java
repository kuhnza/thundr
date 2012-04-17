package com.atomicleopard.webFramework.view.string;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;

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
