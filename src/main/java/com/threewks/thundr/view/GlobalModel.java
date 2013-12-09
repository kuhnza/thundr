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
package com.threewks.thundr.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GlobalModel implements Map<String, Object> {
	private Map<String, Object> delegate = new HashMap<String, Object>();

	public void clear() {
		delegate.clear();
	}

	public boolean containsKey(Object arg0) {
		return delegate.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return delegate.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return delegate.entrySet();
	}

	public boolean equals(Object arg0) {
		return delegate.equals(arg0);
	}

	public Object get(Object arg0) {
		return delegate.get(arg0);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Set<String> keySet() {
		return delegate.keySet();
	}

	public Object put(String arg0, Object arg1) {
		return delegate.put(arg0, arg1);
	}

	public void putAll(Map<? extends String, ? extends Object> arg0) {
		delegate.putAll(arg0);
	}

	public Object remove(Object arg0) {
		return delegate.remove(arg0);
	}

	public int size() {
		return delegate.size();
	}

	public Collection<Object> values() {
		return delegate.values();
	}

}
