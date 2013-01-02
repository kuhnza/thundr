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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.Test;

public class NoProfilerTest {

	@Test
	public void shouldReturnNewProfileSessionEachInvokcationToGetCurrent() {
		NoProfiler profiler = new NoProfiler();
		ProfileSession current = profiler.getCurrent();
		assertThat(current, is(notNullValue()));
		ProfileSession second = profiler.getCurrent();
		assertThat(current, is(not(sameInstance(second))));
	}

	@Test
	public void shouldReturnAndAcceptNullProfileEventKey() {
		NoProfiler profiler = new NoProfiler();
		UUID key = profiler.start("anything", "data");
		profiler.end(key);
		assertThat(key, is(nullValue()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldExecuteProfilable() {
		Profilable<Object> profilable = mock(Profilable.class);
		new NoProfiler().profile("cat", "data", profilable);
		verify(profilable, times(1)).profile();
	}

	@Test
	public void shouldNotFailWhenInvokingNoopMethods() {
		NoProfiler noProfiler = new NoProfiler();
		noProfiler.beginProfileSession("data");
		UUID key = noProfiler.start("cat", "data");
		noProfiler.end(key);
		noProfiler.end(key, ProfileEventStatus.Failed);
		noProfiler.endProfileSession();
	}
}
