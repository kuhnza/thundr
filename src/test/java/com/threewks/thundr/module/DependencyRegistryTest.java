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
package com.threewks.thundr.module;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.module.test.TestInjectionConfiguration;
import com.threewks.thundr.module.test.m1.M1InjectionConfiguration;
import com.threewks.thundr.module.test.m2.M2InjectionConfiguration;

public class DependencyRegistryTest {

	@Test
	public void shouldRetainRegisteredDependenciesInOrder() {
		DependencyRegistry dependencyRegistry = new DependencyRegistry();
		dependencyRegistry.addDependency(M1InjectionConfiguration.class);
		dependencyRegistry.addDependency(M2InjectionConfiguration.class);
		dependencyRegistry.addDependency(TestInjectionConfiguration.class);

		assertThat(dependencyRegistry.hasDependency(M1InjectionConfiguration.class), is(true));
		assertThat(dependencyRegistry.hasDependency(M2InjectionConfiguration.class), is(true));
		assertThat(dependencyRegistry.hasDependency(TestInjectionConfiguration.class), is(true));

		assertThat(dependencyRegistry.getDependencies().isEmpty(), is(false));
		assertThat(dependencyRegistry.getDependencies().size(), is(3));
		Iterator<Class<? extends InjectionConfiguration>> iterator = dependencyRegistry.getDependencies().iterator();
		assertThat(iterator.next(), Matchers.<Class<? extends InjectionConfiguration>> is(M1InjectionConfiguration.class));
		assertThat(iterator.next(), Matchers.<Class<? extends InjectionConfiguration>> is(M2InjectionConfiguration.class));
		assertThat(iterator.next(), Matchers.<Class<? extends InjectionConfiguration>> is(TestInjectionConfiguration.class));
	}

	@Test
	public void shouldReturnEmptyCollectionWhenNothingRegistered() {
		assertThat(new DependencyRegistry().getDependencies(), is(notNullValue()));
		assertThat(new DependencyRegistry().getDependencies().isEmpty(), is(true));
	}

	@Test
	public void shouldOnlyRegisterEachDependencyOnce() {
		DependencyRegistry dependencyRegistry = new DependencyRegistry();
		dependencyRegistry.addDependency(M1InjectionConfiguration.class);
		dependencyRegistry.addDependency(M1InjectionConfiguration.class);
		dependencyRegistry.addDependency(M1InjectionConfiguration.class);

		assertThat(dependencyRegistry.hasDependency(M1InjectionConfiguration.class), is(true));

		assertThat(dependencyRegistry.getDependencies().isEmpty(), is(false));
		assertThat(dependencyRegistry.getDependencies().size(), is(1));
	}
}
