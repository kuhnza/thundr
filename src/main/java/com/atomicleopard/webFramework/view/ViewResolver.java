package com.atomicleopard.webFramework.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewResolver<T> {
	public void resolve(HttpServletRequest req, HttpServletResponse resp, T viewResult);
}
