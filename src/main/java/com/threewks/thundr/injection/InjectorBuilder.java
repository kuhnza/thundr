package com.threewks.thundr.injection;

public class InjectorBuilder<T> {
	private InjectionContextImpl injector;
	private Class<T> type;
	private String name;

	protected InjectorBuilder(InjectionContextImpl injector, Class<T> type) {
		this.type = type;
		this.injector = injector;
	}

	public InjectorBuilder<T> named(String name) {
		this.name = name;
		return this;
	}

	public void as(Class<? extends T> as) {
		injector.addType(type, name, as);
	}

	public T as(T as) {
		injector.addInstance(type, name, as);
		return as;
	}
}
