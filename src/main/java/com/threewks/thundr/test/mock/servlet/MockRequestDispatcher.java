package com.threewks.thundr.test.mock.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockRequestDispatcher implements RequestDispatcher {
	private String path;
	private boolean forwarded = false;
	private boolean included = false;

	public void lastPath(String path) {
		this.path = path;
	}

	public String lastPath() {
		return path;
	}

	public boolean forwarded() {
		return forwarded;
	}

	public boolean included() {
		return included;
	}

	public String getPath() {
		return path;
	}

	@Override
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new IllegalStateException("Reponse already commited");
		}
		forwarded = true;
	}

	@Override
	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new IllegalStateException("Reponse already commited");
		}
		included = true;
	}
}
