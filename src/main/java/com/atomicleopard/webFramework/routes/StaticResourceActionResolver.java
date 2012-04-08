package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.webFramework.http.HttpSupport.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.servlet.filter.GzipResponseStream;
import jodd.util.Wildcard;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.logger.Logger;

public class StaticResourceActionResolver implements ActionResolver<StaticResourceAction> {
	private static final String ActionName = "static";
	private static final Pattern ActionNamePattern = Pattern.compile("^static:(.+)");

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private final String protectedPath = "/?WEB-INF/.*";

	private boolean gzipEnabled = true;

	private Map<String, String> defaultMimeTypes = new HashMap<String, String>();
	{
		defaultMimeTypes.put(".css", "text/css");
		defaultMimeTypes.put(".gif", "image/gif");
		defaultMimeTypes.put(".ico", "image/vnd.microsoft.icon");
		defaultMimeTypes.put(".jpeg", "image/jpeg");
		defaultMimeTypes.put(".jpg", "image/jpeg");
		defaultMimeTypes.put(".js", "text/javascript");
		defaultMimeTypes.put(".png", "image/png");
	}

	private Set<String> compressedMimeTypes = new HashSet<String>();
	{
		compressedMimeTypes.add("text/*");
	}

	private int cacheDuration = 24 * 60 * 60;
	private ServletContext servletContext;

	public StaticResourceActionResolver(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public StaticResourceAction createActionIfPossible(String actionName) {
		if (ActionName.equalsIgnoreCase(actionName) || ActionNamePattern.matcher(actionName).matches()) {
			return new StaticResourceAction();
		}
		return null;
	}

	@Override
	public Object resolve(StaticResourceAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		try {
			serve(action, req, resp);
			return null;
		} catch (Exception e) {
			Throwable original = e.getCause() == null ? e : e.getCause();
			throw new BaseException(original, "Failed to load resource %s: %s", req.getRequestURI(), original.getMessage());
		}
	}

	protected void serve(StaticResourceAction action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resource = request.getRequestURI();
		URL resourceUrl = servletContext.getResource(resource);
		boolean allowed = isAllowed(resource);
		if (resourceUrl == null || !allowed) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			Logger.info("%s -> %s not resolved: %s", resource, action, allowed ? "Not found" : "Not Permitted");
			return;
		}
		URLConnection urlConnection = resourceUrl.openConnection();
		String mimeType = deriveMimeType(resource);
		long contentLength = urlConnection.getContentLength();
		long lastModified = urlConnection.getLastModified();
		String acceptEncoding = request.getHeader(HttpHeaderAcceptEncoding);
		long cacheTimeSeconds = deriveCacheDuration(resource, mimeType);
		boolean zip = shouldZip(acceptEncoding, mimeType);

		response.setContentType(mimeType);
		response.setDateHeader(HttpHeaderExpires, System.currentTimeMillis() + cacheTimeSeconds * 1000L); // HTTP 1.0
		response.setHeader(HttpHeaderCacheControl, "max-age=" + cacheTimeSeconds); // HTTP 1.1
		response.setHeader(HttpHeaderContentLength, Long.toString(contentLength));
		response.setDateHeader(HttpHeaderLastModified, lastModified);
		OutputStream os = zip ? new GzipResponseStream(response) : response.getOutputStream();

		try {
			InputStream is = urlConnection.getInputStream();
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
			}
		} finally {
			os.close();
		}
		response.setStatus(HttpServletResponse.SC_OK);
		Logger.info("%s -> %s resolved as %s(%d bytes)", resource, action, mimeType, contentLength);
	}

	private long deriveCacheDuration(String resource, String mimeType) {
		return cacheDuration;
	}

	private String deriveMimeType(String resource) {
		String mimeType = servletContext.getMimeType(resource);
		if (mimeType == null) {
			String extension = resource.substring(resource.lastIndexOf('.'));
			mimeType = defaultMimeTypes.get(extension);
		}
		return mimeType;
	}

	private boolean shouldZip(String acceptEncoding, String mimeType) {
		return gzipEnabled && StringUtils.indexOf(acceptEncoding, "gzip") > -1 && matchesCompressedMimeTypes(mimeType);
	}

	private boolean matchesCompressedMimeTypes(String mimeType) {
		for (String compressedMimeType : compressedMimeTypes) {
			if (Wildcard.match(compressedMimeType, mimeType)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAllowed(String resourcePath) {
		return !resourcePath.matches(protectedPath);
		// for (String pattern : allowedResourcePaths) {
		// if (Wildcard.match(pattern, resourcePath)) {
		// return true;
		// }
		// }
		// return false;
	}
	//
	// private Set<String> allowedResourcePaths = new HashSet<String>();
	// {
	// allowedResourcePaths.add("/**/*.css");
	// allowedResourcePaths.add("/**/*.gif");
	// allowedResourcePaths.add("/**/*.ico");
	// allowedResourcePaths.add("/**/*.jpeg");
	// allowedResourcePaths.add("/**/*.jpg");
	// allowedResourcePaths.add("/**/*.js");
	// allowedResourcePaths.add("/**/*.png");
	// allowedResourcePaths.add("META-INF/**/*.css");
	// allowedResourcePaths.add("META-INF/**/*.gif");
	// allowedResourcePaths.add("META-INF/**/*.ico");
	// allowedResourcePaths.add("META-INF/**/*.jpeg");
	// allowedResourcePaths.add("META-INF/**/*.jpg");
	// allowedResourcePaths.add("META-INF/**/*.js");
	// allowedResourcePaths.add("META-INF/**/*.png");
	// };
}
