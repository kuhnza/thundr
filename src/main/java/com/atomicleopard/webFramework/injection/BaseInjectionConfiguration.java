package com.atomicleopard.webFramework.injection;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.fusesource.scalate.util.IOUtil;

import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.webFramework.ViewResolverRegistry;
import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.routes.MethodAction;
import com.atomicleopard.webFramework.routes.MethodActionResolver;
import com.atomicleopard.webFramework.routes.RedirectAction;
import com.atomicleopard.webFramework.routes.RedirectActionResolver;
import com.atomicleopard.webFramework.routes.RewriteAction;
import com.atomicleopard.webFramework.routes.RewriteActionResolver;
import com.atomicleopard.webFramework.routes.Route;
import com.atomicleopard.webFramework.routes.Routes;
import com.atomicleopard.webFramework.routes.StaticResourceAction;
import com.atomicleopard.webFramework.routes.StaticResourceActionResolver;
import com.atomicleopard.webFramework.view.JsonViewResolver;
import com.atomicleopard.webFramework.view.JsonViewResult;
import com.atomicleopard.webFramework.view.TemplateViewResolver;
import com.atomicleopard.webFramework.view.TemplateViewResult;
import com.atomicleopard.webFramework.view.exception.ExceptionViewResolver;
import com.atomicleopard.webFramework.view.jsp.JspViewResolver;
import com.atomicleopard.webFramework.view.jsp.JspViewResult;

public class BaseInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		loadProperties(injectionContext);

		Routes routes = new Routes();
		injectionContext.inject(Routes.class).as(routes);
		addRouteActionResolvers(routes, injectionContext);
		addRoutes(routes, injectionContext);

		ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
		injectionContext.inject(ViewResolverRegistry.class).as(viewResolverRegistry);
		addViewResolvers(viewResolverRegistry, injectionContext);

		addServices(injectionContext);
	}

	private void addRouteActionResolvers(Routes routes, UpdatableInjectionContext injectionContext) {
		ServletContext servletContext = injectionContext.get(ServletContext.class);
		routes.addActionResolver(RedirectAction.class, new RedirectActionResolver());
		routes.addActionResolver(RewriteAction.class, new RewriteActionResolver(routes));
		routes.addActionResolver(StaticResourceAction.class, new StaticResourceActionResolver(servletContext));
		routes.addActionResolver(MethodAction.class, new MethodActionResolver(injectionContext));
	}

	protected void loadProperties(UpdatableInjectionContext injectionContext) {
		String filename = "application.properties";
		Properties properties = new Properties();
		try {
			InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream(filename);
			properties.load(propertiesStream);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to load application properties file %s: %s", filename, e.getMessage());
		}
		for (Object key : Expressive.iterable(properties.propertyNames())) {
			String value = (String) properties.get(key);
			injectionContext.inject(String.class).named((String) key).as(value);
		}
	}

	protected void addServices(UpdatableInjectionContext injectionContext) {

	}

	protected void addRoutes(Routes routes, UpdatableInjectionContext injectionContext) {
		String routesSource = IOUtil.loadText(this.getClass().getClassLoader().getResourceAsStream("routes.json"), "UTF-8");
		List<Route> routeMap = Routes.parseJsonRoutes(routesSource);
		routes.addRoutes(routeMap);
	}

	protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
		ServletContext servletContext = injectionContext.get(ServletContext.class);
		viewResolverRegistry.addResolver(TemplateViewResult.class, new TemplateViewResolver(servletContext));
		viewResolverRegistry.addResolver(JsonViewResult.class, new JsonViewResolver());
		viewResolverRegistry.addResolver(JspViewResult.class, new JspViewResolver());
		viewResolverRegistry.addResolver(Throwable.class, new ExceptionViewResolver());
	}

}
