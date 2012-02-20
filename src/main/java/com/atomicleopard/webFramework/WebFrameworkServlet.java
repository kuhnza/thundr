package com.atomicleopard.webFramework;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.scalate.TemplateEngine;
import org.fusesource.scalate.servlet.ServletRenderContext;
import org.fusesource.scalate.servlet.ServletResourceLoader;
import org.fusesource.scalate.ssp.SspCodeGenerator;
import org.fusesource.scalate.support.CodeGenerator;
import org.fusesource.scalate.util.IOUtil;

import scala.Tuple2;
import scala.collection.immutable.Map;

import com.atomicleopard.webFramework.routes.RouteType;
import com.atomicleopard.webFramework.routes.Routes;
import com.atomicleopard.webFramework.scalate.ClasspathResourceLoader;

public class WebFrameworkServlet extends HttpServlet {
	public static final Logger logger = Logger.getLogger(WebFrameworkServlet.class.getName());
	private TemplateEngine templateEngine;
	private Routes routes;

	public WebFrameworkServlet() throws ClassNotFoundException {
		templateEngine = new TemplateEngine(null, "development");
		applyTemplateEngineConfiguration(templateEngine);
		templateEngine.allowCaching_$eq(false);
		templateEngine.allowReload_$eq(false);

		String routesSource = IOUtil.loadText(this.getClass().getClassLoader().getResourceAsStream("routes.json"), "UTF-8");
		java.util.Map<String, Tuple2<String, RouteType>> routeMap = Routes.parseJsonRoutes(routesSource);
		routes = new Routes();
		routes.addRoutes(routeMap);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		templateEngine.resourceLoader_$eq(new ServletResourceLoader(getServletContext(), new ClasspathResourceLoader()));
		ServletRenderContext context = new ServletRenderContext(templateEngine, req, resp, getServletContext());

		String requestPath = req.getPathInfo();
		logger.warning(String.format("Invoking path %s", requestPath));
		try {
			routes.invoke(requestPath);
			logger.warning(String.format("Invoked path %s!!", requestPath));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			if (templateEngine.canLoad("test.html")) {
				templateEngine.layout("test.html", context);
			} else {
				resp.getWriter().println("Cannot find /views/test.html");
			}
		} catch (Exception e) {
			resp.getWriter().println("Exception " + e.getMessage());
			e.printStackTrace(resp.getWriter());
		}
		resp.setContentType("text/html");
		resp.setStatus(200);
	}

	public static void applyTemplateEngineConfiguration(TemplateEngine templateEngine) {
		Map<String, CodeGenerator> codeGenerators = templateEngine.codeGenerators();
		CodeGenerator sspGenerator = new SspCodeGenerator();
		codeGenerators = codeGenerators.$plus(new Tuple2<String, CodeGenerator>("html", sspGenerator));
		templateEngine.codeGenerators_$eq(codeGenerators);
	}
}
