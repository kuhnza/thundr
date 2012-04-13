package com.atomicleopard.webFramework.view.redirect;

public class RedirectView {
	private String path;

	public RedirectView(String path) {
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
