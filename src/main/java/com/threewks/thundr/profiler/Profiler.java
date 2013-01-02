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
package com.threewks.thundr.profiler;

import java.util.UUID;

public interface Profiler {
	/**
	 * A No-op implementation of the {@link Profiler} interface.
	 * This allows test-safe and injection safe usage in dependant classes.
	 * 
	 * <code>
	 * @Inject
	 * public Profiler profiler = Profiler.None;
	 * </code>
	 * With this style of definition, the profiler can be injected automatically as well as manually, as well
	 * as mocked or stubbed for testing.
	 */
	public static final Profiler None = new NoProfiler();
	
	public static final String CategoryAction = "Action";
	public static final String CategoryDatabase = "Database";
	public static final String CategoryDatabaseRead = "DatabaseRead";
	public static final String CategoryDatabaseWrite = "DatabaseWrite";
	public static final String CategorySearch = "Search";
	public static final String CategoryHttp = "Http";
	public static final String CategoryHttpRequest = "HttpRequest";
	public static final String CategoryHttpResponse = "HttpResponse";
	public static final String CategoryService = "Service";
	public static final String CategoryServiceRequest = "ServiceRequest";
	public static final String CategoryServiceResponse = "ServiceResponse";
	public static final String CategoryBusinessLogic = "BusinessLogic";
	public static final String CategoryView = "View";

	public void beginProfileSession(String data);

	public void endProfileSession();

	public UUID start(String category, String data);

	public void end(UUID eventKey);

	public void end(UUID eventKey, ProfileEventStatus status);

	public ProfileSession getCurrent();

	public <T> T profile(String category, String data, Profilable<T> profilable);
}
