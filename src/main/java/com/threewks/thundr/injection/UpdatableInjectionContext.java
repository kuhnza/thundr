package com.threewks.thundr.injection;

public interface UpdatableInjectionContext extends InjectionContext {
	public <T> InjectorBuilder<T> inject(Class<T> type);

	public <T> InjectorBuilder<T> inject(T instance);
}
