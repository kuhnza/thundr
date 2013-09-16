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
package com.threewks.thundr.action.method.bind;

import java.util.LinkedHashMap;
import java.util.Map;

import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.action.method.bind.http.MultipartHttpBinder;
import com.threewks.thundr.action.method.bind.json.GsonBinder;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.action.method.bind.request.CookieBinder;
import com.threewks.thundr.action.method.bind.request.RequestAttributeBinder;
import com.threewks.thundr.action.method.bind.request.RequestClassBinder;
import com.threewks.thundr.action.method.bind.request.RequestHeaderBinder;
import com.threewks.thundr.action.method.bind.request.SessionAttributeBinder;

public class ActionMethodBinderRegistry {
	private Map<Class<? extends ActionMethodBinder>, ActionMethodBinder> methodBinders = new LinkedHashMap<Class<? extends ActionMethodBinder>, ActionMethodBinder>();

	public ActionMethodBinderRegistry() {
	}

	public void registerDefaultActionMethodBinders() {
		HttpBinder httpBinder = new HttpBinder();
		registerActionMethodBinder(new PathVariableBinder());
		registerActionMethodBinder(new RequestClassBinder());
		registerActionMethodBinder(new GsonBinder());
		registerActionMethodBinder(httpBinder);
		registerActionMethodBinder(new MultipartHttpBinder(httpBinder));
		registerActionMethodBinder(new RequestAttributeBinder(httpBinder));
		registerActionMethodBinder(new SessionAttributeBinder(httpBinder));
		registerActionMethodBinder(new RequestHeaderBinder(httpBinder));
		registerActionMethodBinder(new CookieBinder(httpBinder));
	}

	public void registerActionMethodBinder(ActionMethodBinder binder) {
		methodBinders.put(binder.getClass(), binder);
	}

	public void deregisterActionMethodBinder(Class<? extends ActionMethodBinder> type) {
		methodBinders.remove(type);
	}

	public Iterable<ActionMethodBinder> getRegisteredBinders() {
		return methodBinders.values();
	}
}
