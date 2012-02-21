package com.atomicleopard.webFramework.routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	public Object invoke(HttpServletRequest req, HttpServletResponse resp);
}
