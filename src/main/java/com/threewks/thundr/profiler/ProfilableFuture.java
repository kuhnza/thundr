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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProfilableFuture<T> implements Future<T> {
	public Profiler profiler;
	public Future<T> delegate;
	private UUID key;

	public ProfilableFuture(String category, String data, Profiler profiler, Future<T> delegate) {
		this(profiler, profiler.start(category, data), delegate);
	}

	public ProfilableFuture(Profiler profiler, UUID key, Future<T> delegate) {
		super();
		this.profiler = profiler;
		this.delegate = delegate;
		this.key = key;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate.cancel(mayInterruptIfRunning);
	}

	public T get() throws InterruptedException, ExecutionException {
		try {
			T t = delegate.get();
			profiler.end(key);
			return t;
		} catch (InterruptedException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		} catch (ExecutionException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		} catch (RuntimeException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		}
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			T t = delegate.get(timeout, unit);
			profiler.end(key);
			return t;
		} catch (InterruptedException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		} catch (ExecutionException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		} catch (TimeoutException e) {
			profiler.end(key, ProfileEventStatus.Timeout);
			throw e;
		} catch (RuntimeException e) {
			profiler.end(key, ProfileEventStatus.Failed);
			throw e;
		}
	}

	public boolean isCancelled() {
		return delegate.isCancelled();
	}

	public boolean isDone() {
		return delegate.isDone();
	}
}