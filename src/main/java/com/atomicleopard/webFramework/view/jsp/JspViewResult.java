package com.atomicleopard.webFramework.view.jsp;

import java.util.Collections;
import java.util.Map;

public class JspViewResult {
	private String view;
	private Map<String, Object> model;

	public JspViewResult(String view) {
		this(view, Collections.<String, Object> emptyMap());
	}

	public JspViewResult(String view, Map<String, Object> model) {
		this.view = view;
		this.model = model;
	}

	public String getView() {
		return view;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	@Override
	public String toString() {
		return view;
	}

}
