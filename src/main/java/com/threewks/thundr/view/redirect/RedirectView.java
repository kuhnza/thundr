package com.threewks.thundr.view.redirect;

import com.threewks.thundr.view.View;

public class RedirectView implements View {
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
