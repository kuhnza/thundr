package com.threewks.thundr.injection;

public class InjectorBuilder<T> {
	private InjectionContextImpl injector;
	private Class<T> type;
	private String name;
	private T instance;

	protected InjectorBuilder(InjectionContextImpl injector, Class<T> type) {
		this.type = type;
		this.injector = injector;
	}

	protected InjectorBuilder(InjectionContextImpl injector, T instance) {
		this.injector = injector;
		this.instance = instance;
	}

	public InjectorBuilder<T> named(String name) {
		this.name = name;
		return this;
	}

	public void as(Class<? super T> interfaceType) {
		if (type != null) {
			injector.addType(interfaceType, name, type);
		} else {
			injector.addInstance(interfaceType, name, instance);
		}
	}
}
