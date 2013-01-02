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
package com.threewks.thundr.persist;

/**
 * The app-engine datastore is not good at incremental counters. To surmount this, we need a customer counter implementation.
 * @see DatastoreCounterRepository
 */
public interface CounterRepository {

	public long getCount(String counterType);

	public long incrementCount(String counterType);

	public long decrementCount(String counterType);

	public long getCount(String counterType, String association);

	public long incrementCount(String counterType, String association);

	long incrementCountBy(String counterType, String association, long by);

	public long decrementCount(String counterType, String association);

	public long decrementCountBy(String counterType, String association, long by);
	
	public long setCount(String counterType, String association, long value);
}
