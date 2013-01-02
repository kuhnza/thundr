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
package com.threewks.thundr.injection;

public interface InjectionContext {
	/**
	 * Get an named instance of the specified type from this {@link InjectionContext}.
	 * A type exists if either an instance or class type was registered previously into this context using the specified name,
	 * or if an instance or class type was registered without a specific name.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws NullPointerException
	 */
	public <T> T get(Class<T> type, String name) throws NullPointerException;

	/**
	 * Get an instance of the specified type from this {@link InjectionContext}.
	 * A type exists if either an instance or class type was registered previously into this context.
	 * 
	 * @param type
	 * @return
	 * @throws NullPointerException
	 */
	public <T> T get(Class<T> type) throws NullPointerException;

	/**
	 * Returns true if this {@link InjectionContext} can return an object instance from {@link #get(Class)} based on the given type.
	 * 
	 * @param type
	 * @return
	 */
	public <T> boolean contains(Class<T> type);

	/**
	 * Returns true if this {@link InjectionContext} can return an object instance from {@link #get(Class, String)} based on the given type and name.
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public <T> boolean contains(Class<T> type, String name);
}
