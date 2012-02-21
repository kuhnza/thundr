package com.atomicleopard.webFramework;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.KeyValue;

import org.fusesource.scalate.util.IOUtil;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.routes.RouteType;
import com.atomicleopard.webFramework.routes.Routes;
import com.atomicleopard.webFramework.view.JsonViewResolver;
import com.atomicleopard.webFramework.view.JsonViewResult;
import com.atomicleopard.webFramework.view.TemplateViewResolver;
import com.atomicleopard.webFramework.view.TemplateViewResult;
import com.atomicleopard.webFramework.view.ViewResolver;
import com.atomicleopard.webFramework.view.ViewResult;

public class WebFrameworkServlet extends HttpServlet {
	private Routes routes;
	private ViewResolverRegistry viewResolverRegistry;

	public WebFrameworkServlet() throws ClassNotFoundException {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		try {
			String routesSource = IOUtil.loadText(this.getClass().getClassLoader().getResourceAsStream("routes.json"), "UTF-8");
			java.util.Map<String, KeyValue<String, RouteType>> routeMap = Routes.parseJsonRoutes(routesSource);
			routes = new Routes();
			routes.addRoutes(routeMap);

			viewResolverRegistry = new ViewResolverRegistry();
			viewResolverRegistry.addResolver(TemplateViewResult.class, new TemplateViewResolver(servletContext));
			viewResolverRegistry.addResolver(JsonViewResult.class, new JsonViewResolver());
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}

	protected void delegateToController(RouteType routeType, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestPath = req.getPathInfo();
		try {
			Logger.debug("Invoking path %s", requestPath);
			Object result = routes.invoke(requestPath, routeType, req, resp);
			ViewResult viewResult = Cast.as(result, ViewResult.class);
			if (viewResult != null) {
				ViewResolver<ViewResult> viewResolver = viewResolverRegistry.findViewResolver(viewResult);
				viewResolver.resolve(req, resp, viewResult);
			}
		} catch (Exception e1) {
			// TODO - Error handling - an ErrorViewResolver would do the trick
			e1.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		delegateToController(RouteType.GET, req, resp);
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
		delegateToController(routeType, req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		delegateToController(RouteType.DELETE, req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		delegateToController(RouteType.PUT, req, resp);
	}
}
