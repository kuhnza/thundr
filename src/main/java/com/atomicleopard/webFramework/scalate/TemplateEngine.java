package com.atomicleopard.webFramework.scalate;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.fusesource.scalate.ssp.SspCodeGenerator;
import org.fusesource.scalate.support.Code;
import org.fusesource.scalate.support.StringTemplateSource;
import org.fusesource.scalate.util.IOUtil;

import scala.tools.nsc.Global;
import scala.tools.nsc.Settings;

public class TemplateEngine {
	public static final Logger logger = Logger.getLogger(TemplateEngine.class.getSimpleName());
	private ClassLoader classLoader = this.getClass().getClassLoader();
	private SspCodeGenerator codeGenerator;
	private org.fusesource.scalate.TemplateEngine scalateTemplateEngine;
	private ConcurrentHashMap<String, Code> templateCache = new ConcurrentHashMap<String, Code>(100);

	public Code loadTemplate(String template) {
		logger.info(String.format("Loading template %s", template));
		InputStream resource = classLoader.getResourceAsStream(template);
		if (resource == null) {
			logger.warning("Stripping slash " + template);
			resource = classLoader.getResourceAsStream(template.replaceFirst("/", ""));
		}
		if (resource == null) {
			throw new RuntimeException(String.format("Unable to load template '%s'", template));
		}
		String templateContent = IOUtil.loadText(resource, "UTF-8");
		logger.info(String.format("Loaded template %s", template));
		StringTemplateSource templateSource = new StringTemplateSource("", templateContent);
		Code generated = codeGenerator.generate(scalateTemplateEngine, templateSource, null);
		
		Settings settings = new Settings();
		Global scalaGlobal = new Global(settings);
	//	scalaGlobal.com
//		scalateTemplateEngine.compiler().compile(arg0)
		return generated;
	}

	public Code getTemplate(String template) {
		Code result = getCachedTemplate(template);
		if (result == null) {
			result = loadTemplate(template);
			result = cacheTemplate(template, result);
		}
		return result;
	}

	public Code getCachedTemplate(String template) {
		return templateCache.get(template);
	}

	public Code cacheTemplate(String template, Code result) {
		templateCache.put(template, result);
		return result;
	}

	public void uncacheTemplate(String template) {
		templateCache.remove(template);
	}
}
