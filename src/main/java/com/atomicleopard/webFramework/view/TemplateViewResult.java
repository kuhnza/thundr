package com.atomicleopard.webFramework.view;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jodd.util.MimeTypes;

public class TemplateViewResult {
	private String view;
	private String contentType;
	private Map<String, Object> model;
	private int responseCode;

	public TemplateViewResult(String view, Map<String, Object> model) {
		this(view, model, MimeTypes.MIME_TEXT_HTML);
	}

	public TemplateViewResult(String view, Map<String, Object> model, String contentType) {
		this(view, model, contentType, HttpServletResponse.SC_OK);
	}

	public TemplateViewResult(String view, Map<String, Object> model, String contentType, int responseCode) {
		this.view = view;
		this.model = model;
		this.contentType = contentType;
		this.responseCode = responseCode;
	}

	public String getView() {
		return view;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public String getContentType() {
		return contentType;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
