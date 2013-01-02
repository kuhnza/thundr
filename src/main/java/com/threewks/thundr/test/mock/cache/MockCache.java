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
package com.threewks.thundr.test.mock.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.CacheEntry;
import javax.cache.CacheListener;
import javax.cache.CacheStatistics;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MockCache implements Cache {
	private Map<String, Object> cache = new HashMap<String, Object>();

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return cache.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return cache.containsValue(arg0);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public Object get(Object arg0) {
		return cache.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return cache.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return cache.put((String) key, value);
	}

	@Override
	public void putAll(Map arg0) {
		cache.putAll(arg0);
	}

	@Override
	public Object remove(Object arg0) {
		return cache.remove(arg0);
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public Collection<Object> values() {
		return cache.values();
	}

	@Override
	public void addListener(CacheListener arg0) {
	}

	@Override
	public void evict() {
	}

	@Override
	public Map getAll(Collection arg0) {
		return null;
	}

	@Override
	public CacheEntry getCacheEntry(Object arg0) {
		return null;
	}

	@Override
	public CacheStatistics getCacheStatistics() {
		return null;
	}

	@Override
	public void load(Object arg0) {

	}

	@Override
	public void loadAll(Collection arg0) {

	}

	@Override
	public Object peek(Object arg0) {
		return null;
	}

	@Override
	public void removeListener(CacheListener arg0) {
	}

}
