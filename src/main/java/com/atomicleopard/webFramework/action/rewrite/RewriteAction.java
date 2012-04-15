package com.atomicleopard.webFramework.action.rewrite;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.action.Action;
import com.atomicleopard.webFramework.route.Route;

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
