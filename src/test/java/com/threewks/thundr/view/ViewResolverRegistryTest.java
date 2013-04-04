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
package com.threewks.thundr.view;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ViewResolverRegistryTest {
	private ViewResolverRegistry registry = new ViewResolverRegistry();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void shouldResolveWithMostSpecifiedRegisteredViewResolverType() {
		ViewResolver resolverA = mock(ViewResolver.class);
		ViewResolver resolverB = mock(ViewResolver.class);
		registry.addResolver(Throwable.class, resolverA);
		registry.addResolver(RuntimeException.class, resolverB);

		assertThat(registry.findViewResolver(new Throwable()), is(resolverA));
		assertThat(registry.findViewResolver(new RuntimeException()), is(resolverB));
		assertThat(registry.findViewResolver(new IllegalArgumentException()), is(resolverB));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldCacheViewResolverMatchAndUseForSubsequentRequests() {
		ViewResolver resolver = mock(ViewResolver.class);
		registry = spy(registry);
		registry.addResolver(Throwable.class, resolver);

		IllegalArgumentException first = new IllegalArgumentException();
		registry.findViewResolver(first);
		verify(registry).findViewResolverInCache(first);
		verify(registry).createAndCacheResolver(first);

		assertThat(registry.findViewResolverInCache(first), is(resolver));

		IllegalArgumentException second = new IllegalArgumentException();
		registry.findViewResolver(second);
		verify(registry).findViewResolverInCache(second);
		verify(registry, times(0)).createAndCacheResolver(second);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldCacheViewResolverMatchAndUseForSubsequentRequestsOnlyOfMatchingType() {
		ViewResolver resolver = mock(ViewResolver.class);
		registry = spy(registry);
		registry.addResolver(Throwable.class, resolver);

		IllegalArgumentException first = new IllegalArgumentException();
		registry.findViewResolver(first);
		verify(registry).findViewResolverInCache(first);
		verify(registry).createAndCacheResolver(first);

		assertThat(registry.findViewResolverInCache(first), is(resolver));

		RuntimeException second = new RuntimeException();
		registry.findViewResolver(second);
		verify(registry).findViewResolverInCache(second);
		verify(registry).createAndCacheResolver(second);

		assertThat(registry.findViewResolverInCache(new RuntimeException()), is(resolver));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldReturnNullIfUnableToResolveView() {
		assertThat(registry.findViewResolver(null), is(nullValue()));
		assertThat(registry.findViewResolver(""), is(nullValue()));
		assertThat(registry.findViewResolver(new RuntimeException()), is(nullValue()));

		ViewResolver resolver = mock(ViewResolver.class);
		registry.addResolver(Throwable.class, resolver);
		assertThat(registry.findViewResolver(new RuntimeException()), is(notNullValue()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldRemoveViewResolverAndClearCache() {
		ViewResolver resolver = mock(ViewResolver.class);
		registry.addResolver(Throwable.class, resolver);

		RuntimeException viewResult = new RuntimeException();
		assertThat(registry.findViewResolver(viewResult), is(notNullValue()));
		assertThat(registry.findViewResolverInCache(viewResult), is(resolver));

		registry.removeResolver(Throwable.class);
		assertThat(registry.findViewResolver(viewResult), is(nullValue()));
		assertThat(registry.findViewResolverInCache(viewResult), is(nullValue()));
	}
}
