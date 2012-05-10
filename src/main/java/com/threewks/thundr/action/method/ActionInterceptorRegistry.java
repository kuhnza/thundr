package com.threewks.thundr.action.method;

import java.lang.annotation.Annotation;

public interface ActionInterceptorRegistry {
	public <A extends Annotation> void registerInterceptor(Class<A> annotation, ActionInterceptor<A> interceptor);
}
