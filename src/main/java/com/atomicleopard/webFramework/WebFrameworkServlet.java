package com.atomicleopard.webFramework;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.scalate.util.IOUtil;

import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.routes.Route;
import com.atomicleopard.webFramework.routes.RouteType;
import com.atomicleopard.webFramework.routes.Routes;
import com.atomicleopard.webFramework.view.JsonViewResolver;
import com.atomicleopard.webFramework.view.JsonViewResult;
import com.atomicleopard.webFramework.view.TemplateViewResolver;
import com.atomicleopard.webFramework.view.TemplateViewResult;
import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;
import com.atomicleopard.webFramework.view.exception.ExceptionViewResolver;
import com.atomicleopard.webFramework.view.jsp.JspViewResolver;
import com.atomicleopard.webFramework.view.jsp.JspViewResult;

public class WebFrameworkServlet extends HttpServlet {
	private static final long serialVersionUID = -7179293239117252585L;
	private Routes routes;
	private ViewResolverRegistry viewResolverRegistry;

	public WebFrameworkServlet() throws ClassNotFoundException {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();

		String routesSource = IOUtil.loadText(this.getClass().getClassLoader().getResourceAsStream("routes.json"), "UTF-8");
		List<Route> routeMap = Routes.parseJsonRoutes(routesSource);
		routes = new Routes(servletContext);
		routes.addRoutes(routeMap);

		viewResolverRegistry = new ViewResolverRegistry();
		viewResolverRegistry.addResolver(TemplateViewResult.class, new TemplateViewResolver(servletContext));
		viewResolverRegistry.addResolver(JsonViewResult.class, new JsonViewResolver());
		viewResolverRegistry.addResolver(JspViewResult.class, new JspViewResolver());
		viewResolverRegistry.addResolver(Throwable.class, new ExceptionViewResolver());
	}

	protected void delegateToController(RouteType routeType, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestPath = req.getRequestURI();
		try {
			Logger.debug("Invoking path %s", requestPath);
			Object viewResult = routes.invoke(requestPath, routeType, req, resp);
			if (viewResult != null) {
				ViewResolver<Object> viewResolver = viewResolverRegistry.findViewResolver(viewResult);
				viewResolver.resolve(req, resp, viewResult);
			}
		} catch (Exception e) {
			if (!resp.isCommitted()) {
				ViewResolver<Exception> viewResolver = viewResolverRegistry.findViewResolver(e);
				if(viewResolver != null){
					viewResolver.resolve(req, resp, e);
				}
			}
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
