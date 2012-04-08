package com.atomicleopard.webFramework.routes;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RewriteAction implements Action {
	private String rewriteTo;

	public RewriteAction(String rewriteTo) {
		this.rewriteTo = rewriteTo;
	}

	public String getRewriteTo(Map<String, String> pathVars) {
		String finalRewrite = rewriteTo;

		Matcher matcher = Route.PathParameterToken.matcher(rewriteTo);
		while (matcher.find()) {
			String token = matcher.group(1);
			finalRewrite = finalRewrite.replaceAll(Pattern.quote("{" + token + "}"), Matcher.quoteReplacement(StringUtils.trimToEmpty(pathVars.get(token))));
		}
		return finalRewrite;
	}

	@Override
	public String toString() {
		return "Rewrite:" + rewriteTo;
	}

}
