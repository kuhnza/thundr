package com.atomicleopard.webFramework.view.scalate;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import org.fusesource.scalate.util.IOUtil;
import org.fusesource.scalate.util.Resource;
import org.fusesource.scalate.util.ResourceLoader;
import org.fusesource.scalate.util.ResourceNotFoundException;
import org.fusesource.scalate.util.URLResource;

import scala.Option;
import scala.Some;

public class ClasspathResourceLoader implements ResourceLoader {
	public static final Logger logger = Logger.getLogger("ClassPathResourceLoader");
	public static final String UTF8 = "UTF-8";
	private ClassLoader classLoader;

	public ClasspathResourceLoader() {
		this.classLoader = this.getClass().getClassLoader();
	}

	public ClasspathResourceLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public ResourceNotFoundException createNotFoundException(String arg0) {
		return null;
	}

	@Override
	public boolean exists(String arg0) {
		return classLoader.getResource(arg0) != null;
	}

	@Override
	public long lastModified(String arg0) {
		return resourceOrFail(arg0).lastModified();
	}

	@Override
	public String load(String arg0) {
		InputStream resourceAsStream = classLoader.getResourceAsStream(arg0);
		return IOUtil.loadText(resourceAsStream, UTF8);
	}

	@Override
	public void org$fusesource$scalate$util$ResourceLoader$_setter_$pageFileEncoding_$eq(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String pageFileEncoding() {
		return UTF8;
	}

	@Override
	public String resolve(String base, String path) {
		try {
			return path.startsWith("/") ? path : new URI(base).resolve(path).toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Option<Resource> resource(String arg0) {
		return new Some<Resource>(resourceOrFail(arg0));
	}

	@Override
	public Resource resourceOrFail(String arg0) {
		logger.warning("Attempting to get " + arg0);
		URL resource = classLoader.getResource(arg0);
		if (resource == null) {
			logger.warning("Stripping slash " + arg0);
			resource = classLoader.getResource(arg0.replaceFirst("/", ""));
		}
		if (resource == null) {
			ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(arg0, "", "");
			throw new RuntimeException(resourceNotFoundException.getMessage(), resourceNotFoundException);
		}
		return new URLResource(resource);
	}

}
