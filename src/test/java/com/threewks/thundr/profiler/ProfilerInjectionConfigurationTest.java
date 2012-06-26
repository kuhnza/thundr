package com.threewks.thundr.profiler;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ProfilerInjectionConfigurationTest {
	@Test
	public void shouldInjectNoProfilerImplementationByDefault() {
		UpdatableInjectionContext injectionContext = new InjectionContextImpl();
		new ProfilerInjectionConfiguration().configure(injectionContext);
		assertThat(injectionContext.get(Profiler.class) instanceof NoProfiler, is(true));

	}
}
