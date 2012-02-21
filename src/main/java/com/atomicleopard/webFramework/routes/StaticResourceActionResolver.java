package com.atomicleopard.webFramework.routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticResourceActionResolver implements ActionResolver<StaticResourceAction> {
	@Override
	public Object resolve(StaticResourceAction action, HttpServletRequest req, HttpServletResponse resp) {
		return null;
	}
}
