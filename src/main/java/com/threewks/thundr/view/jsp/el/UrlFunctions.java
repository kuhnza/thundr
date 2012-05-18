package com.threewks.thundr.view.jsp.el;

import com.threewks.thundr.http.URLEncoder;

public class UrlFunctions {
	public static String path(String path) {
		return URLEncoder.encodePathComponent(path);
	}

	public static String param(String param) {
		return URLEncoder.encodeQueryComponent(param);
	}
}
