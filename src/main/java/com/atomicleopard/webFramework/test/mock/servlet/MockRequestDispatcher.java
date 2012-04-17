package com.atomicleopard.webFramework.test.mock.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockRequestDispatcher implements RequestDispatcher {
	private String path;

	public MockRequestDispatcher(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@Override
	public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new IllegalStateException("Reponse already commited");
		}
	}

	@Override
	public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new IllegalStateException("Reponse already commited");
		}
	}
}
