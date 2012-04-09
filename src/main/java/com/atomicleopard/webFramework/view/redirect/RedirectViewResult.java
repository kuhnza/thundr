package com.atomicleopard.webFramework.view.redirect;

public class RedirectViewResult {
	private String path;

	public RedirectViewResult(String path) {
		this.path = path;
	}

	public String getRedirect() {
		return path;
	}

	@Override
	public String toString() {
		return "Redirect to " + path;
	}
}
