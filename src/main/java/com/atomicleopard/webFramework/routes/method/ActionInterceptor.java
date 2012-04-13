package com.atomicleopard.webFramework.routes.method;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionInterceptor<A extends Annotation> {
	public <T> T before(A annotation, HttpServletRequest req, HttpServletResponse resp);

	public <T> T after(A annotation, HttpServletRequest req, HttpServletResponse resp);

	public <T> T exception(A annotation, Exception e, HttpServletRequest req, HttpServletResponse resp);
}
