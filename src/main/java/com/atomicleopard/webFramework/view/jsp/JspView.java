package com.atomicleopard.webFramework.view.jsp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class JspView {
	private String view;
	private Map<String, Object> model;

	public JspView(String view) {
		this(view, new HashMap<String, Object>());
	}

	public JspView(String view, Map<String, Object> model) {
		this.view = view;
		this.model = model;
	}

	public String getView() {
		return completeViewName(view);
	}

	public Map<String, Object> getModel() {
		return model;
	}

	@Override
	public String toString() {
		String completeView = completeViewName(view);
		return completeView.equals(view) ? view : String.format("%s (%s)", view, completeView);
	}

	private String completeViewName(String view) {
		if (!StringUtils.startsWith(view, "/")) {
			view = "/WEB-INF/jsp/" + view;
		}
		if (!StringUtils.endsWith(view, ".jsp")) {
			view = view + ".jsp";
		}
		return view;
	}
}
