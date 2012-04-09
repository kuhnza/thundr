package com.atomicleopard.webFramework.view.scalate;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.scalate.RenderContext;
import org.fusesource.scalate.TemplateEngine;
import org.fusesource.scalate.servlet.ServletRenderContext;
import org.fusesource.scalate.servlet.ServletResourceLoader;
import org.fusesource.scalate.ssp.SspCodeGenerator;
import org.fusesource.scalate.support.CodeGenerator;

import scala.Some;
import scala.Tuple2;

import com.atomicleopard.webFramework.scalate.ClasspathResourceLoader;
import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class TemplateViewResolver implements ViewResolver<TemplateViewResult> {

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();

	public TemplateViewResolver(ServletContext servletContext) {
		this.servletContext = servletContext;
		templateEngine = new TemplateEngine(null, "development");
		templateEngine.resourceLoader_$eq(new ServletResourceLoader(servletContext, new ClasspathResourceLoader()));
		applyTemplateEngineConfiguration(templateEngine);
	}

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, TemplateViewResult viewResult) {
		String view = viewResult.getView();
		try {
			if (templateEngine.canLoad(view)) {
				RenderContext context = new ServletRenderContext(templateEngine, req, resp, servletContext);
				for (Map.Entry<String, Object> attr : viewResult.getModel().entrySet()) {
					context.setAttribute(attr.getKey(), new Some<Object>(attr.getValue()));
				}
				templateEngine.layout(view, context);
				resp.setContentType(viewResult.getContentType());
				resp.setStatus(viewResult.getResponseCode());
			} else {
				throw new ViewResolutionException("Cannot find view '%s'", view);
			}
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed while rendering view '%s': %s", view, e.getMessage());
		}
	}

	public static void applyTemplateEngineConfiguration(TemplateEngine templateEngine) {
		scala.collection.immutable.Map<String, CodeGenerator> codeGenerators = templateEngine.codeGenerators();
		CodeGenerator sspGenerator = new SspCodeGenerator();
		codeGenerators = codeGenerators.$plus(new Tuple2<String, CodeGenerator>("html", sspGenerator));
		templateEngine.codeGenerators_$eq(codeGenerators);
		templateEngine.allowCaching_$eq(false);
		templateEngine.allowReload_$eq(false);
	}

}
