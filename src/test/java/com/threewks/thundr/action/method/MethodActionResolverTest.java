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
package com.threewks.thundr.action.method;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.injection.InjectorBuilder;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.route.RouteType;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;

public class MethodActionResolverTest {
	private MethodActionResolver resolver;
	private UpdatableInjectionContext injectionContext;
	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private Map<String, String> pathVars = map();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before
	public void before() {
		injectionContext = mock(UpdatableInjectionContext.class);
		InjectorBuilder<Class> injectionBuilder = mock(InjectorBuilder.class);
		when(injectionContext.inject(Mockito.any(Class.class))).thenReturn(injectionBuilder);
		resolver = new MethodActionResolver(injectionContext);
		when(req.getContentType()).thenReturn(ContentType.ApplicationFormUrlEncoded.value());
	}

	@Test
	public void shouldAllowRegistrationOfActionInterceptors() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, null);
		resolver.registerInterceptor(TestAnnotation.class, registeredInterceptor);
		ActionInterceptor<?> interceptor = resolver.interceptor(TestAnnotation.class);
		assertThat(interceptor, is(notNullValue()));
		assertThat(interceptor == registeredInterceptor, is(true));
	}

	@Test
	public void shouldReplaceExistingInterceptorWithNewInterceptor() {
		TestActionInterceptor registeredInterceptor1 = new TestActionInterceptor(null, null, null);
		TestActionInterceptor registeredInterceptor2 = new TestActionInterceptor(null, null, null);
		resolver.registerInterceptor(TestAnnotation.class, registeredInterceptor1);
		resolver.registerInterceptor(TestAnnotation.class, registeredInterceptor2);
		ActionInterceptor<?> interceptor = resolver.interceptor(TestAnnotation.class);
		assertThat(interceptor, is(notNullValue()));
		assertThat(interceptor == registeredInterceptor1, is(false));
		assertThat(interceptor == registeredInterceptor2, is(true));
	}

	@Test
	public void shouldFindRegisteredInterceptorsForMethod() throws SecurityException, NoSuchMethodException {
		Method method = MethodActionResolverTest.class.getMethod("intercept");
		TestAnnotation annotation = method.getAnnotation(TestAnnotation.class);

		Map<Annotation, ActionInterceptor<Annotation>> interceptors = resolver.findInterceptors(method);
		assertThat(interceptors.isEmpty(), is(true));

		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, null);
		resolver.registerInterceptor(TestAnnotation.class, registeredInterceptor);

		interceptors = resolver.findInterceptors(method);
		assertThat(interceptors.isEmpty(), is(false));
		assertThat(interceptors.get(annotation).equals(registeredInterceptor), is(true));
	}

	@Test
	public void shouldInvokeInterceptorBeforeActionMethod() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, null);
		MethodAction action = prepareActionMethod("intercept", registeredInterceptor);

		assertThat(registeredInterceptor.beforeInvoked, is(false));
		resolver.resolve(action, RouteType.GET, req, resp, pathVars);
		assertThat(registeredInterceptor.beforeInvoked, is(true));
	}

	@Test
	public void shouldInvokeInterceptorBeforeActionMethodNotInvokingMethodIsSomethingIsReturned() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor("Expected Before", null, null);
		MethodAction action = prepareActionMethod("intercept", registeredInterceptor);

		assertThat(registeredInterceptor.beforeInvoked, is(false));
		assertThat((String) resolver.resolve(action, RouteType.GET, req, resp, pathVars), is("Expected Before"));
		assertThat(registeredInterceptor.beforeInvoked, is(true));
		assertThat(registeredInterceptor.afterInvoked, is(false));
		assertThat(registeredInterceptor.exceptionInvoked, is(false));
	}

	@Test
	public void shouldBindArgumentsAfterInvokingBeforeInterceptorAllowingBindingsToUseValuesModifiedByInterceptor() {
		@SuppressWarnings("unchecked")
		ActionInterceptor<TestAnnotation> registeredInterceptor = new ActionInterceptor<TestAnnotation>() {
			@Override
			public String before(TestAnnotation annotation, HttpServletRequest req, HttpServletResponse resp) {
				req.setAttribute("name", "value");
				return null;
			}

			@Override
			public String after(TestAnnotation annotation, Object result, HttpServletRequest req, HttpServletResponse resp) {
				return null;
			}

			@Override
			public String exception(TestAnnotation annotation, Exception e, HttpServletRequest req, HttpServletResponse resp) {
				return null;
			}

		};
		MethodAction action = prepareActionMethod("interceptWithValue", registeredInterceptor);

		req = new MockHttpServletRequest();
		Object resolvedValue = resolver.resolve(action, RouteType.GET, req, resp, pathVars);
		assertThat(resolvedValue, is((Object) "value"));
	}

	@Test
	public void shouldInvokeInterceptorAfterActionMethod() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, null);
		MethodAction action = prepareActionMethod("intercept", registeredInterceptor);

		assertThat(registeredInterceptor.afterInvoked, is(false));
		resolver.resolve(action, RouteType.GET, req, resp, pathVars);
		assertThat(registeredInterceptor.afterInvoked, is(true));
	}

	@Test
	public void shouldInvokeInterceptorAfterActionMethodReturingInterceptorValueIfNotNull() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, "Expected After", null);
		MethodAction action = prepareActionMethod("intercept", registeredInterceptor);

		assertThat((String) resolver.resolve(action, RouteType.GET, req, resp, pathVars), is("Expected After"));
		assertThat(registeredInterceptor.beforeInvoked, is(true));
		assertThat(registeredInterceptor.afterInvoked, is(true));
		assertThat(registeredInterceptor.exceptionInvoked, is(false));
	}

	@Test
	public void shouldInvokeInterceptorOnExceptionFromActionMethod() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, "invoked");
		MethodAction action = prepareActionMethod("interceptException", registeredInterceptor);

		assertThat(registeredInterceptor.exceptionInvoked, is(false));
		resolver.resolve(action, RouteType.GET, req, resp, pathVars);
		assertThat(registeredInterceptor.exceptionInvoked, is(true));
	}

	@Test
	public void shouldInvokeInterceptorWhenExceptionInActionMethodReturingInterceptorValueIfNotNull() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, "Expected Exception");
		MethodAction action = prepareActionMethod("interceptException", registeredInterceptor);

		assertThat((String) resolver.resolve(action, RouteType.GET, req, resp, pathVars), is("Expected Exception"));
		assertThat(registeredInterceptor.beforeInvoked, is(true));
		assertThat(registeredInterceptor.afterInvoked, is(false));
		assertThat(registeredInterceptor.exceptionInvoked, is(true));
	}

	@Test
	public void shouldInvokeInterceptorWhenExceptionInActionMethodThrowingIfInterceptorValueIsNull() {
		TestActionInterceptor registeredInterceptor = new TestActionInterceptor(null, null, null);
		MethodAction action = prepareActionMethod("interceptException", registeredInterceptor);

		try {
			resolver.resolve(action, RouteType.GET, req, resp, pathVars);
			fail("Expected an exception");
		} catch (RuntimeException e) {
			// expected
		}
		assertThat(registeredInterceptor.beforeInvoked, is(true));
		assertThat(registeredInterceptor.afterInvoked, is(false));
		assertThat(registeredInterceptor.exceptionInvoked, is(true));
	}

	@Test
	public void shouldReturnNullIfCannotCreateAction() {
		assertThat(resolver.createActionIfPossible(null), is(nullValue()));
		assertThat(resolver.createActionIfPossible(""), is(nullValue()));
		assertThat(resolver.createActionIfPossible("Junk"), is(nullValue()));
		assertThat(resolver.createActionIfPossible(".method"), is(nullValue()));
		assertThat(resolver.createActionIfPossible(MethodActionResolverTest.class.getName()), is(nullValue()));
		assertThat(resolver.createActionIfPossible(MethodActionResolverTest.class.getName() + ".aintNoSuchMethod"), is(nullValue()));
	}

	@Test
	public void shouldCreateActionMethodClassAtCreationTime() {
		resolver = spy(resolver);
		resolver.createActionIfPossible(MethodActionResolverTest.class.getName() + ".intercept");
		verify(resolver).createController(Mockito.any(MethodAction.class));
	}

	private MethodAction prepareActionMethod(String method, ActionInterceptor<TestAnnotation> registeredInterceptor) {
		when(injectionContext.get(MethodActionResolverTest.class)).thenReturn(this);
		resolver.registerInterceptor(TestAnnotation.class, registeredInterceptor);
		MethodAction action = resolver.createActionIfPossible(MethodActionResolverTest.class.getName() + "." + method);
		assertThat(action, is(notNullValue()));
		return action;
	}

	@TestAnnotation("Parameter")
	public void intercept() {

	}

	@TestAnnotation("Parameter")
	public String interceptWithValue(String name) {
		return name;
	}

	@TestAnnotation("Parameter")
	public void interceptException() {
		throw new RuntimeException("Expected");
	}

	@SuppressWarnings("unchecked")
	private class TestActionInterceptor implements ActionInterceptor<TestAnnotation> {
		private String onBefore = null;
		private String onAfter = null;
		private String onException = null;
		public boolean exceptionInvoked;
		public boolean afterInvoked;
		public boolean beforeInvoked;

		public TestActionInterceptor(String onBefore, String onAfter, String onException) {
			super();
			this.onBefore = onBefore;
			this.onAfter = onAfter;
			this.onException = onException;
		}

		@Override
		public String before(TestAnnotation annotation, HttpServletRequest req, HttpServletResponse resp) {
			beforeInvoked = true;
			return onBefore;
		}

		@Override
		public String after(TestAnnotation annotation, Object result, HttpServletRequest req, HttpServletResponse resp) {
			afterInvoked = true;
			return onAfter;
		}

		@Override
		public String exception(TestAnnotation annotation, Exception e, HttpServletRequest req, HttpServletResponse resp) {
			exceptionInvoked = true;
			return onException;
		}
	}
}
