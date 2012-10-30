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