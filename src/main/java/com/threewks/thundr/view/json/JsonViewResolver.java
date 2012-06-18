package com.threewks.thundr.view.json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.MimeTypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class JsonViewResolver implements ViewResolver<JsonView> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, JsonView viewResult) {
		Object output = viewResult.getOutput();
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson create = gsonBuilder.create();
			String json = create.toJson(output);
			resp.getWriter().write(json);
			resp.setContentType(MimeTypes.MIME_APPLICATION_JSON);
			resp.setContentLength(json.getBytes().length);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to generate JSON output for object '%s': %s", output.toString(), e.getMessage());
		}
	}
}
