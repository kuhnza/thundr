package com.atomicleopard.webFramework;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.webFramework.injection.BaseInjectionConfiguration;
import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.InjectionContextImpl;
import com.atomicleopard.webFramework.injection.InjectionException;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.routes.RouteType;
import com.atomicleopard.webFramework.routes.Routes;
import com.atomicleopard.webFramework.view.ViewResolver;

public class WebFrameworkServlet extends HttpServlet {
	private static final long serialVersionUID = -7179293239117252585L;
	private static final String DiConfigClassProperty = "diConfigClass";
	private UpdatableInjectionContext injectionContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		String diConfigClass = servletContext.getInitParameter(DiConfigClassProperty);
		injectionContext = new InjectionContextImpl();
		injectionContext.inject(ServletContext.class).as(servletContext);
		InjectionConfiguration injectionConfiguration = getDiConfigInstance(diConfigClass);
		injectionConfiguration.configure(injectionContext);
	}

	private InjectionConfiguration getDiConfigInstance(String diConfigClass) {
		if (diConfigClass == null) {
			Logger.info("No %s specified, defaulting to %s", DiConfigClassProperty, BaseInjectionConfiguration.class.getSimpleName());
			return new BaseInjectionConfiguration();
		}
		try {
			InjectionConfiguration newInstance = Cast.as(Class.forName(diConfigClass).newInstance(), InjectionConfiguration.class);
			if (newInstance == null) {
				throw new InjectionException("Failed to load the specified %s %s class '%s': target does not implement %s", DiConfigClassProperty, InjectionConfiguration.class.getSimpleName(),
						diConfigClass, InjectionConfiguration.class.getSimpleName());
			}
			Logger.info("Loaded %s %s", DiConfigClassProperty, newInstance.getClass().getName());
			return newInstance;
		} catch (Exception e) {
			throw new InjectionException(e, "Failed to load the specified %s %s class '%s': %s", DiConfigClassProperty, InjectionConfiguration.class.getSimpleName(), diConfigClass, e.getMessage());
		}
	}

	protected void applyRoute(RouteType routeType, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ViewResolverRegistry viewResolverRegistry = injectionContext.get(ViewResolverRegistry.class);
		String requestPath = req.getRequestURI();
		try {
			Logger.debug("Invoking path %s", requestPath);
			Routes routes = injectionContext.get(Routes.class);
			Object viewResult = routes.invoke(requestPath, routeType, req, resp);
			if (viewResult != null) {
				ViewResolver<Object> viewResolver = viewResolverRegistry.findViewResolver(viewResult);
				viewResolver.resolve(req, resp, viewResult);
			}
		} catch (Exception e) {
			if (!resp.isCommitted()) {
				ViewResolver<Exception> viewResolver = viewResolverRegistry.findViewResolver(e);
				if (viewResolver != null) {
					viewResolver.resolve(req, resp, e);
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		applyRoute(RouteType.GET, req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("_method");
		RouteType routeType = RouteType.POST;
		if ("PUT".equalsIgnoreCase(method)) {
			routeType = RouteType.PUT;
		} else if ("DELETE".equalsIgnoreCase(method)) {
			routeType = RouteType.DELETE;
		}
		applyRoute(routeType, req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		applyRoute(RouteType.DELETE, req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		applyRoute(RouteType.PUT, req, resp);
	}
}
