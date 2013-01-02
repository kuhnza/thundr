/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
