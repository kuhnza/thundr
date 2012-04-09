package com.atomicleopard.webFramework.view.json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.MimeTypes;

import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonViewResolver implements ViewResolver<JsonViewResult> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, JsonViewResult viewResult) {
		Object output = viewResult.getOutput();
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson create = gsonBuilder.create();
			String json = create.toJson(output);
			resp.getWriter().write(json);
			resp.setContentType(MimeTypes.MIME_APPLICATION_JSON);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to generate JSON output for object %s: %s", output.toString(), e.getMessage());
		}
	}
}
