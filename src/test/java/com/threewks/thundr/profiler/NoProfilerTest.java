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
